package zhangyu.fool.generator.main.model.param;

import lombok.Data;
import zhangyu.fool.generator.model.TableField;

import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2021/7/23
 */
@Data
public class DaoParam extends CommonParam {
    /**
     * 对应表主键的java类型，如Integer/String/Long等
     */
    private String keyType;
    /**
     * 列信息
     */
    List<TableField> fieldList;

    private String tableName;

}
