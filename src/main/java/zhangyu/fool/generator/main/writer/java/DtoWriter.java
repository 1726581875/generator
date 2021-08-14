package zhangyu.fool.generator.main.writer.java;

import zhangyu.fool.generator.model.Author;
import zhangyu.fool.generator.model.TableField;
import zhangyu.fool.generator.service.DatabaseService;
import zhangyu.fool.generator.util.NameConvertUtil;
import zhangyu.fool.generator.main.writer.AbstractCodeWriter;
import zhangyu.fool.generator.main.annotation.Writer;
import zhangyu.fool.generator.main.enums.TypeSuffixEnum;
import zhangyu.fool.generator.main.enums.WriterEnum;
import zhangyu.fool.generator.main.model.ProjectConfig;
import zhangyu.fool.generator.main.model.param.CommonParam;
import zhangyu.fool.generator.main.model.param.EntityParam;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiaomingzhang
 * @date 2021/6/8
 */
@Writer(type = WriterEnum.DTO)
public class DtoWriter extends AbstractCodeWriter {

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
	public CommonParam buildParam(String tableName, String entityName) {
		EntityParam entityParam = new EntityParam();
		List<TableField> fieldList = DatabaseService.getFieldList(tableName);
		Set<String> javaTypeSet = fieldList.stream().map(TableField::getJavaType).collect(Collectors.toSet());
		entityParam.setFieldList(fieldList);
		entityParam.setJavaTypeSet(javaTypeSet);
		entityParam.setEntityName(entityName);
		this.buildBaseParam(entityParam);
		return entityParam;
	}

	@Override
	public void write(String destPath, String templateName) {
		WriteConfig writeConfig = new WriteConfig();
		writeConfig.setDestPath(destPath);
		writeConfig.setTemplatePath(ENTITY_TEMPLATE_PATH);
		writeConfig.setTemplateName(templateName);
		writeConfig.setTypeSuffixEnum(TypeSuffixEnum.DTO);
		this.forEachWrite(writeConfig);
	}
	
}
