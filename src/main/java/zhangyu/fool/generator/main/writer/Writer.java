package zhangyu.fool.generator.main.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * writer接口
 * @author xmz
 * @date 2020/9/18
 */
public interface Writer {
	/**
	 * 模板基础路径
 	 */
	String TEMPLATE_BASE_PATH = "src/main/resources/fool/template/";
	
	Logger log = LoggerFactory.getLogger(Writer.class);

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


}
