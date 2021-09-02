package zhangyu.fool.generator.main.writer.java;

import zhangyu.fool.generator.main.annotation.Writer;
import zhangyu.fool.generator.main.enums.TypeSuffixEnum;
import zhangyu.fool.generator.main.enums.WriterEnum;
import zhangyu.fool.generator.main.model.ProjectConfig;

/**
 * @author xiaomingzhang
 * @date 2021/6/8
 */
@Writer(type = WriterEnum.DTO)
public class DtoWriter extends EntityWriter {

	public static final String ENTITY_TEMPLATE_PATH = TEMPLATE_BASE_PATH + "/model";

	public static final String TEMPLATE_NAME = "dto";

	public DtoWriter(ProjectConfig projectConfig) {
		super(projectConfig);
	}

	@Override
	public void write(String destPath) {
		this.write(destPath, TEMPLATE_NAME);
	}

	@Override
	public void write(String destPath, String templateName) {
		WriteConfig writeConfig = this.buildWriteConfig(destPath, templateName);
		this.forEachWrite(writeConfig);
	}

	@Override
	protected WriteConfig buildWriteConfig(String destPath, String templateName) {
		WriteConfig writeConfig = new WriteConfig();
		writeConfig.setDestPath(destPath);
		writeConfig.setTemplatePath(ENTITY_TEMPLATE_PATH);
		writeConfig.setTemplateName(templateName);
		writeConfig.setTypeSuffixEnum(TypeSuffixEnum.DTO);
		return writeConfig;
	}
}
