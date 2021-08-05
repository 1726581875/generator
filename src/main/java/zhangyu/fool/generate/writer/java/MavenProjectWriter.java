package zhangyu.fool.generate.writer.java;

import org.dom4j.Element;
import zhangyu.fool.generate.dao.DataBaseDAO;
import zhangyu.fool.generate.enums.ProjectEnum;
import zhangyu.fool.generate.model.Author;
import zhangyu.fool.generate.thread.WriterExecutorUtil;
import zhangyu.fool.generate.thread.WriterTask;
import zhangyu.fool.generate.util.BuildPath;
import zhangyu.fool.generate.util.FileUtil;
import zhangyu.fool.generate.util.NameConvertUtil;
import zhangyu.fool.generate.util.XmlUtil;
import zhangyu.fool.generate.writer.AbstractCodeWriter;
import zhangyu.fool.generate.writer.CodeWriter;
import zhangyu.fool.generate.writer.builder.WriterBuilderFactory;
import zhangyu.fool.generate.writer.enums.WriterEnum;
import zhangyu.fool.generate.writer.model.ProjectConfig;
import zhangyu.fool.generate.writer.model.TableSql;
import zhangyu.fool.generate.writer.model.param.CommonParam;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomingzhang
 * @date 2021/6/8
 */
public class MavenProjectWriter extends AbstractCodeWriter {

	/**
	 * 工程根目录
	 */
	private String ROOT_PATH = BuildPath.buildDir(XmlUtil.getText(ProjectEnum.PROJECT_PATH),
			XmlUtil.getText(ProjectEnum.ARTIFACT_ID));
	/**
	 * 源码目录
	 */
	private String SOURCE_CODE_PATH = BuildPath.buildDir(ROOT_PATH, "src", "main", "java");
	/**
	 * 测试代码目录
	 */
	private String TEST_CODE_PATH = BuildPath.buildDir(ROOT_PATH, "src", "test", "java");
	/**
	 * 资源目录
	 */
	private String RESOURCES_PATH = BuildPath.buildDir(ROOT_PATH, "src", "main", "resources");
	/**
	 * 基础包路径
	 */
	private String BASE_PACKAGE = BuildPath.buildDir(SOURCE_CODE_PATH,
			BuildPath.converToDir(XmlUtil.getText(ProjectEnum.GROUP_ID)),
			BuildPath.converToDir(XmlUtil.getText(ProjectEnum.ARTIFACT_ID)));
	/**
	 * 测试包路径
	 */
	private String TEST_BASE_PACKAGE = BuildPath.buildDir(TEST_CODE_PATH,
			BuildPath.converToDir(XmlUtil.getText(ProjectEnum.GROUP_ID)),
			BuildPath.converToDir(XmlUtil.getText(ProjectEnum.ARTIFACT_ID)));


	public MavenProjectWriter(ProjectConfig projectConfig) {
		super(projectConfig);
	}

	public void write() {

		// 初始化maven目录
		initMavenDir();
		// 初始化工程基本包结构
		initPackage();
		// 创建启动类
		createApplication();
		// 创建application.yml文件
		createYmlFile();
		//生成sql文件
		createSqlFile();
        // 生成代码
		generatorCode();
        // 生成测试代码
		generatorTestCode();
		
	}

	private void generatorTestCode() {
		String testDirPath = BuildPath.buildDir(TEST_BASE_PACKAGE);
		CodeWriter.build(TestWriter.class).write(testDirPath);
	}

	@Override
	public void write(String destPath, String templateName) {

	}

	@Override
	public void write(String destPath) {
		if(destPath != null) {
			this.ROOT_PATH = BuildPath.buildDir(destPath, XmlUtil.getText(ProjectEnum.ARTIFACT_ID));
			this.SOURCE_CODE_PATH = BuildPath.buildDir(ROOT_PATH, "src", "main", "java");
			this.TEST_CODE_PATH = BuildPath.buildDir(ROOT_PATH, "src", "test", "java");
			this.RESOURCES_PATH = BuildPath.buildDir(ROOT_PATH, "src", "main", "resources");
			this.BASE_PACKAGE = BuildPath.buildDir(SOURCE_CODE_PATH,
					BuildPath.converToDir(XmlUtil.getText(ProjectEnum.GROUP_ID)),
					BuildPath.converToDir(XmlUtil.getText(ProjectEnum.ARTIFACT_ID)));
			this.TEST_BASE_PACKAGE = BuildPath.buildDir(TEST_CODE_PATH,
					BuildPath.converToDir(XmlUtil.getText(ProjectEnum.GROUP_ID)),
					BuildPath.converToDir(XmlUtil.getText(ProjectEnum.ARTIFACT_ID)));
		}
		write();
	}

	@Override
	public CommonParam buildParam(String tableName, String entityName) {
		return null;
	}

	private void generatorCode() {
		// 初始化需要生成的模块数组
		WriterEnum[] writerEnums = {WriterEnum.ENTITY,WriterEnum.DTO, WriterEnum.DAO, WriterEnum.UTIL,
				WriterEnum.VO,WriterEnum.SERVICE,WriterEnum.CONTROLLER,WriterEnum.CONFIG};

		for (WriterEnum writerEnum : writerEnums) {
			String destPath = BuildPath.buildDir(BASE_PACKAGE, writerEnum.getValue());
			// 生成dao需要特殊处理，设置mapper文件路径
			if(WriterEnum.DAO.equals(writerEnum) && !projectConfig.isUseJpa()){
				DaoWriter daoWriter = new DaoWriter(projectConfig);
				daoWriter.setXmlPath(BuildPath.buildDir(RESOURCES_PATH, "mapper"));
				WriterExecutorUtil.submit(new WriterTask(daoWriter, destPath));
			}else {
				CodeWriter writer = WriterBuilderFactory.toGetBuilder(writerEnum).build(projectConfig);
				WriterExecutorUtil.submit(new WriterTask(writer, destPath));
			}
		}
	}

	/**
	 * 创建application.yml文件
	 */
	private void createYmlFile() {
		String ymlTemplatePath = BuildPath.buildDir(TEMPLATE_BASE_PATH , "config");
		String ymlTemplateName = "application";

		String destFullPath =  RESOURCES_PATH + File.separator + "application.yml";
		String driver = XmlUtil.getText("driver");
		String url = XmlUtil.getText("url");
		String username = XmlUtil.getText("username");
		String password = XmlUtil.getText("password");
		Map<String, Object> ymlParamMap = new HashMap<>();
		ymlParamMap.put("driver", driver);
		ymlParamMap.put("url", url);
		ymlParamMap.put("username", username);
		ymlParamMap.put("password", password);
		this.writeByTemplate(ymlTemplatePath, ymlTemplateName, destFullPath, ymlParamMap);
		log.info("生成application.yml配置文件");

	}

	/**
	 * 生成数据库脚本文件
	 */
	private void createSqlFile(){
		String sqlTemplatePath = BuildPath.buildDir(TEMPLATE_BASE_PATH , "resources","sql");
		String sqlTemplateName = "sql";
		String destFullPath =  RESOURCES_PATH + File.separator + "sql" + File.separator + "db.sql";

		List<String> tableNameList = DataBaseDAO.getTableNameList();
		List<TableSql> tableSqlList = new ArrayList<>(tableNameList.size());
		tableNameList.forEach(name -> tableSqlList.add(DataBaseDAO.getCreateTableSQL(name)));
		Map<String, Object> paramMap = new HashMap<>(1);
		paramMap.put("tableSqlList", tableSqlList);
		this.writeByTemplate(sqlTemplatePath, sqlTemplateName, destFullPath, paramMap);
	}

	/**
	 * 初始化maven目录 1、创建工程根目录 2、创建src/main/java源码目录 3、创建src/text/java测试目录
	 * 4、创建src/main/resource资源目录 5、创建pom.xml配置文件
	 */
	private void initMavenDir() {
		log.info("===开始初始化maven目录  begin===");

		// 创建根目录
		log.info("创建项目根目录[{}]", ROOT_PATH);
		FileUtil.mkdirs(ROOT_PATH);

		// 源代码目录
		log.info("创建项目源码目录[{}]", "src/main/java");
		FileUtil.mkdirs(SOURCE_CODE_PATH);

		// 测试代码目录
		log.info("创建项目测试代码目录[{}]", "src/test/java");
		FileUtil.mkdirs(TEST_CODE_PATH);

		// 资源目录
		log.info("创建项目资源目录[{}]", "src/main/resources");
		FileUtil.mkdirs(RESOURCES_PATH);
		FileUtil.mkdirs(RESOURCES_PATH + File.separator + "sql");

		// 生成pom.xml文件
		createPomXML();

		log.info("===maven基础目录创建完成  end===");
	}

	/**
	 * 初始化工程基本package
	 * 1、创建基础包，如smallchili.com.blog (src/main/java下)
	 * 2、创建测试基础包，如smallchili.com.blog (src/test/java下)
	 * 3、创建xml配置文件里配置的包名，如dao、service、controller等包
	 */
	private void initPackage() {
		log.info("===开始初始化包结构   begin===");
		log.info("创建基础包[{}]", String.format(XmlUtil.getText("groupId"), ".", XmlUtil.getText("artifactId")));
		// 1、创建基础包
		FileUtil.mkdirs(BASE_PACKAGE);

		// 2、创建测试包
		log.info("创建测试基础包[{}]", String.format(XmlUtil.getText("groupId"), ".", XmlUtil.getText("artifactId")));
		FileUtil.mkdirs(TEST_BASE_PACKAGE);

		// 3、创建xml里配置的包

		// 获取xml里的<package></package>标签节点
		Element packageNode = XmlUtil.getNode("package");
		if (packageNode == null) {
			log.warn("xml里没有配置<package><package/>标签");
			return;
		}
		List<Element> packageNodeList = packageNode.elements();
		if (null == packageNodeList || packageNodeList.isEmpty()) {
			log.warn("xml配置里没有配置包名");
			return;
		}

		// package标签节点下的子节点
		packageNodeList.forEach(node -> {
			String nodeName = node.getTextTrim();
			if (!nodeName.equals("")) {
				// [src/main/java]下生成包目录(dao、service、controller等)
				FileUtil.mkdirs(BASE_PACKAGE + nodeName);
				// [src/test/java]测试目录下生成对应包
				FileUtil.mkdirs(TEST_BASE_PACKAGE + nodeName);
				log.info("创建{}包", nodeName);
			}
		});

		log.info("===初始化包结构结束！ end===");
	}

	private void createPomXML() {
		String pomTemplatePath = BuildPath.buildDir(TEMPLATE_BASE_PATH, "config");
		String pomTemplateName = "pom";
        
		String destFullPath = ROOT_PATH + File.separator + "pom.xml";
		Map<String, Object> pomParamMap = new HashMap<>(8);
		String groupId = XmlUtil.getText("groupId");
		String artifactId = XmlUtil.getText("artifactId");
		pomParamMap.put("groupId", groupId);
		pomParamMap.put("artifactId", artifactId);
		pomParamMap.put("isJpa", projectConfig.isUseJpa());
		pomParamMap.put("isMyBatis",projectConfig.isUseMyBatis());
		pomParamMap.put("isMyBatisPlus",projectConfig.isUseMyBatisPlus());
		pomParamMap.put("isLombok", projectConfig.isUseLombok());
		//pomParamMap.put("isMyBatisPlus", projectConfig.isUseMyBatisPlus());
		writeByTemplate(pomTemplatePath, pomTemplateName, destFullPath, pomParamMap);

		log.info("pom.xml文件生成成功");
	}

	/**
	 * 创建启动类
	 */
	private void createApplication() {
		String appTemplatePath = TEMPLATE_BASE_PATH;
		String appTemplateName = "application";
        
		Map<String, Object> appParamMap = new HashMap<>(3);
		// TODO
		String packageName = XmlUtil.getText(ProjectEnum.GROUP_ID) + "." + XmlUtil.getText(ProjectEnum.ARTIFACT_ID);
		String className = NameConvertUtil.lineToBigHump(XmlUtil.getText(ProjectEnum.ARTIFACT_ID));
		String destFullPath = BASE_PACKAGE + File.separator + className + "Application.java";
		appParamMap.put("packageName", packageName);
		appParamMap.put("className", className);
		appParamMap.put("author", Author.build());
		writeByTemplate(appTemplatePath, appTemplateName, destFullPath, appParamMap);

		log.info("生成启动类 {}Application.java", className);
	}


	public static void main(String[] args) {
		new MavenProjectWriter(ProjectConfig.buildMyBatis()).write("C:\\Users\\admin\\Desktop\\查询语句\\");
	}

}
