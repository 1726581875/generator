package zhangyu.fool.generator.model.mysql;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xiaomingzhang
 * @date 2021/8/9
 * 查询表详情 SELECT * FROM information_schema.TABLES WHERE table_schema= 'databaseName';
 */
@Data
public class TableInfo implements MySqlMetadata {

    public static final String SQL = "SELECT * FROM information_schema.TABLES WHERE table_schema= '%s'";

    public static final String TABLE_NAME = "TABLE_NAME";
    public static final String TABLE_COMMENT = "TABLE_COMMENT";

    private String tableName;

    private String tableComment;


    public static String getSQL(Object... params) {
        return String.format(SQL, params);
    }


    @Override
    public MySqlMetadata getAnalyzedData(ResultSet result) throws SQLException {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(result.getString(TABLE_NAME));
        tableInfo.setTableComment(result.getString(TABLE_COMMENT));
        return tableInfo;
    }
}
