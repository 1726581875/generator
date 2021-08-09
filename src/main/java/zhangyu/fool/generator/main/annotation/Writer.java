package zhangyu.fool.generator.main.annotation;

import zhangyu.fool.generator.main.enums.WriterEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xiaomingzhang
 * @date 2021/07/29
 * 标记哪些类是writer类
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Writer {
    String value() default "";

    WriterEnum type();
}
