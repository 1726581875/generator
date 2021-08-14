package ${basePackagePath}.util;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

/**
 * @author ${author.author}
 * @date ${author.date}
 * 复制属性的工具类
 */
public class CopyUtil {

   /**
     * 拷贝一个list
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> List<T> copyList(List source, Class<T> targetClass) {
        List<T> target = new ArrayList<>();
        if (!CollectionUtils.isEmpty(source)){
            if (!CollectionUtils.isEmpty(source)){
                for (Object c: source) {
                    T obj = copy(c, targetClass);
                    target.add(obj);
                }
            }
        }
        return target;
    }

    /**
     * 拷贝单个
     * @param source 源对象
     * @param targetClass 目标类的class对象
     * @param <T>
     * @return 目标对象
     */
    public static <T> T copy(Object source, Class<T> targetClass) {
        if (Objects.isNull(source)) {
            return null;
        }
        T obj = null;
        try {
            obj = targetClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(source, obj);
        return obj;
    }

    /**
     * @param source 源对象
     * @param target 目标对象
     * @return
     * @throws Exception
     *
     * <p>用来复制对象属性 </p>
     * source对象不为null的属性会覆盖target对象的值，
     * 如果source对象为null的属性不会覆盖target里的对应属性值
     */
    public static void notNullCopy(Object source, Object target) throws IllegalArgumentException {
        //参数是否为空
        if (Objects.isNull(source)) {
            throw new IllegalArgumentException("Source must not be null");
        }
        if (Objects.isNull(target)) {
            throw new IllegalArgumentException("Target must not be null");
        }

        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        //获取source类的所有声明的属性全部
        Field[] sourceFields = sourceClass.getDeclaredFields();
        for (Field sField : sourceFields) {
            Field tgField = null;
            try {
                //如果target类没有该属性会抛一个异常，这里要进行捕获
                tgField = targetClass.getDeclaredField(sField.getName());
            } catch (NoSuchFieldException e) {

            }
            //如果存在
            if (tgField != null) {
                //如果该属性是私有的，允许反射访问
                if (!Modifier.isPublic(tgField.getModifiers())) {
                    tgField.setAccessible(true);
                }
                if (!Modifier.isPublic(sField.getModifiers())) {
                    sField.setAccessible(true);
                }
                //如果属性类型一样
                if (tgField.getType().equals(sField.getType())) {
                    try {
                         Object value = sField.get(source);
                        //如果source对象该属性不为空
                         if (value != null) {
                            //给target对象对应属性赋值
                             tgField.set(target,value);
                        }
                    }catch (IllegalAccessException e){

                    }


                }
            }

        }

    }


}
