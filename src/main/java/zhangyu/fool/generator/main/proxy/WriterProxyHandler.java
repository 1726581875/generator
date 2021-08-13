package zhangyu.fool.generator.main.proxy;

import zhangyu.fool.generator.main.annotation.Writer;
import zhangyu.fool.generator.util.FileUtil;
import zhangyu.fool.generator.main.writer.FoolWriter;
import zhangyu.fool.generator.main.builder.WriterBuilderFactory;
import zhangyu.fool.generator.main.writer.java.EntityWriter;
import zhangyu.fool.generator.main.model.ProjectConfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author xmz
 * @date: 2021/08/08
 */
public class WriterProxyHandler implements InvocationHandler {

    private static final String PROXY_METHOD = "write";

    private FoolWriter codeWriter;

    public WriterProxyHandler(FoolWriter codeWriter) {
        this.codeWriter = codeWriter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Writer annotation = codeWriter.getClass().getDeclaredAnnotation(Writer.class);
        //调用目标方法前，检查目录是否存在
        if (PROXY_METHOD.equals(method.getName())) {
            FileUtil.mkdirs(String.valueOf(args[0]));
            if (annotation != null) {
                FoolWriter.log.debug("开始生成>>{}", annotation.type().getValue());
            }
        }
        //执行代理对象目标方法
        Object result = method.invoke(codeWriter, args);
        //执行目标方法后输出日志
        if (PROXY_METHOD.equals(method.getName()) && annotation != null) {
            FoolWriter.log.debug("生成>>{} 结束", annotation.type().getValue());
        }

        return result;
    }


    public static void main(String[] args) {
        FoolWriter codeWriter = WriterBuilderFactory.toGetBuilder(EntityWriter.class)
                .build(ProjectConfig.buildJpa());
        FoolWriter codeWriterProxy = WriterProxyFactory.getWriterProxy(codeWriter);
        codeWriterProxy.write("D:\\data\\test");
    }

}
