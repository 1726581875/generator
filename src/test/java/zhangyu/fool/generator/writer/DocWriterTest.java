package zhangyu.fool.generator.writer;

import org.junit.Test;
import zhangyu.fool.generator.BaseTest;
import zhangyu.fool.generator.main.writer.doc.SqlDocxWriter;
import zhangyu.fool.generator.main.writer.doc.SqlScriptWriter;

/**
 * @author xiaomingzhang
 * @date 2021/8/9
 */
public class DocWriterTest extends BaseTest {

    @Test
    public void runSqlScriptWriterTest(){
        new SqlScriptWriter().write(BASE_TEST_PATH);
    }

    @Test
    public void runSqlDocxtWriterTest(){
        new SqlDocxWriter().write(BASE_TEST_PATH);
    }


}
