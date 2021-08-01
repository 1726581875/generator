package zhangyu.fool.generate.writer;

import lombok.Data;
import zhangyu.fool.generate.model.Author;
import zhangyu.fool.generate.service.DatabaseService;
import zhangyu.fool.generate.util.AssertUtil;
import zhangyu.fool.generate.util.ObjectToMapUtil;
import zhangyu.fool.generate.writer.enums.TypeSuffixEnum;
import zhangyu.fool.generate.writer.model.ProjectConfig;
import zhangyu.fool.generate.writer.model.param.CommonParam;

import java.io.File;
import java.util.Map;

/**
 * @author xmz
 * @date: 2021/07/25
 */
public abstract class AbstractCodeWriter implements CodeWriter {
    /**
     * 工程配置
     */
    protected ProjectConfig projectConfig;
    /**
     * 参数
     */
    protected CommonParam commonParam;

    public AbstractCodeWriter(ProjectConfig projectConfig){
        this.projectConfig = projectConfig;
    }

    @Override
    public abstract void write(String destPath, String templateName);

    @Override
    public abstract void write(String destPath);

    /**
     * 构造参数
     * @param tableName
     * @param entityName
     * @return
     */
    abstract public CommonParam buildParam(String tableName, String entityName);


    public void buildBaseParam(CommonParam commonParam, ProjectConfig projectConfig){
        commonParam.setAuthor(Author.build());
        commonParam.setIsJpa(projectConfig.isUseJpa());
        commonParam.setIsLombok(projectConfig.isUseLombok());
        commonParam.setIsMyBatisPlus(projectConfig.isUseMyBatisPlus());
        commonParam.setIsMyBatis(projectConfig.isUseMyBatis());
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
        this.checkAndCreateDir(writeConfig.getDestPath());
        TypeSuffixEnum typeSuffixEnum = writeConfig.getTypeSuffixEnum();
        log.info("======开始生成{}类   begin======",typeSuffixEnum.getType());
        Map<String, String> tableMap = DatabaseService.getTableNameMap();
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
