package zhangyu.fool.generator.main.writer.java;

import org.dom4j.Element;
import zhangyu.fool.generator.enums.ProjectEnum;
import zhangyu.fool.generator.main.proxy.WriterProxyFactory;
import zhangyu.fool.generator.main.writer.doc.SqlDocxWriter;
import zhangyu.fool.generator.main.writer.doc.SqlScriptWriter;
import zhangyu.fool.generator.model.Author;
import zhangyu.fool.generator.main.thread.WriterExecutorUtil;
import zhangyu.fool.generator.main.thread.WriterTask;
import zhangyu.fool.generator.util.BuildPath;
import zhangyu.fool.generator.util.FileUtil;
import zhangyu.fool.generator.util.NameConvertUtil;
import zhangyu.fool.generator.util.XmlUtil;
import zhangyu.fool.generator.main.writer.AbstractCodeWriter;
import zhangyu.fool.generator.main.writer.FoolWriter;
import zhangyu.fool.generator.main.builder.WriterBuilderFactory;
import zhangyu.fool.generator.main.enums.WriterEnum;
import zhangyu.fool.generator.main.model.ProjectConfig;
import zhangyu.fool.generator.main.model.param.CommonParam;
import zhangyu.fool.generator.main.model.param.MavenProjectParam;

import java.io.File;
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
			BuildPath.convertToDir(XmlUtil.getText(ProjectEnum.GROUP_ID)),
			BuildPath.convertToDir(XmlUtil.getText(ProjectEnum.ARTIFACT_ID)));
	/**
	 * 测试包路径
	 */
	private String TEST_BASE_PACKAGE = BuildPath.buildDir(TEST_CODE_PATH,
			BuildPath.convertToDir(XmlUtil.getText(ProjectEnum.GROUP_ID)),
			BuildPath.convertToDir(XmlUtil.getText(ProjectEnum.ARTIFACT_ID)));


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
		//生成sql脚本文件、数据库说明文档
		createSqlFile();
        // 生成代码
		generatorCode();
        // 生成测试代码
		generatorTestCode();

	}

	private void generatorTestCode() {
		String testDirPath = BuildPath.buildDir(TEST_BASE_PACKAGE);
		WriterBuilderFactory.toGetBuilder(TestWriter.class).build(projectConfig).write(testDirPath);
	}

	@Override
	public void write(String destPath) {
		if(destPath != null) {
			this.buildBasePath(destPath);
		}
		write();
	}

	private void buildBasePath(String destPath){
		this.ROOT_PATH = BuildPath.buildDir(destPath, XmlUtil.getText(ProjectEnum.ARTIFACT_ID));
		this.SOURCE_CODE_PATH = BuildPath.buildDir(ROOT_PATH, "src", "main", "java");
		this.TEST_CODE_PATH = BuildPath.buildDir(ROOT_PATH, "src", "test", "java");
		this.RESOURCES_PATH = BuildPath.buildDir(ROOT_PATH, "src", "main", "resources");
		this.BASE_PACKAGE = BuildPath.buildDir(SOURCE_CODE_PATH,
				BuildPath.convertToDir(XmlUtil.getText(ProjectEnum.GROUP_ID)),
				BuildPath.convertToDir(XmlUtil.getText(ProjectEnum.ARTIFACT_ID)));
		this.TEST_BASE_PACKAGE = BuildPath.buildDir(TEST_CODE_PATH,
				BuildPath.convertToDir(XmlUtil.getText(ProjectEnum.GROUP_ID)),
				BuildPath.convertToDir(XmlUtil.getText(ProjectEnum.ARTIFACT_ID)));
	}


	@Override
	public CommonParam buildParam(String tableName, String entityName) {
		MavenProjectParam projectParam = new MavenProjectParam();
		//pom.xml
		this.buildBaseParam(projectParam);
		projectParam.setIsLombok(projectParam.getIsLombok());
		projectParam.setGroupId(XmlUtil.getText(ProjectEnum.GROUP_ID.getName()));
		projectParam.setArtifactId(XmlUtil.getText(ProjectEnum.ARTIFACT_ID.getName()));
		//application.yml
		projectParam.setDriver(XmlUtil.getText(ProjectEnum.DRIVER));
		projectParam.setUrl(XmlUtil.getText(ProjectEnum.URL));
		projectParam.setUsername(XmlUtil.getText(ProjectEnum.USERNAME));
		projectParam.setPassword(XmlUtil.getText(ProjectEnum.PASSWORD));
		//启动类参数
		projectParam.setEntityName(entityName);
		return projectParam;
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
				FoolWriter writer = WriterBuilderFactory.toGetBuilder(writerEnum).build(projectConfig);
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
		this.writeByParam(ymlTemplatePath, ymlTemplateName, destFullPath, this.buildParam(null,null));
		log.info("生成application.yml配置文件");

	}

	/**
	 * 生成数据库sql脚本文件
	 */
	private void createSqlFile() {
		//脚本文件
		SqlScriptWriter sqlScriptWriter = new SqlScriptWriter();
		FoolWriter writerProxy = WriterProxyFactory.getWriterProxy(sqlScriptWriter);
		writerProxy.write(RESOURCES_PATH + "/test/sql");
		//数据库说明文档
		SqlDocxWriter sqlDocxWriter = new SqlDocxWriter();
		WriterProxyFactory.getWriterProxy2(sqlDocxWriter).write(RESOURCES_PATH + "/doc");
	}

	/**
	 * 初始化maven目录 1、创建工程根目录 2、创建src/main/java源码目录 3、创建src/text/java测试目录
	 * 4、创建src/main/resource资源目录 5、创建pom.xml配置文件
	 */
	private void initMavenDir() {
		log.info("创建项目根目录[{}]", ROOT_PATH);
		FileUtil.mkdirs(ROOT_PATH);

		log.info("创建项目源码目录[{}]", "src/main/java");
		FileUtil.mkdirs(SOURCE_CODE_PATH);

		log.info("创建项目测试代码目录[{}]", "src/test/java");
		FileUtil.mkdirs(TEST_CODE_PATH);

		log.info("创建项目资源目录[{}]", "src/main/resources");
		FileUtil.mkdirs(RESOURCES_PATH);
		FileUtil.mkdirs(RESOURCES_PATH + "/test/sql");

		// 生成pom.xml文件
		createPomXML();
	}

	/**
	 * 初始化工程基本package
	 * 1、创建基础包，如smallchili.com.blog (src/main/java下)
	 * 2、创建测试基础包，如smallchili.com.blog (src/test/java下)
	 */
	private void initPackage() {
		log.debug("创建基础目录");
		FileUtil.mkdirs(BASE_PACKAGE);
		log.info("创建测试基础包");
		FileUtil.mkdirs(TEST_BASE_PACKAGE);
	}

	private void createPomXML() {
		String pomTemplatePath = BuildPath.buildDir(TEMPLATE_BASE_PATH, "config");
		String pomTemplateName = "pom";
		String destFullPath = ROOT_PATH + File.separator + "pom.xml";
		this.writeByParam(pomTemplatePath, pomTemplateName, destFullPath, this.buildParam(null,null));
		log.info("pom.xml文件生成成功");
	}

	/**
	 * 创建启动类
	 */
	private void createApplication() {
		String appTemplatePath = TEMPLATE_BASE_PATH;
		String appTemplateName = "application";
		String className = NameConvertUtil.lineToBigHump(XmlUtil.getText(ProjectEnum.ARTIFACT_ID));
		String destFullPath = BASE_PACKAGE + File.separator + className + "Application.java";
		CommonParam commonParam = this.buildParam(null, className);
		this.writeByParam(appTemplatePath, appTemplateName, destFullPath, commonParam);
		log.info("生成启动类 {}Application.java", className);
	}


}
