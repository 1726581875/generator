package zhangyu.fool.generate.writer.java;

import zhangyu.fool.generate.enums.ProjectEnum;
import zhangyu.fool.generate.model.Field;
import zhangyu.fool.generate.util.BuildPath;
import zhangyu.fool.generate.util.DataBaseUtil;
import zhangyu.fool.generate.util.NameConvertUtil;
import zhangyu.fool.generate.writer.AbstractCodeWriter;
import zhangyu.fool.generate.writer.CodeWriter;
import zhangyu.fool.generate.writer.enums.TypeSuffixEnum;
import zhangyu.fool.generate.writer.model.ProjectConfig;
import zhangyu.fool.generate.writer.model.param.CommonParam;
import zhangyu.fool.generate.writer.model.param.EntityParam;

import java.util.List;
import java.util.Set;

/**
 * 生成实体类的工厂
 * @author xmz
 * 2020/9/18
 */
public class EntityWriter extends AbstractCodeWriter {
    /**
     * 模板路径
     */
    public static final String ENTITY_TEMPLATE_PATH = BuildPath.buildDir(TEMPLATE_BASE_PATH, "model");
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
        WriteConfig writeConfig = new WriteConfig();
        writeConfig.setDestPath(destPath);
        writeConfig.setTemplatePath(ENTITY_TEMPLATE_PATH);
        writeConfig.setTemplateName(templateName);
        writeConfig.setTypeSuffixEnum(TypeSuffixEnum.ENTITY);
        this.forEachWrite(writeConfig);
    }

    @Override
    public CommonParam buildParam(String tableName, String entityName) {
        EntityParam entityParam = new EntityParam();
        List<Field> fieldList = DataBaseUtil.getColumnByTableName(tableName);
        Set<String> javaTypeSet = DataBaseUtil.getJavaTypes(fieldList);
        String packageName = NameConvertUtil.getPackageName(ProjectEnum.ENTITY_PACKAGE.getElementName());
        entityParam.setFieldList(fieldList);
        entityParam.setJavaTypeSet(javaTypeSet);
        entityParam.setEntityName(entityName);
        entityParam.setTableName(tableName);
        entityParam.setPackageName(packageName);
        //设置工程基本配置
        this.buildBaseParam(entityParam, projectConfig);
        return entityParam;
    }

    public static void main(String[] args) {
        CodeWriter factory = new EntityWriter(ProjectConfig.buildJpa());
        factory.write("C:\\Users\\admin\\Desktop\\查询语句\\");
    }

}
