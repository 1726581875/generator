package zhangyu.fool.generator.main.model;

import lombok.Data;

/**
 * @author xmz
 * @date: 2021/07/23
 */
@Data
public class ProjectConfig {
    /**
     * 是否使用Jpa
     */
    private boolean useJpa = true;
    /**
     * 是否使用Lombok
     */
    private boolean useLombok = true;
    /**
     * 是否使用MyBatis
     */
    private boolean useMyBatis = false;
    /**
     * 是否使用MyBatis-plus
     */
    private boolean useMyBatisPlus = false;

    public static ProjectConfig buildJpa(){
        ProjectConfig projectConfig = new ProjectConfig();
        projectConfig.setUseMyBatis(false);
        projectConfig.setUseLombok(true);
        projectConfig.setUseJpa(true);
        projectConfig.setUseMyBatisPlus(false);
        return projectConfig;
    }

    public static ProjectConfig buildMyBatis(){
        ProjectConfig projectConfig = new ProjectConfig();
        projectConfig.setUseMyBatis(true);
        projectConfig.setUseLombok(false);
        projectConfig.setUseJpa(false);
        projectConfig.setUseMyBatisPlus(false);
        return projectConfig;
    }

    public static ProjectConfig buildMyBatisPlus(){
        ProjectConfig projectConfig = new ProjectConfig();
        projectConfig.setUseMyBatis(false);
        projectConfig.setUseLombok(true);
        projectConfig.setUseJpa(false);
        projectConfig.setUseMyBatisPlus(true);
        return projectConfig;
    }


}
