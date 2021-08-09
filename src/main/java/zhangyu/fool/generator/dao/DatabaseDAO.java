package zhangyu.fool.generator.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generator.model.mysql.TableColumn;
import zhangyu.fool.generator.model.mysql.TableInfo;
import zhangyu.fool.generator.util.DatabaseUtil;
import zhangyu.fool.generator.main.model.TableSql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xmz
 * @date: 2021/07/26
 */
public class DatabaseDAO {

    private static Logger log = LoggerFactory.getLogger(DatabaseDAO.class);

    public static List<String> getTableNameList() {
        List<String> tableNameList = new ArrayList<>();
        try(Connection connection = DatabaseUtil.getConnection();
            PreparedStatement prepareStatement = connection.prepareStatement("show tables");
            ResultSet resultSet = prepareStatement.executeQuery()){
            while (resultSet.next()) {
                tableNameList.add(resultSet.getString(1));
            }
        } catch (Exception e) {
            log.error("show tables发生错误",e);
        }
        return tableNameList;
    }

    public static TableSql getCreateTableSQL(String tableName) {
        TableSql tableSql = new TableSql();
        try(Connection connection = DatabaseUtil.getConnection();
            PreparedStatement prepareStatement = connection.prepareStatement("show create table `"+ tableName +"`");
            ResultSet resultSet = prepareStatement.executeQuery()){
            if(resultSet.next()) {
                tableSql.setTableName(resultSet.getString(1));
                tableSql.setTableSql(resultSet.getString(2));
            }
        }catch (Exception e) {
            log.error("获取表{}的建表语句发生错误", tableName, e);
        }
        return tableSql;
    }



    public static List<TableColumn> getColumnByTableName(String tableName) {
        List<TableColumn> tableColumnList = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet result = stmt.executeQuery("show full columns from `" + tableName + "`")) {
            if (result != null) {
                while (result.next()) {
                    tableColumnList.add(TableColumn.getTableColumn(result));
                }
            }
        } catch (Exception e) {
            log.error("获取表{}的列信息名发生错误", tableName, e);
        }
        return tableColumnList;
    }

    public static List<TableInfo> getTableInfList(String databaseName) {
        List<TableInfo> tableInfoList = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet result = stmt.executeQuery("SELECT * FROM information_schema.TABLES WHERE table_schema= '" + databaseName + "'")) {
            if (result != null) {
                while (result.next()) {
                    tableInfoList.add(TableInfo.getTableInfo(result));
                }
            }
        } catch (Exception e) {
            log.error("获取数据库{}的表信息名发生错误", databaseName, e);
        }
        return tableInfoList;
    }



}