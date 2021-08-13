package zhangyu.fool.generator.main.writer.vue;

import zhangyu.fool.generator.service.DatabaseServiceImpl;
import zhangyu.fool.generator.util.NameConvertUtil;
import zhangyu.fool.generator.main.writer.AbstractCodeWriter;
import zhangyu.fool.generator.main.enums.TypeSuffixEnum;
import zhangyu.fool.generator.main.builder.WriterBuilderFactory;
import zhangyu.fool.generator.main.model.ProjectConfig;
import zhangyu.fool.generator.main.model.param.CommonParam;
import zhangyu.fool.generator.main.model.param.EntityParam;

/**
 * @author xmz
 * @date: 2020/10/11
 */
public class ViewWriter extends AbstractCodeWriter {

    protected final String WEB_TEMPLATE_PATH =  TEMPLATE_BASE_PATH + "/web";

    protected final String VIEW_TEMPLATE_PATH = WEB_TEMPLATE_PATH + "/views";
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
        entityParam.setFieldList(DatabaseServiceImpl.getFieldList(tableName));
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
