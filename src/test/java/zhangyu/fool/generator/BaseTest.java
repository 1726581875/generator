package zhangyu.fool.generator;

import org.junit.*;
import zhangyu.fool.generator.util.RunSqlScriptUtil;
import zhangyu.fool.generator.util.XmlUtil;

/**
 * @author xiaomingzhang
 * @date 2021/8/13
 */
public class BaseTest {

    public final static String TEST_PROFILE_PATH = "src\\main\\resources\\test\\profile.xml";

    public final static String TEST_SQL_SCRIPT = "src\\main\\resources\\test\\sql\\fool.sql";

    @BeforeClass
    public static void init(){
        //初始化读取测试profile.xml
        XmlUtil.setProfilePath(TEST_PROFILE_PATH);
        //初始化测试数据库脚本
        RunSqlScriptUtil.executeSqlScriptFile(TEST_SQL_SCRIPT);
    }


    @AfterClass
    public static void clean(){
        RunSqlScriptUtil.deleteTestDatabase(TEST_SQL_SCRIPT);
    }


}
