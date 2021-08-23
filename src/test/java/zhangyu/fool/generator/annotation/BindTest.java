package zhangyu.fool.generator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 仅仅标记测试类对应的类（不能反过来标记类对应的测试类，这就很烦）
 * 暂不做任何处理
 * @author xiaominzghang
 * @date 2021/08/16
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BindTest {
    Class<?> value();
}
