package zhangyu.fool.generator.main.model.param;

import lombok.Data;
import zhangyu.fool.generator.model.Author;

/**
 * @author xiaomingzhang
 * @date 2021/7/23
 */
@Data
public class CommonParam {

    /**
     * 是否使用jpa
     */
    private Boolean isJpa;
    /**
     * 是否使用lombok
     */
    private Boolean isLombok;
    /**
     * 是否使用MyBatis
     */
    private Boolean isMyBatis;
    /**
     * 是否使用MyBatis
     */
    private Boolean isMyBatisPlus;
    /**
     * 类作者注释
     */
    private Author author;
    /**
     * 基础包名
     */
    private String basePackagePath;
    /**
     * 实体类名，如CourseRecord
     */
    private String entityName;

    /**
     * 实体类名，首字母小写，如courseRecord
     */
    private String entityNameLow;

}
