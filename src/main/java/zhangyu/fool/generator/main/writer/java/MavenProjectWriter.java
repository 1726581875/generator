package zhangyu.fool.generator.main.writer.java;

import zhangyu.fool.generator.enums.ProjectEnum;
import zhangyu.fool.generator.main.proxy.WriterProxyFactory;
import zhangyu.fool.generator.main.writer.doc.SqlDocxWriter;
import zhangyu.fool.generator.main.writer.doc.SqlScriptWriter;
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

/**
 * @author xiaomingzhang
 * @date 2021/6/8
 */
public class MavenProjectWriter extends AbstractCodeWriter {


	private ProjectPath projectPath = new ProjectPath();

	public MavenProjectWriter(ProjectConfig projectConfig) {
		super(projectConfig);
	}

	public void write() {

		// 初始化maven目录
		initMavenProject();
		// 初始化工程基本包结构
		initPackage();
		// 创建启动类
		generateApplication();
		// 创建application.yml文件
		generateYmlFile();
		//生成sql脚本文件、数据库说明文档
		generateSqlFile();
        // 生成代码
		generateCode();
        // 生成测试代码
		generateTestCode();
	}

	private void generateTestCode() {
		String testDirPath = BuildPath.buildDir(projectPath.getBaseTestPackagePath());
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
		this.projectPath = new ProjectPath(destPath);
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

	private void generateCode() {
		// 初始化需要生成的模块数组
		WriterEnum[] writerEnums = {WriterEnum.ENTITY,WriterEnum.DTO, WriterEnum.DAO, WriterEnum.UTIL,
				WriterEnum.VO,WriterEnum.SERVICE,WriterEnum.CONTROLLER,WriterEnum.CONFIG};

		for (WriterEnum writerEnum : writerEnums) {
			String destPath = BuildPath.buildDir(projectPath.getBasePackagePath(), writerEnum.getValue());
			FoolWriter writer = WriterBuilderFactory.toGetBuilder(writerEnum).build(projectConfig);
			// 生成dao需要特殊处理，设置mapper文件路径
			if(WriterEnum.DAO.equals(writerEnum) && !projectConfig.isUseJpa()) {
				DaoWriter daoWriter = (DaoWriter) writer;
				daoWriter.setXmlPath(BuildPath.buildDir(projectPath.getResourcePath() , "mapper"));
			}
			WriterExecutorUtil.submit(new WriterTask(writer, destPath));
		}
	}

	/**
	 * 创建application.yml文件
	 */
	private void generateYmlFile() {
		String ymlTemplatePath = BuildPath.buildDir(TEMPLATE_BASE_PATH , "config");
		String ymlTemplateName = "application";

		String destFullPath =  projectPath.getResourcePath() + File.separator + "application.yml";
		this.writeByParam(ymlTemplatePath, ymlTemplateName, destFullPath, this.buildParam(null,null));
		log.info("生成application.yml配置文件");
	}

	/**
	 * 生成数据库sql脚本文件
	 */
	private void generateSqlFile() {
		//脚本文件
		SqlScriptWriter sqlScriptWriter = new SqlScriptWriter();
		FoolWriter writerProxy = WriterProxyFactory.getWriterProxy(sqlScriptWriter);
		writerProxy.write(projectPath.getResourcePath() + "/test/sql");
		//数据库说明文档
		SqlDocxWriter sqlDocxWriter = new SqlDocxWriter();
		WriterProxyFactory.getWriterProxy2(sqlDocxWriter).write(projectPath.getResourcePath() + "/doc");
	}

	/**
	 * 初始化maven目录 1、创建工程根目录 2、创建src/main/java源码目录 3、创建src/text/java测试目录
	 * 4、创建src/main/resource资源目录 5、创建pom.xml配置文件
	 */
	private void initMavenProject() {
		log.info("创建项目根目录[{}]", projectPath.getRootPath());
		FileUtil.mkdirs(projectPath.getRootPath());

		log.info("创建项目源码目录[{}]", "src/main/java");
		FileUtil.mkdirs(projectPath.getSourceCodePath());

		log.info("创建项目测试代码目录[{}]", "src/test/java");
		FileUtil.mkdirs(projectPath.getTestCodePath());

		log.info("创建项目资源目录[{}]", "src/main/resources");
		FileUtil.mkdirs(projectPath.getResourcePath());

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
		FileUtil.mkdirs(projectPath.getBasePackagePath());
		log.info("创建测试基础包");
		FileUtil.mkdirs(projectPath.getTestCodePath());
	}

	private void createPomXML() {
		String pomTemplatePath = BuildPath.buildDir(TEMPLATE_BASE_PATH, "config");
		String pomTemplateName = "pom";
		String destFullPath = projectPath.getRootPath() + File.separator + "pom.xml";
		this.writeByParam(pomTemplatePath, pomTemplateName, destFullPath, this.buildParam(null,null));
		log.info("pom.xml文件生成成功");
	}

	/**
	 * 创建启动类
	 */
	private void generateApplication() {
		String appTemplatePath = TEMPLATE_BASE_PATH;
		String appTemplateName = "application";
		String className = NameConvertUtil.lineToBigHump(XmlUtil.getText(ProjectEnum.ARTIFACT_ID));
		String destFullPath = projectPath.getBasePackagePath() + File.separator + className + "Application.java";
		CommonParam commonParam = this.buildParam(null, className);
		this.writeByParam(appTemplatePath, appTemplateName, destFullPath, commonParam);
		log.info("生成启动类 {}Application.java", className);
	}


	public static class ProjectPath {
		/**
		 * 工程根目录
		 */
		private String rootPath = XmlUtil.getText(ProjectEnum.PROJECT_PATH) + XmlUtil.getText(ProjectEnum.ARTIFACT_ID);

		public ProjectPath(){}

		public ProjectPath(String basePath){
			String artifactId = XmlUtil.getText(ProjectEnum.ARTIFACT_ID);
			rootPath = BuildPath.buildDir(basePath ,artifactId);
		}


		public String getRootPath(){
			return rootPath;
		}

		public String getSourceCodePath(){
			return this.rootPath + "/src/main/java";
		}

		public String getResourcePath(){
			return this.rootPath + "/src/main/resources";
		}

		public String getTestCodePath(){
			return this.rootPath + "/src/test/java";
		}

		/**
		 * maven源码目录 + 包路径
		 * /src/main/java/ + groupId + artifactId
		 * @return
		 */
		public String getBasePackagePath(){
			String path = BuildPath.convertToDir(getBasePackageName());
			return getSourceCodePath() + path;
		}

		/**
		 * maven测试目录 + 包路径
		 * /src/test/java + groupId + artifactId
		 * @return
		 */
		public String getBaseTestPackagePath(){
			String path = BuildPath.convertToDir(getBasePackageName());
			return getTestCodePath() + path;
		}

		public static String getBasePackageName(){
			String groupId = XmlUtil.getText(ProjectEnum.GROUP_ID);
			String artifactId = XmlUtil.getText(ProjectEnum.ARTIFACT_ID);
			return groupId + "." + artifactId;
		}


	}


}
