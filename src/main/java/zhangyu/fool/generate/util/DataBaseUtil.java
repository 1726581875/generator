package zhangyu.fool.generate.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generate.enums.ProjectEnum;
import zhangyu.fool.generate.enums.TypeMappingEnum;
import zhangyu.fool.generate.model.Field;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xmz
 * 2020年9月12日
 * 数据库连接工具
 */
public class DataBaseUtil {

    private static Logger log = LoggerFactory.getLogger(DataBaseUtil.class);

    /**
     * 获取数据库连接
     *
     * @return
     */
    public static Connection getConnection() {
        // 获取到xml文件里配置的连接参数
        String url = XmlUtil.getText(ProjectEnum.URL);
        String user = XmlUtil.getText(ProjectEnum.USERNAME);
        String password = XmlUtil.getText(ProjectEnum.PASSWORD);
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            Class.forName(XmlUtil.getText(ProjectEnum.DRIVER));
            return conn;
        } catch (Exception e) {
            log.error("=====获取数据库连接失败{}====", e);
            throw new RuntimeException("获取数据库连接失败");
        }

    }


    public static List<Field> getColumnByTableName(String tableName) {

		List<Field> fieldList = new ArrayList<>();
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


	private static Field buildField(ResultSet result) throws SQLException {
		String columnName = result.getString("Field");
		//fixme 类型获取不准确
		String type = result.getString("Type");
		String comment = result.getString("Comment");
		// YES NO
		String nullAble = result.getString("Null");
		String keyType = result.getString("Key");
		Field field = new Field();
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



    /**
     * 获取<strong>表主键</strong>的Java类型
     *
     * @param tableName
     * @return 例如 Integer、String
     */
    public static String getPrimaryType(String tableName) {
        String keyType = null;
        try (Connection conn = DataBaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet result = stmt.executeQuery("show full columns from `" + tableName + "`")) {
            if (result != null) {
                while (result.next()) {
                    String key = result.getString("Key");
                    //如果是主键
                    if (key.equals("PRI")) {
                        String type = result.getString("Type");
                        keyType = TypeMappingEnum.getJavaType(type);
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取{}表主键的Java类型失败{}", tableName, e);
        }
        return keyType;
    }

    /**
     * 获取表主键id名（转换为大驼峰后的）
     *
     * @param tableName
     * @return 例如 ： ArticleId、UserId
     */
    public static String getPrimaryName(String tableName) {
        String primaryName = null;
        try (Connection conn = DataBaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet result = stmt.executeQuery("show full columns from `" + tableName + "`")) {
            if (result != null) {
                while (result.next()) {
                    String key = result.getString("Key");
                    if (key.equals("PRI")) {
                        String tableColumnName = result.getString("Field");
                        primaryName = NameConvertUtil.lineToBigHump(tableColumnName);
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取{}表主键的Java类型失败{}", tableName, e);
        }
        return primaryName;
    }


    /**
     * 获取所有的Java类型，使用Set去重
     */
    public static Set<String> getJavaTypes(List<Field> fieldList) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            set.add(field.getJavaType());
        }
        return set;
    }


    public static void main(String[] args) {
        List<Field> chapter = getColumnByTableName("chapter");
        chapter.forEach(System.out::println);
    }


}
