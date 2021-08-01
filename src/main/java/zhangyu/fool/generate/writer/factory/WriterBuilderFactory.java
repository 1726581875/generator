package zhangyu.fool.generate.writer.factory;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generate.writer.annotation.Writer;
import zhangyu.fool.generate.writer.java.ConfigWriter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaomingzhang
 * @date 2021/7/29
 */
public class WriterBuilderFactory {

    private static final Map<Class<?>, Class<?>> WRITER_CLASS_MAP = new ConcurrentHashMap<>();

    private static final Logger log = LoggerFactory.getLogger(WriterBuilderFactory.class);

    static {
        //反射获取对应包标有注解的类
        Reflections reflections = new Reflections("zhangyu.fool.writer");
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Writer.class);
        classSet.stream().forEach(clazz -> {
            System.out.println(clazz.getName());
        });
    }


    public static final WriterBuilder toGetBuilder(Class<?> clazz){
        return new WriterBuilder(clazz);
    }


    public static void main(String[] args) {
        WriterBuilderFactory.toGetBuilder(ConfigWriter.class);
    }

}
