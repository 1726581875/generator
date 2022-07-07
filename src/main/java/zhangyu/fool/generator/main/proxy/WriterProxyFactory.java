package zhangyu.fool.generator.main.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generator.main.writer.FoolWriter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author xmz
 * @date: 2021/08/08
 * 代理工厂，生成代理对象
 */
public class WriterProxyFactory {

    private static final Logger log= LoggerFactory.getLogger(WriterProxyFactory.class);


    public static FoolWriter getWriterProxy(FoolWriter writer) {

        WriterProxyHandler handler = new WriterProxyHandler(writer);

        Class<?>[] interfaces = getInterfaces(writer.getClass());

        return (FoolWriter) Proxy.newProxyInstance(writer.getClass().getClassLoader(), interfaces, handler);
    }

    private static Class<?>[] getInterfaces(Class<?> clazz){
        if(clazz == null){
            return null;
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        if(interfaces.length == 0) {
            return getInterfaces(clazz.getSuperclass());
        }
        return interfaces;
    }

    /**
     * 获取代理对象写法2
     * @param writer
     * @return
     */
    public static FoolWriter getWriterProxy2(FoolWriter writer) {
        WriterProxyHandler handler = new WriterProxyHandler(writer);
        //生成代理类型
        Class<?> proxyClass = Proxy.getProxyClass(writer.getClass().getClassLoader(), new Class<?>[]{FoolWriter.class});
        //生成代理对象
        Object object = null;
        try {
            object = proxyClass.getConstructor(new Class[]{InvocationHandler.class}).newInstance(new Object[]{handler});
        } catch (Exception e) {
            log.error("获取代理对象发生异常",e);
        }
        return (FoolWriter) object;
    }

}
