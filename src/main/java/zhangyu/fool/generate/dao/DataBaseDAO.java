package zhangyu.fool.generate.dao;

import zhangyu.fool.generate.util.DataBaseUtil;
import zhangyu.fool.generate.writer.model.TableSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xmz
 * @date: 2021/07/26
 */
public class DataBaseDAO {


    public static List<String> getTableNameList(){
        List<String> tableNameList = new ArrayList<>();
        try(Connection connection = DataBaseUtil.getConnection();
            PreparedStatement prepareStatement = connection.prepareStatement("show tables");
            ResultSet resultSet = prepareStatement.executeQuery()){
            while (resultSet.next()) {
                tableNameList.add(resultSet.getString(1));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return tableNameList;
    }

    public static TableSql getCreateTableSQL(String tableName) {
        TableSql tableSql = new TableSql();
        try(Connection connection = DataBaseUtil.getConnection();
            PreparedStatement prepareStatement = connection.prepareStatement("show create table `"+ tableName +"`");
            ResultSet resultSet = prepareStatement.executeQuery()){
            if(resultSet.next()) {
                tableSql.setTableName(resultSet.getString(1));
                tableSql.setTableSql(resultSet.getString(2));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return tableSql;
    }



    public static void main(String[] args) {

    }


}
