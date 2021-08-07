package zhangyu.fool.generate.writer.model.param;

import lombok.Data;
import zhangyu.fool.generate.model.TableField;

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

    private String packageName;

    private String tableName;

}
