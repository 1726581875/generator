package zhangyu.fool.generate.writer.java;

import zhangyu.fool.generate.model.Author;
import zhangyu.fool.generate.util.BuildPath;
import zhangyu.fool.generate.util.DataBaseUtil;
import zhangyu.fool.generate.util.NameConvertUtil;
import zhangyu.fool.generate.writer.AbstractCodeWriter;
import zhangyu.fool.generate.writer.annotation.Writer;
import zhangyu.fool.generate.writer.enums.TypeSuffixEnum;
import zhangyu.fool.generate.writer.model.ProjectConfig;
import zhangyu.fool.generate.writer.model.param.CommonParam;
import zhangyu.fool.generate.writer.model.param.ControllerParam;

/**
 * @author xiaomingzhang
 * @date 2021/6/8
 */
@Writer
public class ControllerWriter extends AbstractCodeWriter {
	/**
	 * 模板路径
	 */
	public static final String CONTROLLER_TEMPLATE_PATH = BuildPath.buildDir(TEMPLATE_BASE_PATH, "controller");
	/**
	 * 默认模板名
	 */
	public static final String DEFAULT_TEMPLATE_NAME = "NewController";

	public ControllerWriter() {
		super(new ProjectConfig());
	}

	public ControllerWriter(ProjectConfig projectConfig) {
		super(projectConfig);
	}

	private String controllerPkName = NameConvertUtil.getPackageName("controllerPackage");
	private String servicePkName = NameConvertUtil.getPackageName("servicePackage");
	private String entityPkName = NameConvertUtil.getPackageName("entityPackage");
	private String voPkName = NameConvertUtil.getPackageName("voPackage");


	@Override
	public CommonParam buildParam(String tableName, String entityName) {
		ControllerParam controllerParam = new ControllerParam();
		controllerParam.setAuthor(Author.build());
		controllerParam.setControllerPkName(controllerPkName);
		controllerParam.setServicePkName(servicePkName);
		controllerParam.setEntityPkName(entityPkName);
		controllerParam.setVoPkName(voPkName);
		controllerParam.setEntityName(entityName);
		controllerParam.setEntityNameLow(NameConvertUtil.bigHumpToHump(entityName));
		controllerParam.setKeyType(DataBaseUtil.getPrimaryType(tableName));
		return controllerParam;
	}


	@Override
	public void write(String destPath) {
		write(destPath,DEFAULT_TEMPLATE_NAME);
	}

	@Override
	public void write(String destPath, String templateName) {
		WriteConfig writeConfig = new WriteConfig();
		writeConfig.setDestPath(destPath);
		writeConfig.setTemplateName(templateName);
		writeConfig.setTemplatePath(CONTROLLER_TEMPLATE_PATH);
		writeConfig.setTypeSuffixEnum(TypeSuffixEnum.CONTROLLER);
		this.forEachWrite(writeConfig);
	}


	public static void main(String[] args) {
		ProjectConfig projectConfig = new ProjectConfig();
		projectConfig.setUseMyBatis(false);
		projectConfig.setUseLombok(true);
		projectConfig.setUseJpa(true);
		ControllerWriter controllerWriter = new ControllerWriter(projectConfig);
		controllerWriter.write("C:\\Users\\admin\\Desktop\\查询语句\\");
	}




}
