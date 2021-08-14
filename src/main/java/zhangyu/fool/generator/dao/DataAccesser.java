package zhangyu.fool.generator.dao;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generator.model.mysql.MySqlMetadata;
import zhangyu.fool.generator.module.MainModule;
import zhangyu.fool.generator.service.DatabaseService;
import zhangyu.fool.generator.service.DatabaseServiceImpl;
import zhangyu.fool.generator.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomingzhang
 * @date 2021/8/13
 */
@Singleton
public class DataAccesser {

    private static Logger log = LoggerFactory.getLogger(DataAccesser.class);

    public <T extends MySqlMetadata> List<T> getList(String sql, Class<T> resultType) {
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

    public <T extends MySqlMetadata> T getOne(String sql, Class<T> resultType) {
        return getList(sql, resultType).get(0);
    }

    public String getDatabaseName() {
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

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MainModule());
        DatabaseService databaseService = injector.getInstance(DatabaseService.class);
        Map<String, String> tableNameMap = databaseService.getTableNameMap();
        System.out.println(tableNameMap);
    }

}
