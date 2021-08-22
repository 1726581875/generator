package zhangyu.fool.generator.main.writer.java;

import zhangyu.fool.generator.enums.ProjectEnum;
import zhangyu.fool.generator.model.TableField;
import zhangyu.fool.generator.service.DatabaseService;
import zhangyu.fool.generator.util.NameConvertUtil;
import zhangyu.fool.generator.main.writer.AbstractCodeWriter;
import zhangyu.fool.generator.main.writer.FoolWriter;
import zhangyu.fool.generator.main.enums.TypeSuffixEnum;
import zhangyu.fool.generator.main.enums.WriterEnum;
import zhangyu.fool.generator.main.model.ProjectConfig;
import zhangyu.fool.generator.main.model.param.CommonParam;
import zhangyu.fool.generator.main.model.param.EntityParam;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 生成实体类的工厂
 * @author xmz
 * 2020/9/18
 */
@zhangyu.fool.generator.main.annotation.Writer(type = WriterEnum.ENTITY)
public class EntityWriter extends AbstractCodeWriter {
    /**
     * 模板路径
     */
    public static final String ENTITY_TEMPLATE_PATH = TEMPLATE_BASE_PATH + "/model/entity";
    /**
     * 模板名
     */
    public static final String TEMPLATE_NAME = "entity";

    public EntityWriter(ProjectConfig projectConfig){
        super(projectConfig);
    }


    @Override
    public void write(String destPath) {
        this.write(destPath, TEMPLATE_NAME);
    }

    @Override
    public void write(String destPath, String templateName) {
        WriteConfig writeConfig = buildWriteConfig(destPath, templateName);
        this.forEachWrite(writeConfig);
    }
     @Override
    protected WriteConfig buildWriteConfig(String destPath, String templateName){
        WriteConfig writeConfig = new WriteConfig();
        writeConfig.setDestPath(destPath);
        writeConfig.setTemplatePath(ENTITY_TEMPLATE_PATH);
        writeConfig.setTemplateName(templateName);
        writeConfig.setTypeSuffixEnum(TypeSuffixEnum.ENTITY);
        return writeConfig;
    }


    @Override
    public CommonParam buildParam(String tableName, String entityName) {
        EntityParam entityParam = new EntityParam();
        List<TableField> fieldList = DatabaseService.getFieldList(tableName);
        Set<String> javaTypeSet = fieldList.stream().map(TableField::getJavaType).collect(Collectors.toSet());
        entityParam.setFieldList(fieldList);
        entityParam.setJavaTypeSet(javaTypeSet);
        entityParam.setEntityName(entityName);
        entityParam.setTableName(tableName);
        //设置工程基本配置
        this.buildBaseParam(entityParam);
        return entityParam;
    }

}
