package zhangyu.fool.generator.main.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generator.main.writer.FoolWriter;
import zhangyu.fool.generator.main.model.ProjectConfig;

import java.lang.reflect.Constructor;

/**
 * @author xiaomingzhang
 * @date 2021/7/29
 */
public class WriterBuilder {

    private static final Logger log = LoggerFactory.getLogger(WriterBuilder.class);

    private Class<?> clazz;

    public WriterBuilder(Class<?> clazz) {
        this.clazz = clazz;
    }

    public FoolWriter build(ProjectConfig projectConfig){
        if(clazz == null) {
            throw new RuntimeException("build writer fail. cause: class is null");
        }
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(ProjectConfig.class);
            FoolWriter codeWriter = (FoolWriter) constructor.newInstance(projectConfig);
            return codeWriter;
        } catch (Exception e) {
            log.error("",e);
            throw new RuntimeException("build writer fail. cause:" + e.getMessage());
        }
    }


}
