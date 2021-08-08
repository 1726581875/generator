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
