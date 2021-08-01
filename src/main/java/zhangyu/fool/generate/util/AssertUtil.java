package zhangyu.fool.generate.util;

/**
 * @author xiaomingzhang
 * @date 2021/7/26
 */
public class AssertUtil {

    public static void allNotNull(String message, Object... objects) {
        for (Object object : objects){
            if (object == null) {
                throw new RuntimeException(message);
            }
        }
    }

}
