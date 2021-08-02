package zhangyu.fool.generate.writer.vue;

import zhangyu.fool.generate.util.BuildPath;
import zhangyu.fool.generate.util.DataBaseUtil;
import zhangyu.fool.generate.util.NameConvertUtil;
import zhangyu.fool.generate.writer.AbstractCodeWriter;
import zhangyu.fool.generate.writer.enums.TypeSuffixEnum;
import zhangyu.fool.generate.writer.builder.WriterBuilderFactory;
import zhangyu.fool.generate.writer.model.ProjectConfig;
import zhangyu.fool.generate.writer.model.param.CommonParam;
import zhangyu.fool.generate.writer.model.param.EntityParam;

/**
 * @author xmz
 * @date: 2020/10/11
 */
public class ViewWriter extends AbstractCodeWriter {

    protected final String WEB_TEMPLATE_PATH =  BuildPath.buildDir(TEMPLATE_BASE_PATH,"web");

    protected final String VIEW_TEMPLATE_PATH = BuildPath.buildDir(WEB_TEMPLATE_PATH,"views");
    /**
     * 默认模板名称
     */
    private final String DEFAULT_TEMPLATE_NAME = "page";

    public ViewWriter() {
        super(new ProjectConfig());
    }

    public ViewWriter(ProjectConfig projectConfig) {
        super(projectConfig);
    }


    @Override
    public void write(String destPath) {
        write(destPath, DEFAULT_TEMPLATE_NAME);
    }

    @Override
    public CommonParam buildParam(String tableName, String entityName) {
        EntityParam entityParam = new EntityParam();
        entityParam.setFieldList(DataBaseUtil.getColumnByTableName(tableName));
        entityParam.setEntityName(entityName);
        entityParam.setEntityNameLow(NameConvertUtil.bigHumpToHump(entityName));
        return entityParam;
    }

    @Override
    public void write(String destPath, String templateName) {

        WriteConfig writeConfig = new WriteConfig();
        writeConfig.setDestPath(destPath);
        writeConfig.setTemplateName(templateName);
        writeConfig.setTemplatePath(VIEW_TEMPLATE_PATH);
        writeConfig.setTypeSuffixEnum(TypeSuffixEnum.VIEW);
        this.forEachWrite(writeConfig);
    }


    public static void main(String[] args) {
        WriterBuilderFactory.toGetBuilder(ViewWriter.class).build(new ProjectConfig())
                .write("C:\\Users\\admin\\Desktop\\查询语句\\");
    }



}
