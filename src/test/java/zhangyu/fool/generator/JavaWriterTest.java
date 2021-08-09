package zhangyu.fool.generator;

import org.junit.Test;
import zhangyu.fool.generator.main.builder.WriterBuilder;
import zhangyu.fool.generator.main.builder.WriterBuilderFactory;
import zhangyu.fool.generator.main.writer.java.*;
import zhangyu.fool.generator.main.model.ProjectConfig;

import java.io.File;

/**
 * @author xmz
 * @date: 2021/07/28
 */
public class JavaWriterTest {

    private static final String baseTestPath = "D:\\data\\test";

    private static final ProjectConfig jpaConfig = ProjectConfig.buildJpa();
    private static final ProjectConfig myBatisConfig = ProjectConfig.buildMyBatis();

    /**
     * 递归删除文件
     * @param baseTestPath
     */
    private void clean(String baseTestPath){
        File file = new File(baseTestPath);
        if (file.exists() && file.isDirectory()){
            File[] files = file.listFiles();
            for (File sonFile : files) {
                if(sonFile.isDirectory()){
                    clean(sonFile.getAbsolutePath());
                }
                sonFile.deleteOnExit();
            }
        }
        file.deleteOnExit();
    }

    private void writerFileTestByClass(Class<?> clazz) {
        WriterBuilder writerBuilder = WriterBuilderFactory.toGetBuilder(clazz);
        //jpa版本
        writerBuilder.build(jpaConfig).write(baseTestPath);
        clean(baseTestPath);
        //mybatis版本
        writerBuilder.build(myBatisConfig).write(baseTestPath);
        clean(baseTestPath);
    }

    @Test
    public void entityWriterTest(){
        this.writerFileTestByClass(EntityWriter.class);
    }

    @Test
    public void dtoWriterTest(){
        this.writerFileTestByClass(DtoWriter.class);
    }

    @Test
    public void daoWriterTest(){
        this.writerFileTestByClass(DaoWriter.class);
    }

    @Test
    public void serviceWriterTest(){
        this.writerFileTestByClass(ServiceWriter.class);
    }

    @Test
    public void controllerWriterTest(){
        this.writerFileTestByClass(ControllerWriter.class);
    }

    @Test
    public void testWriterTest(){
        this.writerFileTestByClass(TestWriter.class);
    }

    @Test
    public void voWriterTest(){
        this.writerFileTestByClass(VoWriter.class);
    }

    @Test
    public void configWriterTest(){
        this.writerFileTestByClass(ConfigWriter.class);
    }

    @Test
    public void mavenProjectWriterTest(){
        this.writerFileTestByClass(MavenProjectWriter.class);
    }



}
