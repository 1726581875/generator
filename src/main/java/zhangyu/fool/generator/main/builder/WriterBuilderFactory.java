package zhangyu.fool.generator.main.builder;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generator.MainRunner;
import zhangyu.fool.generator.main.annotation.Writer;
import zhangyu.fool.generator.main.enums.WriterEnum;
import zhangyu.fool.generator.main.writer.java.ConfigWriter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author xiaomingzhang
 * @date 2021/7/29
 */
public class WriterBuilderFactory {

    private static final Map<WriterEnum, Class<?>> WRITER_CLASS_MAP = new HashMap<>(16);

    private static final Logger log = LoggerFactory.getLogger(WriterBuilderFactory.class);

    static {
        //反射获取对应包标有注解的类
        Reflections reflections = new Reflections(MainRunner.class.getPackage().getName());
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Writer.class);
        classSet.stream().forEach(clazz -> {
            Writer annotation = clazz.getDeclaredAnnotation(Writer.class);
            WRITER_CLASS_MAP.put(annotation.type(),clazz);
        });
    }


    public static final WriterBuilder toGetBuilder(Class<?> clazz){
        return new WriterBuilder(clazz);
    }

    public static final WriterBuilder toGetBuilder(WriterEnum writerEnum){
        Class<?> aClass = WRITER_CLASS_MAP.get(writerEnum);
        return new WriterBuilder(aClass);
    }



    public static void main(String[] args) {
        WriterBuilderFactory.toGetBuilder(ConfigWriter.class);
    }

}
