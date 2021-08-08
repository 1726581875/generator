package zhangyu.fool.generate.writer.proxy;

import zhangyu.fool.generate.writer.CodeWriter;
import zhangyu.fool.generate.writer.builder.WriterBuilderFactory;
import zhangyu.fool.generate.writer.java.EntityWriter;
import zhangyu.fool.generate.writer.model.ProjectConfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

/**
 * @author xmz
 * @date: 2021/08/08
 */
public class WriterProxyFactory {

    public static CodeWriter getWriterProxy(CodeWriter codeWriter){
        WriterProxyHandler handler = new WriterProxyHandler(codeWriter);
        return (CodeWriter) Proxy.newProxyInstance(codeWriter.getClass().getClassLoader(),
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
    public static CodeWriter getWriterProxy2(CodeWriter codeWriter) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        WriterProxyHandler handler = new WriterProxyHandler(codeWriter);
        //生成代理类型
        Class<?> proxyClass = Proxy.getProxyClass(codeWriter.getClass().getClassLoader(), new Class<?>[]{CodeWriter.class});
        //生成代理对象
        Object object = proxyClass.getConstructor(new Class[]{InvocationHandler.class}).newInstance(new Object[]{handler});
       return (CodeWriter) object;
    }

}
