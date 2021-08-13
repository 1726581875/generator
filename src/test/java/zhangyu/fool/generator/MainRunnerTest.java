package zhangyu.fool.generator;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import zhangyu.fool.generator.dao.DatabaseDAOTest;
import zhangyu.fool.generator.service.DatabaseService;
import zhangyu.fool.generator.service.DatabaseServiceTest;
import zhangyu.fool.generator.writer.DocWriterTest;
import zhangyu.fool.generator.writer.JavaWriterTest;
import zhangyu.fool.generator.writer.VueWriterTest;

/**
 * @author xiaomingzhang
 * @date 2021/8/13
 * 一次运行多个测试类
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        DatabaseDAOTest.class,
        DatabaseServiceTest.class,
        DocWriterTest.class,
        JavaWriterTest.class,
        VueWriterTest.class
})
public class MainRunnerTest {

}
