package zhangyu.fool.generator.writer;

import org.junit.Test;
import zhangyu.fool.generator.BaseTest;
import zhangyu.fool.generator.main.writer.vue.ViewWriter;
import zhangyu.fool.generator.main.writer.vue.VueProjectWriter;

/**
 * @author xiaomingzhang
 * @date 2021/8/13
 */
public class VueWriterTest extends BaseTest {

    @Test
    public void viewWriterTest() {
        new ViewWriter().write(BASE_TEST_PATH);
    }

    @Test
    public void vueProjectWriterTest() {
        new VueProjectWriter().write(BASE_TEST_PATH);
    }

}
