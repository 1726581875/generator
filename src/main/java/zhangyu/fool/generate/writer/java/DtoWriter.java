package zhangyu.fool.generate.writer.java;

import zhangyu.fool.generate.enums.ProjectEnum;
import zhangyu.fool.generate.model.Author;
import zhangyu.fool.generate.model.TableField;
import zhangyu.fool.generate.service.DatabaseService;
import zhangyu.fool.generate.util.DataBaseUtil;
import zhangyu.fool.generate.util.NameConvertUtil;
import zhangyu.fool.generate.writer.AbstractCodeWriter;
import zhangyu.fool.generate.writer.annotation.Writer;
import zhangyu.fool.generate.writer.enums.TypeSuffixEnum;
import zhangyu.fool.generate.writer.enums.WriterEnum;
import zhangyu.fool.generate.writer.model.ProjectConfig;
import zhangyu.fool.generate.writer.model.param.CommonParam;
import zhangyu.fool.generate.writer.model.param.EntityParam;

import java.util.List;
import java.util.Set;

/**
 * @author xiaomingzhang
 * @date 2021/6/8
 */
@Writer(type = WriterEnum.DTO)
public class DtoWriter extends AbstractCodeWriter {

	public static final String ENTITY_TEMPLATE_PATH = TEMPLATE_BASE_PATH + "model\\";

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
		Set<String> javaTypeSet = DataBaseUtil.getJavaTypes(fieldList);
		String packageName = NameConvertUtil.getPackageName(ProjectEnum.ENTITY_PACKAGE.getName());
		entityParam.setFieldList(fieldList);
		entityParam.setJavaTypeSet(javaTypeSet);
		entityParam.setEntityName(entityName);
		entityParam.setAuthor(Author.build());
		entityParam.setPackageName(packageName);
		entityParam.setIsLombok(projectConfig.isUseLombok());
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
