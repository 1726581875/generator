package zhangyu.fool.generator.main.writer.java;

import zhangyu.fool.generator.model.Author;
import zhangyu.fool.generator.util.FileUtil;
import zhangyu.fool.generator.util.NameConvertUtil;
import zhangyu.fool.generator.main.writer.AbstractCodeWriter;
import zhangyu.fool.generator.main.annotation.Writer;
import zhangyu.fool.generator.main.enums.WriterEnum;
import zhangyu.fool.generator.main.model.ProjectConfig;
import zhangyu.fool.generator.main.model.param.CommonParam;

import java.io.File;

/**
 * @author xiaomingzhang
 * @date 2021/6/8
 */
@Writer(type = WriterEnum.CONFIG)
public class ConfigWriter extends AbstractCodeWriter {

    public static final String CONFIG_TEMPLATE_PATH = TEMPLATE_BASE_PATH + "/config";

    public static final String CORS_CONFIG_TEMPLATE_NAME = "CorsConfig";

    public static final String JSON_CONFIG_TEMPLATE_NAME = "JacksonConfig";

    public ConfigWriter() {
        super(new ProjectConfig());
    }

    public ConfigWriter(ProjectConfig projectConfig) {
        super(projectConfig);
    }


    @Override
    public void write(String destPath) {
        this.write(destPath,CORS_CONFIG_TEMPLATE_NAME);
    }

    @Override
    public void write(String destPath, String templateName) {
        FileUtil.checkAndCreateDir(destPath);
        //生成跨域配置
        String fullPath = destPath + File.separator +"CorsConfig.java";
        this.writeByParam(CONFIG_TEMPLATE_PATH, CORS_CONFIG_TEMPLATE_NAME, fullPath, this.buildParam(null,null));
        log.info("已生成 [CorsConfig.java]");
        //生成Long类型json序列化配置
        String fullPath2 = destPath + File.separator +"JacksonConfig.java";
        this.writeByParam(CONFIG_TEMPLATE_PATH, JSON_CONFIG_TEMPLATE_NAME, fullPath2, this.buildParam(null,null));
        log.info("已生成 [JacksonConfig.java]");
    }

    @Override
    public CommonParam buildParam(String tableName, String entityName) {
        CommonParam commonParam = new CommonParam();
        this.buildBaseParam(commonParam);
        return commonParam;
    }

}
