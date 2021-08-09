package zhangyu.fool.generator.model.mysql;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xmz
 * @date: 2021/08/07
 * sql查询结果映射类: show full columns from `tableName`
 */
@Data
public class TableColumn {

    public static final String FIELD = "Field";

    public static final String TYPE = "Type";

    public static final String COMMENT = "Comment";

    public static final String NULL = "Null";

    public static final String KEY = "Key";

    /** Key的值，主键 */
    public static final String PRI = "PRI";
    /** Key的值，唯一键 */
    public static final String UNI = "UNI";


    private String field;

    private String type;

    private String comment;

    private String nullAble;

    private String key;

    public static TableColumn getTableColumn(ResultSet resultSet) throws SQLException {
        TableColumn tableColumn = new TableColumn();
        tableColumn.setField(resultSet.getString(FIELD));
        tableColumn.setType(resultSet.getString(TYPE));
        tableColumn.setComment(resultSet.getString(COMMENT));
        tableColumn.setNullAble(resultSet.getString(NULL));
        tableColumn.setKey(resultSet.getString(KEY));
        return tableColumn;
    }


}
