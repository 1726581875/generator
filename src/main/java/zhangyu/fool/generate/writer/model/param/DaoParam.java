package zhangyu.fool.generate.writer.model.param;

import lombok.Data;
import zhangyu.fool.generate.model.Field;

import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2021/7/23
 */
@Data
public class DaoParam extends CommonParam {

    private String daoPackageName;

    private String entityPackageName;
    /**
     * 对应表主键的java类型，如Integer/String/Long等
     */
    private String keyType;
    /**
     * 列信息
     */
    List<Field> fieldList;

    private String tableName;

}
