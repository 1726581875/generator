package zhangyu.fool.generator.main.writer.java;

import zhangyu.fool.generator.model.Author;
import zhangyu.fool.generator.service.DatabaseService;
import zhangyu.fool.generator.util.NameConvertUtil;
import zhangyu.fool.generator.main.writer.AbstractCodeWriter;
import zhangyu.fool.generator.main.annotation.Writer;
import zhangyu.fool.generator.main.enums.TypeSuffixEnum;
import zhangyu.fool.generator.main.enums.WriterEnum;
import zhangyu.fool.generator.main.model.ProjectConfig;
import zhangyu.fool.generator.main.model.param.CommonParam;
import zhangyu.fool.generator.main.model.param.ControllerParam;

/**
 * @author xiaomingzhang
 * @date 2021/6/8
 */
@Writer(type = WriterEnum.CONTROLLER)
public class ControllerWriter extends AbstractCodeWriter {
	/**
	 * 模板路径
	 */
	public static final String CONTROLLER_TEMPLATE_PATH = TEMPLATE_BASE_PATH + "/controller";
	/**
	 * 默认模板名
	 */
	public static final String DEFAULT_TEMPLATE_NAME = "Controller";

	public ControllerWriter() {
		super(new ProjectConfig());
	}

	public ControllerWriter(ProjectConfig projectConfig) {
		super(projectConfig);
	}

	@Override
	public CommonParam buildParam(String tableName, String entityName) {
		ControllerParam controllerParam = new ControllerParam();
		controllerParam.setEntityName(entityName);
		controllerParam.setEntityNameLow(NameConvertUtil.bigHumpToHump(entityName));
		controllerParam.setKeyType(DatabaseService.getPrimaryType(tableName));
		this.buildBaseParam(controllerParam);
		return controllerParam;
	}

	@Override
	public void write(String destPath) {
		write(destPath,DEFAULT_TEMPLATE_NAME);
	}

	@Override
	public void write(String destPath, String templateName) {
		WriteConfig writeConfig = buildWriteConfig(destPath, templateName);
		this.forEachWrite(writeConfig);
	}

	@Override
	protected WriteConfig buildWriteConfig(String destPath, String templateName) {
		WriteConfig writeConfig = new WriteConfig();
		writeConfig.setDestPath(destPath);
		writeConfig.setTemplateName(templateName);
		writeConfig.setTemplatePath(CONTROLLER_TEMPLATE_PATH);
		writeConfig.setTypeSuffixEnum(TypeSuffixEnum.CONTROLLER);
		return writeConfig;
	}
}
