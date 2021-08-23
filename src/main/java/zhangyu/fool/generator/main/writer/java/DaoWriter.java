package zhangyu.fool.generator.main.writer.java;

import zhangyu.fool.generator.main.annotation.Writer;
import zhangyu.fool.generator.main.enums.OrmTypeEnum;
import zhangyu.fool.generator.main.enums.TypeSuffixEnum;
import zhangyu.fool.generator.main.enums.WriterEnum;
import zhangyu.fool.generator.main.model.ProjectConfig;
import zhangyu.fool.generator.main.model.param.CommonParam;
import zhangyu.fool.generator.main.model.param.DaoParam;
import zhangyu.fool.generator.main.writer.AbstractCodeWriter;
import zhangyu.fool.generator.service.DatabaseService;
import zhangyu.fool.generator.util.FileUtil;
import zhangyu.fool.generator.util.NameConvertUtil;

/**
 * @author xiaomingzhang
 * @date 2021/6/8
 */
@Writer(type = WriterEnum.DAO)
public class DaoWriter extends AbstractCodeWriter {
	/**
	 * 模板位置
	 */
	public static final String DAO_TEMPLATE_PATH =TEMPLATE_BASE_PATH + "/dao";
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
		if(projectConfig.isUseMyBatis()) {
			this.writeTemplate(destPath,OrmTypeEnum.MYBATIS.getType());
		}
		if(projectConfig.isUseMyBatisPlus()){
			this.writeTemplate(destPath,OrmTypeEnum.MYBATIS_PLUS.getType());
		}
	}



	@Override
	public void write(String destPath, String templateName) {
		FileUtil.checkAndCreateDir(destPath);
	}

	public void writeTemplate(String destPath, String type) {

		WriteConfig writeConfig = this.buildWriteDaoConfig(destPath, type);
		this.forEachWrite(writeConfig);

		if(isUseMybatis()) {
			WriteConfig xmlWriteConfig = buildWriteXmlConfig();
			this.forEachWrite(xmlWriteConfig);
		}
	}

	private boolean isUseMybatis(){
		return (projectConfig.isUseMyBatis() || projectConfig.isUseMyBatisPlus());
	}

	private WriteConfig buildWriteDaoConfig(String destPath, String type){
		WriteConfig writeConfig = new WriteConfig();
		writeConfig.setDestPath(destPath);
		writeConfig.setTemplateName(this.getUseTemplateName(type));
		writeConfig.setTemplatePath(DAO_TEMPLATE_PATH);
		TypeSuffixEnum typeSuffixEnum = OrmTypeEnum.JPA.getType().equals(type) ? TypeSuffixEnum.REPOSITORY : TypeSuffixEnum.MAPPER;
		writeConfig.setTypeSuffixEnum(typeSuffixEnum);
		return writeConfig;
	}

	private WriteConfig buildWriteXmlConfig(){
		String realXmlPath = xmlPath == null ? defaultXmlPath : xmlPath;
		WriteConfig xmlWriteConfig = new WriteConfig();
		xmlWriteConfig.setTypeSuffixEnum(TypeSuffixEnum.MAPPER_XML);
		xmlWriteConfig.setTemplatePath(DAO_TEMPLATE_PATH);
		xmlWriteConfig.setTemplateName(MYBATIS_XML_TEMPLATE_NAME);
		xmlWriteConfig.setDestPath(realXmlPath);
		return xmlWriteConfig;
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
		this.buildBaseParam(daoParam);
		daoParam.setEntityName(entityName);
		daoParam.setKeyType(DatabaseService.getPrimaryType(tableName));
		if(projectConfig.isUseMyBatis() || projectConfig.isUseMyBatisPlus()){
			daoParam.setTableName(tableName);
			daoParam.setEntityNameLow(NameConvertUtil.bigHumpToHump(entityName));
			daoParam.setFieldList(DatabaseService.getFieldList(tableName));
		}
		return daoParam;
	}


}
