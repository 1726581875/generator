package zhangyu.fool.generator.main.model.param;

import lombok.Data;
import zhangyu.fool.generator.model.TableField;

import java.util.List;
import java.util.Set;

/**
 * @author xiaomingzhang
 * @date 2021/7/23
 */
@Data
public class EntityParam extends CommonParam {

    private List<TableField> fieldList;

    private Set<String> javaTypeSet;

    private String tableName;

}
