package zhangyu.fool.generate.writer;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generate.constant.SuffixConstant;
import zhangyu.fool.generate.model.Author;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import java.util.Objects;

/**
 * writer接口
 * @author xmz
 * @date 2020/9/18
 */
public interface CodeWriter {
	/**
	 * 模板基础路径
 	 */
	String TEMPLATE_BASE_PATH = "src/main/resources/fool/template/";
	
	Logger log = LoggerFactory.getLogger(CodeWriter.class);


	/**
	 * 默认方法，使用默认模板
	 * @param destPath
	 */
	void write(String destPath);
	/**
	 * 生成文件的的方法
	 * @param destPath 目标路径
	 * @param templateName 使用的模板名
	 */
	void write(String destPath, String templateName);

	/**
	 * 根据模板生成文件
	 * @param templatePath 模板路径
	 * @param templateName 使用模板名称
	 * @param destFullPath 生成文件全路径（路径 + 文件名）
	 * @param paramMap 参数map
	 */
	default void writeByTemplate(String templatePath, String templateName, String destFullPath, Map<String, Object> paramMap) {
		File templateDir = new File(templatePath);
		if (!templateDir.exists()) {
			throw new IllegalArgumentException("模板路径[" + templateDir.getAbsolutePath() +"，必须提前创建");
		}
		//设置作者注释
		if(Objects.nonNull(paramMap)) {
			paramMap.put("Author", Author.AUTHOR);
		}
		try (FileWriter fileWriter = new FileWriter(destFullPath);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
			// 读模板
			Configuration config = new Configuration(Configuration.VERSION_2_3_29);
			config.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_29));
			config.setDirectoryForTemplateLoading(templateDir);
			Template template = config.getTemplate(templateName + SuffixConstant.FTL_SUFFIX);
			// 写
			template.process(paramMap, bufferedWriter);
			bufferedWriter.flush();
		} catch (Exception e) {
			log.error("====加载模板{} 生成代码失败，发生异常====", templateName, e);
		}
	}
	
   /**
    * 如果目录不存在则创建目录	
    * @param destPath
    */
	default void checkAndCreateDir(String destPath) {
		File destDir = new File(destPath);
		if (!destDir.exists()) {
			try {
				log.warn("目录{} 不存在", destPath);
				destDir.mkdirs();
				log.info("已创建目录{} ", destPath);
			}catch (Exception e){
				log.error("创建目录{} 出现异常",destPath,e);
			}
		}		
	}
	
	/**
	 * 创建一个模板工厂
	 * @param clazz 工厂的class对象
	 * @return
	 */
	static CodeWriter build(Class<?> clazz){
		CodeWriter tempFactory = null;
		try {
			tempFactory = (CodeWriter) clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {			
			log.error("反射创建工厂对象发生异常：",e);
		}
		return tempFactory;		
	}
	

}
