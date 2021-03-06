package zhangyu.fool.generator.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generator.model.mysql.MySqlMetadata;
import zhangyu.fool.generator.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xmz
 * @date: 2021/07/26
 */
public class DatabaseDAO {

    private static Logger log = LoggerFactory.getLogger(DatabaseDAO.class);


    public static <T extends MySqlMetadata> List<T> getList(String sql, Class<T> resultType) {
        List<T> list = new ArrayList<>();
        try(Connection connection = DatabaseUtil.getConnection();
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery()){

            MySqlMetadata metadata = resultType.getDeclaredConstructor().newInstance();
            while (resultSet.next()) {
                list.add((T) metadata.getAnalyzedData(resultSet));
            }
        } catch (Exception e) {
            log.error("获取数据发生错误",e);
        }
        return list;
    }

    public static <T extends MySqlMetadata> T getOne(String sql, Class<T> resultType) {
        return getList(sql, resultType).get(0);
    }


    public static String getDatabaseName(){
        String name = "";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet result = stmt.executeQuery("SELECT database()")) {
            if (result != null) {
                while (result.next()) {
                    name = result.getString(1);
                }
            }
        } catch (Exception e) {
            log.error("获取当前数据库名发生错误", e);
        }
        return name;
    }

}
