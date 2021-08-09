package zhangyu.fool.generator.model.mysql;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xmz
 * @date: 2021/08/07
 * 查询数据库表的列信息 : show full columns from `tableName`
 */
@Data
public class TableColumn implements MySqlMetadata{

    public static final String SQL = "show full columns from `%s`";

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

    public static String getSQL(Object... params) {
        return String.format(SQL, params);
    }


    @Override
    public MySqlMetadata getAnalyzedData(ResultSet result) throws SQLException {
        TableColumn tableColumn = new TableColumn();
        tableColumn.setField(result.getString(FIELD));
        tableColumn.setType(result.getString(TYPE));
        tableColumn.setComment(result.getString(COMMENT));
        tableColumn.setNullAble(result.getString(NULL));
        tableColumn.setKey(result.getString(KEY));
        return tableColumn;
    }
}
