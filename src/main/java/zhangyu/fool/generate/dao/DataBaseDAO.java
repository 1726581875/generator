package zhangyu.fool.generate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generate.enums.TypeMappingEnum;
import zhangyu.fool.generate.model.TableField;
import zhangyu.fool.generate.util.DataBaseUtil;
import zhangyu.fool.generate.util.NameConvertUtil;
import zhangyu.fool.generate.writer.model.TableSql;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xmz
 * @date: 2021/07/26
 */
public class DataBaseDAO {

    private static Logger log = LoggerFactory.getLogger(DataBaseDAO.class);

    public static List<String> getTableNameList() {
        List<String> tableNameList = new ArrayList<>();
        try(Connection connection = DataBaseUtil.getConnection();
            PreparedStatement prepareStatement = connection.prepareStatement("show tables");
            ResultSet resultSet = prepareStatement.executeQuery()){
            while (resultSet.next()) {
                tableNameList.add(resultSet.getString(1));
            }
        } catch (Exception e){
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



    public static List<TableField> getFieldByTableName(String tableName) {
        List<TableField> fieldList = new ArrayList<>();
        try (Connection conn = DataBaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet result = stmt.executeQuery("show full columns from `" + tableName + "`")) {
            if (result != null) {
                while (result.next()) {
                    fieldList.add(buildField(result));
                }
            }
            return fieldList;
        } catch (Exception e) {
            log.error("获取表{}的列信息名发生错误{}", tableName, e);
        }
        return fieldList;
    }


    private Object getResultSet(ResultSet result, Class<?> resultObj) {
        Object obj = null;
        try {
            obj = resultObj.newInstance();
            Field[] declaredFields = resultObj.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                String name = field.getName();
                Object value = result.getObject(name);
                if(value != null) {
                    field.set(obj,value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static TableField buildField(ResultSet result) throws SQLException {
        String columnName = result.getString("Field");
        //fixme 类型获取不准确
        String type = result.getString("Type");
        String comment = result.getString("Comment");
        // YES NO
        String nullAble = result.getString("Null");
        String keyType = result.getString("Key");
        TableField field = new TableField();
        field.setName(columnName);
        field.setNameHump(NameConvertUtil.lineToHump(columnName));
        field.setNameBigHump(NameConvertUtil.lineToBigHump(columnName));
        field.setType(type);
        field.setJavaType(TypeMappingEnum.getJavaType(type));
        field.setComment(comment);
        field.setKeyType(keyType);
        if (comment.contains("|")) {
            field.setNameCn(comment.substring(0, comment.indexOf("|")));
        } else {
            field.setNameCn(comment);
        }
        field.setNullAble("YES".equals(nullAble));
        if (type.toUpperCase().contains("varchar".toUpperCase())) {
            String lengthStr = type.substring(type.indexOf("(") + 1, type.length() - 1);
            field.setLength(Integer.valueOf(lengthStr));
        } else {
            field.setLength(0);
        }
        return field;
    }



    public static void main(String[] args) {

    }


}
