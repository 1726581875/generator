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
public class TableInfo {

    public static final String TABLE_NAME = "TABLE_NAME";
    public static final String TABLE_COMMENT = "TABLE_COMMENT";

    private String tableName;

    private String tableComment;

    public static TableInfo getTableInfo(ResultSet resultSet) throws SQLException {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(resultSet.getString(TABLE_NAME));
        tableInfo.setTableComment(resultSet.getString(TABLE_COMMENT));
        return tableInfo;
    }

}
