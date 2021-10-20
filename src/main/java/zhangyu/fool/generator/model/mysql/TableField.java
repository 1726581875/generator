package zhangyu.fool.generator.model.mysql;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xiaomingzhang
 * @date 2021/10/15
 * select * from Information_schema.columns  where table_name= '表名'and table_schema='数据库名'
 */
@Data
public class TableField implements MySqlMetadata{

    public static final String SQL = "select * from Information_schema.columns where table_name= '%s' and table_schema='%s'";

    private String columnName;
    /**
     * 数据类型,如 int
     */
    private String dataType;

    /**
     * 字段备注
     */
    private String columnComment;

    /**
     * int(11) unsigned
     */
    private String columnType;

    public static String getSQL(Object... params) {
        return String.format(SQL, params);
    }

    @Override
    public TableField getAnalyzedData(ResultSet result) throws SQLException {
        TableField tableField = new TableField();
        tableField.setColumnName(result.getString("column_name"));
        tableField.setDataType(result.getString("data_type"));
        tableField.setColumnComment(result.getString("column_comment"));
        tableField.setColumnType(result.getString("column_type"));
        return tableField;
    }


}
