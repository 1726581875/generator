package zhangyu.fool.generator.main.proxy;

import zhangyu.fool.generator.util.FileUtil;
import zhangyu.fool.generator.main.writer.Writer;
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

    private Writer codeWriter;

    public WriterProxyHandler(Writer codeWriter) {
        this.codeWriter = codeWriter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        zhangyu.fool.generator.main.annotation.Writer annotation = codeWriter.getClass().getDeclaredAnnotation(zhangyu.fool.generator.main.annotation.Writer.class);
        //调用目标方法前，检查目录是否存在
        if ("write".equals(method.getName())) {
            FileUtil.mkdirs(String.valueOf(args[0]));
            if (annotation != null) {
                Writer.log.debug("开始生成>>{}", annotation.type().getValue());
            }
        }
        //执行代理对象目标方法
        Object result = method.invoke(codeWriter, args);
        //执行目标方法后输出日志
        if ("write".equals(method.getName()) && annotation != null) {
            Writer.log.debug("生成>>{} 结束", annotation.type().getValue());
        }

        return result;
    }


    public static void main(String[] args) {
        Writer codeWriter = WriterBuilderFactory.toGetBuilder(EntityWriter.class)
                .build(ProjectConfig.buildJpa());
        Writer codeWriterProxy = WriterProxyFactory.getWriterProxy(codeWriter);
        codeWriterProxy.write("D:\\data\\test");
    }

}
