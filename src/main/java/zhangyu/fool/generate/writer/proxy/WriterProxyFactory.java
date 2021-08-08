package zhangyu.fool.generate.writer.proxy;

import zhangyu.fool.generate.writer.Writer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

/**
 * @author xmz
 * @date: 2021/08/08
 */
public class WriterProxyFactory {

    public static Writer getWriterProxy(Writer codeWriter){
        WriterProxyHandler handler = new WriterProxyHandler(codeWriter);
        return (Writer) Proxy.newProxyInstance(codeWriter.getClass().getClassLoader(),
                codeWriter.getClass().getSuperclass().getInterfaces(), handler);
    }

    /**
     * 获取代理对象写法2
     * @param codeWriter
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public static Writer getWriterProxy2(Writer codeWriter) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        WriterProxyHandler handler = new WriterProxyHandler(codeWriter);
        //生成代理类型
        Class<?> proxyClass = Proxy.getProxyClass(codeWriter.getClass().getClassLoader(), new Class<?>[]{Writer.class});
        //生成代理对象
        Object object = proxyClass.getConstructor(new Class[]{InvocationHandler.class}).newInstance(new Object[]{handler});
       return (Writer) object;
    }

}
