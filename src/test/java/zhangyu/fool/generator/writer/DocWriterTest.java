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

    private static final String baseTestPath = "D:\\data\\test";

    @Test
    public void runSqlScriptWriterTest(){
        new SqlScriptWriter().write(baseTestPath);
    }

    @Test
    public void runSqlDocxtWriterTest(){
        new SqlDocxWriter().write(baseTestPath);
    }


}
