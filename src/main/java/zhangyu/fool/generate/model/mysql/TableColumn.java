package zhangyu.fool.generate.model.mysql;

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

    private static final String TYPE = "Type";

    private static final String COMMENT = "Comment";

    private static final String NULL = "Null";

    private static final String KEY = "Key";

    /** Key的值，主键 */
    private static final String PRI = "PRI";
    /** Key的值，唯一键 */
    private static final String UNI = "UNI";


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
