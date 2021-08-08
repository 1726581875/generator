package zhangyu.fool.generate.writer.proxy;

import zhangyu.fool.generate.util.FileUtil;
import zhangyu.fool.generate.writer.CodeWriter;
import zhangyu.fool.generate.writer.annotation.Writer;
import zhangyu.fool.generate.writer.builder.WriterBuilderFactory;
import zhangyu.fool.generate.writer.java.EntityWriter;
import zhangyu.fool.generate.writer.model.ProjectConfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author xmz
 * @date: 2021/08/08
 */
public class WriterProxyHandler implements InvocationHandler {

    private CodeWriter codeWriter;

    public WriterProxyHandler(CodeWriter codeWriter){
        this.codeWriter = codeWriter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Writer annotation = codeWriter.getClass().getDeclaredAnnotation(Writer.class);
        //调用目标方法前，检查目录是否存在
        if("write".equals(method.getName())){
            FileUtil.mkdirs(String.valueOf(args[0]));
            if(annotation != null) {
                CodeWriter.log.debug("开始生成>>{}", annotation.type().getValue());
            }
        }
        //执行代理对象目标方法
        Object result = method.invoke(codeWriter, args);
        //执行目标方法后输出日志
        if("write".equals(method.getName())){
            if(annotation != null) {
                CodeWriter.log.debug("生成>>{} 结束", annotation.type().getValue());
            }
        }

        return result;
    }


    public static void main(String[] args) {
        CodeWriter codeWriter = WriterBuilderFactory.toGetBuilder(EntityWriter.class)
                .build(ProjectConfig.buildJpa());
        CodeWriter codeWriterProxy = WriterProxyFactory.getWriterProxy(codeWriter);
                codeWriterProxy.write("D:\\data\\test");
    }

}
