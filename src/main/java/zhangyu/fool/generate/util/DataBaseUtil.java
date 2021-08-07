package zhangyu.fool.generate.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generate.enums.ProjectEnum;
import zhangyu.fool.generate.enums.TypeMappingEnum;
import zhangyu.fool.generate.model.TableField;

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
    public static Set<String> getJavaTypes(List<TableField> fieldList) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < fieldList.size(); i++) {
            TableField field = fieldList.get(i);
            set.add(field.getJavaType());
        }
        return set;
    }


    public static void main(String[] args) {

    }


}
