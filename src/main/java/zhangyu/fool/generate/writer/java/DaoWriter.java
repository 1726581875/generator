package zhangyu.fool.generate.writer.java;

import zhangyu.fool.generate.enums.ProjectEnum;
import zhangyu.fool.generate.model.Author;
import zhangyu.fool.generate.service.DatabaseService;
import zhangyu.fool.generate.util.BuildPath;
import zhangyu.fool.generate.util.NameConvertUtil;
import zhangyu.fool.generate.writer.AbstractCodeWriter;
import zhangyu.fool.generate.writer.annotation.Writer;
import zhangyu.fool.generate.writer.enums.OrmTypeEnum;
import zhangyu.fool.generate.writer.enums.TypeSuffixEnum;
import zhangyu.fool.generate.writer.enums.WriterEnum;
import zhangyu.fool.generate.writer.model.ProjectConfig;
import zhangyu.fool.generate.writer.model.param.CommonParam;
import zhangyu.fool.generate.writer.model.param.DaoParam;

/**
 * @author xiaomingzhang
 * @date 2021/6/8
 */
@Writer(type = WriterEnum.DAO)
public class DaoWriter extends AbstractCodeWriter {
	/**
	 * 模板位置
	 */
	public static final String DAO_TEMPLATE_PATH = BuildPath.buildDir(TEMPLATE_BASE_PATH, "dao");
	/**
	 * jpa模板名称
	 */
	public static final String JPA_TEMPLATE_NAME = "dao-jpa";
	/**
	 * mybatis mapper.java模板名
	 */
	public static final String MYBATIS_JAVA_TEMPLATE_NAME = "mapper-java";
	/**
	 *  mybatis mapper.xml模板名
	 */
	public static final String MYBATIS_XML_TEMPLATE_NAME = "mapper-xml";

	/**
	 * 目标xml路径，如果不设置使用默认路径defaultXmlPath
	 */
	private String xmlPath = null;
	/**
	 * xml文件默认路径，与Mapper.java文件同一目录
	 */
	private String defaultXmlPath = null;

	public DaoWriter(ProjectConfig projectConfig) {
		super(projectConfig);
	}


	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}

	@Override
	public void write(String destPath) {
		defaultXmlPath = destPath;
		if(projectConfig.isUseJpa()){
			this.writeTemplate(destPath,OrmTypeEnum.JPA.getType());
		}
		if(projectConfig.isUseMyBatis() || projectConfig.isUseMyBatisPlus()) {
			this.writeTemplate(destPath,OrmTypeEnum.MYBATIS.getType());
		}
	}



	@Override
	public void write(String destPath, String templateName) {
		checkAndCreateDir(destPath);
	}

	public void writeTemplate(String destPath, String type) {
		//构造参数
		WriteConfig writeConfig = new WriteConfig();
		writeConfig.setDestPath(destPath);
		writeConfig.setTemplateName(this.getUseTemplateName(type));
		writeConfig.setTemplatePath(DAO_TEMPLATE_PATH);
		TypeSuffixEnum typeSuffixEnum = OrmTypeEnum.JPA.getType().equals(type) ? TypeSuffixEnum.REPOSITORY : TypeSuffixEnum.MAPPER;
		writeConfig.setTypeSuffixEnum(typeSuffixEnum);
		//调用生成方法
		this.forEachWrite(writeConfig);

		//如果使用mybatis需要生成xml文件
		if(projectConfig.isUseMyBatis() || projectConfig.isUseMyBatisPlus()) {
			String realXmlPath = xmlPath == null ? defaultXmlPath : xmlPath;
			WriteConfig xmlWriteConfig = new WriteConfig();
			xmlWriteConfig.setTypeSuffixEnum(TypeSuffixEnum.MAPPER_XML);
			xmlWriteConfig.setTemplatePath(DAO_TEMPLATE_PATH);
			xmlWriteConfig.setTemplateName(MYBATIS_XML_TEMPLATE_NAME);
			xmlWriteConfig.setDestPath(realXmlPath);
			this.forEachWrite(xmlWriteConfig);
		}
	}

	private String getUseTemplateName(String type){
		String template = null;
		if(OrmTypeEnum.JPA.getType().equals(type)){
			template = JPA_TEMPLATE_NAME;
		}else {
			template = MYBATIS_JAVA_TEMPLATE_NAME;
		}
		return template;
	}

	@Override
	public CommonParam buildParam(String tableName, String entityName) {
		DaoParam daoParam = new DaoParam();
		daoParam.setAuthor(Author.build());
		daoParam.setDaoPackageName(NameConvertUtil.getPackageName(ProjectEnum.DAO_PACKAGE_NAME.getName()));
		daoParam.setEntityPackageName(NameConvertUtil.getPackageName(ProjectEnum.ENTITY_PACKAGE.getName()));
		daoParam.setEntityName(entityName);
		daoParam.setKeyType(DatabaseService.getPrimaryType(tableName));
		if(projectConfig.isUseMyBatis() || projectConfig.isUseMyBatisPlus()){
			daoParam.setTableName(tableName);
			daoParam.setEntityNameLow(NameConvertUtil.bigHumpToHump(entityName));
			daoParam.setFieldList(DatabaseService.getFieldList(tableName));
			daoParam.setIsMyBatis(projectConfig.isUseMyBatis());
			daoParam.setIsMyBatisPlus(projectConfig.isUseMyBatisPlus());
		}
		return daoParam;
	}


}
