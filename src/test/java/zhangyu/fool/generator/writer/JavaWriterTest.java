package zhangyu.fool.generator.writer;

import org.junit.Test;
import zhangyu.fool.generator.BaseTest;
import zhangyu.fool.generator.main.builder.WriterBuilder;
import zhangyu.fool.generator.main.builder.WriterBuilderFactory;
import zhangyu.fool.generator.main.writer.java.*;
import zhangyu.fool.generator.main.model.ProjectConfig;
import zhangyu.fool.generator.util.FileUtil;

/**
 * @author xmz
 * @date: 2021/07/28
 */
public class JavaWriterTest extends BaseTest {

    private static final ProjectConfig jpaConfig = ProjectConfig.buildJpa();
    private static final ProjectConfig myBatisConfig = ProjectConfig.buildMyBatis();
    private static final ProjectConfig myBatisPlusConfig = ProjectConfig.buildMyBatisPlus();


    private void writerFileTestByClass(Class<?> clazz) {
        WriterBuilder writerBuilder = WriterBuilderFactory.toGetBuilder(clazz);
        //jpa版本
        writerBuilder.build(jpaConfig).write(BASE_TEST_PATH);
        FileUtil.delete(BASE_TEST_PATH);
        //mybatis版本
        writerBuilder.build(myBatisConfig).write(BASE_TEST_PATH);
        FileUtil.delete(BASE_TEST_PATH);
        //mybatis-plus版本
        writerBuilder.build(myBatisPlusConfig).write(BASE_TEST_PATH);
        FileUtil.delete(BASE_TEST_PATH);
    }

    @Test
    public void entityWriterTest() {
        this.writerFileTestByClass(EntityWriter.class);
    }

    @Test
    public void dtoWriterTest() {
        this.writerFileTestByClass(DtoWriter.class);
    }

    @Test
    public void daoWriterTest() {
        this.writerFileTestByClass(DaoWriter.class);
    }

    @Test
    public void serviceWriterTest() {
        this.writerFileTestByClass(ServiceWriter.class);
    }

    @Test
    public void controllerWriterTest() {
        this.writerFileTestByClass(ControllerWriter.class);
    }

    @Test
    public void testWriterTest() {
        this.writerFileTestByClass(TestWriter.class);
    }

    @Test
    public void voWriterTest() {
        this.writerFileTestByClass(VoWriter.class);
    }

    @Test
    public void configWriterTest() {
        this.writerFileTestByClass(ConfigWriter.class);
    }

    @Test
    public void mavenProjectWriterTest() {
        this.writerFileTestByClass(MavenProjectWriter.class);
    }


}
