package zhangyu.fool.generator.model.mysql;

import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xiaomingzhang
 * @date 2021/7/27
 * 获取建表语句对象
 */
@Data
public class TableSql implements MySqlMetadata {

    public static final String SQL = "SHOW CREATE TABLE `%s`";

    public static final String TABLE_NAME = "Table";

    public static final String CREATE_SQL = "Create Table";

    private String tableName;

    private String tableSql;

    @Override
    public MySqlMetadata getAnalyzedData(ResultSet result) throws SQLException {
        TableSql tableSql = new TableSql();
        tableSql.setTableName(result.getString(TABLE_NAME));
        tableSql.setTableSql(result.getString(CREATE_SQL));
        return tableSql;
    }

    public static String getSQL(Object... params) {
        return String.format(SQL, params);
    }
}
