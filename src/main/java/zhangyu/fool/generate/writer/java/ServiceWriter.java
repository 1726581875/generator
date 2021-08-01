package zhangyu.fool.generate.writer.java;


import zhangyu.fool.generate.model.Author;
import zhangyu.fool.generate.util.BuildPath;
import zhangyu.fool.generate.util.DataBaseUtil;
import zhangyu.fool.generate.util.NameConvertUtil;
import zhangyu.fool.generate.writer.AbstractCodeWriter;
import zhangyu.fool.generate.writer.enums.TypeSuffixEnum;
import zhangyu.fool.generate.writer.model.ProjectConfig;
import zhangyu.fool.generate.writer.model.param.CommonParam;
import zhangyu.fool.generate.writer.model.param.ServiceParam;

import java.io.File;

/**
 * @author xiaomingzhang
 * @date 2021/6/8
 */
public class ServiceWriter extends AbstractCodeWriter {

	public static final String SERVICE_TEMPLATE_PATH = BuildPath.buildDir(TEMPLATE_BASE_PATH, "service");

	public static final String SERVICE_TEMPLATE_NAME = "Service";

	public static final String DEFAULT_TEMPLATE_NAME = "ServiceImpl";
	/**
	 * mybatis版本service模板名
	 */
	public static final String MYBATIS_TEMPLATE_NAME = "MybatisServiceImpl";

	public ServiceWriter(ProjectConfig projectConfig) {
		super(projectConfig);
	}

	String servicePkName = NameConvertUtil.getPackageName("servicePackage");
	String entityPkName = NameConvertUtil.getPackageName("entityPackage");
	String daoPkName = NameConvertUtil.getPackageName("daoPackage");
	String dtoPkName = NameConvertUtil.getPackageName("dtoPackage");
	String voPkName = NameConvertUtil.getPackageName("voPackage");
	String utilPkName = NameConvertUtil.getPackageName("utilPackage");

	@Override
	public CommonParam buildParam(String tableName, String entityName) {
		ServiceParam serviceParam = new ServiceParam();
		serviceParam.setServicePkName(servicePkName);
		serviceParam.setEntityPkName(entityPkName);
		serviceParam.setDaoPkName(daoPkName);
		serviceParam.setDtoPkName(dtoPkName);
		serviceParam.setVoPkName(voPkName);
		serviceParam.setUtilPkName(utilPkName);
		serviceParam.setAuthor(Author.build());
		serviceParam.setEntityKey(DataBaseUtil.getPrimaryName(tableName));
		serviceParam.setEntityName(entityName);
		serviceParam.setEntityNameLow(NameConvertUtil.bigHumpToHump(entityName));
		serviceParam.setKeyType(DataBaseUtil.getPrimaryType(tableName));
		serviceParam.setIsJpa(projectConfig.isUseJpa());
		serviceParam.setIsMyBatis(projectConfig.isUseMyBatis());
		serviceParam.setIsMyBatisPlus(projectConfig.isUseMyBatisPlus());
		return serviceParam;
	}


	@Override
	public void write(String destPath) {
		write(destPath, DEFAULT_TEMPLATE_NAME);
/*		if(projectConfig.isUseJpa()) {
			write(destPath, DEFAULT_TEMPLATE_NAME);
		}else if(projectConfig.isUseMyBatisPlus()){
			write(destPath, DEFAULT_TEMPLATE_NAME);
		}else {
			write(destPath, MYBATIS_TEMPLATE_NAME);
		}*/
	}

	@Override
	public void write(String destPath, String templateName) {
		WriteConfig writeConfig = new WriteConfig();
		writeConfig.setDestPath(destPath);
		writeConfig.setTemplatePath(SERVICE_TEMPLATE_PATH);
		//生成service接口类
		writeConfig.setTemplateName(SERVICE_TEMPLATE_NAME);
		writeConfig.setTypeSuffixEnum(TypeSuffixEnum.SERVICE);
		this.forEachWrite(writeConfig);
		//生成service实现类
		writeConfig.setTypeSuffixEnum(TypeSuffixEnum.SERVICE_IMPL);
		writeConfig.setDestPath(destPath + File.separator + "impl");
		writeConfig.setTemplateName(templateName);
		this.forEachWrite(writeConfig);

	}

	public static void main(String[] args) {
		ProjectConfig projectConfig = ProjectConfig.buildMyBatis();
		ServiceWriter serviceWriter = new ServiceWriter(projectConfig);
		serviceWriter.write("C:\\Users\\admin\\Desktop\\查询语句\\");
	}
	

}
