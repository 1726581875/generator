package zhangyu.fool.generate.writer.model.param;

import lombok.Data;
import zhangyu.fool.generate.model.Author;
import zhangyu.fool.generate.util.NameConvertUtil;

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
    private String basePackageName;
    /**
     * 实体类名，如CourseRecord
     */
    private String entityName;

    /**
     * 实体类名，首字母小写，如courseRecord
     */
    private String entityNameLow;

    /**
     * 初始化部分参数
     * @param tableName
     * @param entityName
     * @return
     */
    public CommonParam build(String tableName, String entityName) {
        this.setEntityName(entityName);
        this.setEntityNameLow(NameConvertUtil.bigHumpToHump(entityName));
        return this;
    }

}
