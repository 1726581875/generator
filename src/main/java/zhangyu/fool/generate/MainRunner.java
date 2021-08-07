package zhangyu.fool.generate;

import zhangyu.fool.generate.writer.builder.WriterBuilderFactory;
import zhangyu.fool.generate.writer.java.MavenProjectWriter;
import zhangyu.fool.generate.writer.model.ProjectConfig;
import zhangyu.fool.generate.writer.vue.VueProjectWriter;

/**
 * @author xmz
 * @date: 2021/06/14
 * main函数测试
 */
public class MainRunner {

    public static void main(String[] args) {
        String path = "D:\\data\\test";
        //生成springboot工程代码,可以通过配置ProjectConfig选择使用Jpa/MyBatis/MyBatis-plus
        long startTime = System.currentTimeMillis();
        WriterBuilderFactory.toGetBuilder(MavenProjectWriter.class)
                .build(ProjectConfig.buildMyBatisPlus())
                .write(path);
        System.out.println("生成java文件耗时：" + (System.currentTimeMillis() - startTime));
        //生成vue工程代码
/*        VueProjectWriter vueProjectWriter = new VueProjectWriter();
        vueProjectWriter.write(path);*/
    }


}
