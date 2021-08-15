package zhangyu.fool.generator;

import org.junit.*;
import zhangyu.fool.generator.util.FileUtil;
import zhangyu.fool.generator.util.RunSqlScriptUtil;
import zhangyu.fool.generator.util.XmlUtil;

/**
 * @author xiaomingzhang
 * @date 2021/8/13
 */
public class BaseTest {

    /** 测试配置文件位置 **/
    public static final String TEST_PROFILE_PATH = "src\\main\\resources\\test\\profile.xml";

    /** 初始化测试数据库脚本位置 **/
    public static final String TEST_SQL_SCRIPT = "src\\main\\resources\\test\\sql\\fool.sql";

    /** 代码生成位置 **/
    public static final String BASE_TEST_PATH = "D:\\data\\test";

    @BeforeClass
    public static void init(){
        //初始化读取测试profile.xml
        XmlUtil.setProfilePath(TEST_PROFILE_PATH);
        //初始化测试数据库脚本
        RunSqlScriptUtil.executeSqlScriptFile(TEST_SQL_SCRIPT);
    }


    @AfterClass
    public static void clean(){
        //删除测试数据库
        //RunSqlScriptUtil.deleteTestDatabase(TEST_SQL_SCRIPT);
        FileUtil.delete(BASE_TEST_PATH);
    }


}
