package zhangyu.fool.generator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generator.enums.ProjectEnum;

import java.sql.*;

/**
 * @author xmz
 * 2020年9月12日
 * 数据库连接工具
 */
public class DatabaseUtil {

    private static Logger log = LoggerFactory.getLogger(DatabaseUtil.class);

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
            log.error("=====获取数据库连接失败====", e);
            throw new RuntimeException("获取数据库连接失败");
        }

    }

}
