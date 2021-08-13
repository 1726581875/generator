package zhangyu.fool.generator.main.writer;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import lombok.Data;
import zhangyu.fool.generator.model.Author;
import zhangyu.fool.generator.service.DatabaseServiceImpl;
import zhangyu.fool.generator.util.AssertUtil;
import zhangyu.fool.generator.util.FileUtil;
import zhangyu.fool.generator.util.ObjectToMapUtil;
import zhangyu.fool.generator.main.enums.TypeSuffixEnum;
import zhangyu.fool.generator.main.model.ProjectConfig;
import zhangyu.fool.generator.main.model.param.CommonParam;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

/**
 * @author xmz
 * @date: 2021/07/25
 */
public abstract class AbstractCodeWriter implements FoolWriter {
    /**
     * 工程配置
     */
    protected ProjectConfig projectConfig;

    protected AbstractCodeWriter(ProjectConfig projectConfig){
        this.projectConfig = projectConfig;
    }

    @Override
    public void write(String destPath, String templateName) {
        this.write(destPath,null);
    }

    /**
     * 构造参数
     * @param tableName
     * @param entityName
     * @return
     */
    abstract public CommonParam buildParam(String tableName, String entityName);


    public void buildBaseParam(CommonParam commonParam, ProjectConfig projectConfig) {
        commonParam.setAuthor(Author.build());
        commonParam.setIsJpa(projectConfig.isUseJpa());
        commonParam.setIsLombok(projectConfig.isUseLombok());
        commonParam.setIsMyBatisPlus(projectConfig.isUseMyBatisPlus());
        commonParam.setIsMyBatis(projectConfig.isUseMyBatis());
    }

    /**
     * 根据模板生成文件
     * @param templatePath 模板路径
     * @param templateName 使用模板名称
     * @param destFullPath 生成文件全路径（路径 + 文件名）
     * @param paramMap 参数map
     */
    public void writeByTemplate(String templatePath, String templateName, String destFullPath, Map<String, Object> paramMap) {
        File templateDir = new File(templatePath);
        if (!templateDir.exists()) {
            throw new IllegalArgumentException("模板路径[" + templateDir.getAbsolutePath() +"，必须提前创建");
        }
        try (FileWriter fileWriter = new FileWriter(destFullPath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            // 读模板
            Configuration config = new Configuration(Configuration.VERSION_2_3_29);
            config.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_29));
            config.setDirectoryForTemplateLoading(templateDir);
            Template template = config.getTemplate(templateName + ".ftl");
            // 写
            template.process(paramMap, bufferedWriter);
            bufferedWriter.flush();
        } catch (Exception e) {
            log.error("====加载模板{} 生成代码失败，发生异常====", templateName, e);
        }
    }

    protected void writeByParam(String templatePath, String templateName, String destFullPath, CommonParam param) {
        this.writeByTemplate(templatePath,templateName,destFullPath, ObjectToMapUtil.toMap(param));
    }

    /**
     * 循环生成文件
     * @param writeConfig
     */
    protected void forEachWrite(WriteConfig writeConfig) {
        this.checkWriteConfig(writeConfig);
        FileUtil.checkAndCreateDir(writeConfig.getDestPath());
        TypeSuffixEnum typeSuffixEnum = writeConfig.getTypeSuffixEnum();
        log.info("======开始生成{}类   begin======",typeSuffixEnum.getType());
        Map<String, String> tableMap = DatabaseServiceImpl.getTableNameMap();
        try {
            tableMap.forEach((tableName, entityName) -> {
                String destFullPath = writeConfig.getDestPath() + File.separator + entityName +  typeSuffixEnum.getSuffix();
                CommonParam param = this.buildParam(tableName,entityName);
                this.writeByParam(writeConfig.getTemplatePath(), writeConfig.getTemplateName(), destFullPath, param);
                log.info("已生成[{}{}}]", entityName, typeSuffixEnum.getSuffix());
            });
        } catch (Exception e) {
            log.error("======{}{}生成发生异常，异常信息:======", typeSuffixEnum.getType() ,typeSuffixEnum.getSuffix(), e);
        }
        log.info("======{}类生成完成  end======",typeSuffixEnum.getType());
    }

    protected void checkWriteConfig(WriteConfig writeConfig){
        AssertUtil.allNotNull(writeConfig.getDestPath(), writeConfig.getTypeSuffixEnum()
                ,writeConfig.getTemplateName(),writeConfig.getDestPath());
    }

    @Data
    public static final class WriteConfig {
        /** must **/
        String destPath;
        String templatePath;
        String templateName;
        TypeSuffixEnum typeSuffixEnum;
    }


}
