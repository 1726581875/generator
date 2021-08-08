package zhangyu.fool.generate.util;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author xiaomingzhang
 * @date 2021/7/23
 */
public class ObjectToMapUtil {

    /**
     * 对象属性转map
     * @param objs
     * @return
     */
    public static Map<String,Object> toMap(Object... objs){
        final Map<String,Object> map = new HashMap<>(16);
        for (Object o: objs) {
            if(o == null) {
                continue;
            }
            if(o instanceof Map){
                map.putAll((Map)o);
                continue;
            }
            Class<?> aClass = o.getClass();
            List<Field> fieldList = new ArrayList<>();
            //父类的属性也需要添加
            while (aClass != null){
                fieldList.addAll(Arrays.asList(aClass.getDeclaredFields()));
                aClass = aClass.getSuperclass();
            }

            if(fieldList.size() == 0){
                continue;
            }
            for (Field field : fieldList) {
                //如果该属性是私有的，允许反射访问
                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }
                try {
                    String name = field.getName();
                    Object value = field.get(o);
                    //如果属性是map，会一并把其所有值加入到新map（仅仅处理对象里第一层，嵌套不管）
                    if(value instanceof Map){
                        map.putAll((Map)value);
                        continue;
                    }
                    if(value != null) {
                        map.put(name, value);
                    }
                } catch (IllegalAccessException e) {

                }
            }
        }
        return map;
    }

    public static void main(String[] args) {
        Map<String,Object> paramMap = new HashMap<>(3);
        paramMap.put("zzz",12566L);
        paramMap.put("xxx",new Date());
        paramMap.put("mmm","MMM");
        Map<String, Object> map = ObjectToMapUtil.toMap(paramMap);
        map.forEach((k,v) -> System.out.println("k=" + k + " ,v=" + v));
    }

}
