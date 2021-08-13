package zhangyu.fool.generator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generator.enums.ProjectEnum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author xiaomingzhang
 * @date 2021/8/12
 * 执行sql脚本
 */
public class RunSqlScriptUtil {

    private static Logger log = LoggerFactory.getLogger(RunSqlScriptUtil.class);

    public final static String TEST_SCRIPT_PATH = "src/main/resources/sql/foolCode.sql";

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String DRIVER_2 = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/";

    private static final String USER_NAME = "root";
    private static final String PASS_WORD = "root";


    public static Connection getLocalConnection() {
        try {
            String url = XmlUtil.getText(ProjectEnum.URL);
            if (url != null) {
                url = url.substring(0, url.lastIndexOf("/")) + "?serverTimezone=GMT%2B8";
            }
            String user = XmlUtil.getText(ProjectEnum.USERNAME);
            String password = XmlUtil.getText(ProjectEnum.PASSWORD);
            Connection conn = DriverManager.getConnection(url, user == null ? USER_NAME : user, password == null ? PASS_WORD : password);
            Class.forName(DRIVER_2);
            return conn;
        } catch (Exception e) {
            log.error("获取数据库连接失败", e);
            throw new RuntimeException("获取数据库连接失败");
        }
    }

    /**
     * @param path sql脚本路径
     */
    public static void executeSqlScriptFile(String path) {

        String sqlScript = readSqlFile(path);

        executeSqlScript(sqlScript);

    }

    public static void executeSqlScript(String sqlScript) {

        List<String> sqlList = splitSqlScript(sqlScript);

        executeSqlList(sqlList);
    }

    /**
     * 不支持读取复杂脚本，只单纯以 “; ”分割，满足我初始化测试数据库的需求
     *
     * @param sqlFilePath
     * @return
     */
    private static String readSqlFile(String sqlFilePath) {
        StringBuilder sql = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(new File(sqlFilePath));
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader bufferedReader = new BufferedReader(isr)) {
            Stream<String> lines = bufferedReader.lines();
            lines.forEach(e -> sql.append(e).append("\n"));
        } catch (Exception e) {
            log.error("read sql file exception", e);
        }
        return sql.toString();
    }

    private static List<String> splitSqlScript(String content) {
        List<String> sqlList = new ArrayList<>();
        String[] split = content.split(";");
        for (String sql : split) {
            if (!"".equals(sql.trim())) {
                sqlList.add(sql + ";");
            }
        }
        return sqlList;
    }


    private static void executeSqlList(List<String> sqlList) {
        try (Connection connection = getLocalConnection();
             Statement statement = connection.createStatement()) {
            for (String sql : sqlList) {
                log.info("执行了sql: {}", sql);
                statement.execute(sql);
                int rowsAffected = statement.getUpdateCount();
                log.info("更新了{}行", rowsAffected);
            }
        } catch (Exception e) {

            log.error("execute sql script exception", e);
        }
    }

    /**
     * 删除测试数据库
     * @param path
     */
    public static void deleteTestDatabase(String path){
        String sqlScript = readSqlFile(path);

        List<String> sqlList = splitSqlScript(sqlScript);
        //执行sql脚本第一句
        executeSqlList(Arrays.asList(sqlList.get(0)));
    }

}
