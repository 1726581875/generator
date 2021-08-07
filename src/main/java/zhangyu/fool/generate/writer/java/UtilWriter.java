package zhangyu.fool.generate.writer.java;

import zhangyu.fool.generate.model.Author;
import zhangyu.fool.generate.util.BuildPath;
import zhangyu.fool.generate.util.NameConvertUtil;
import zhangyu.fool.generate.writer.AbstractCodeWriter;
import zhangyu.fool.generate.writer.annotation.Writer;
import zhangyu.fool.generate.writer.enums.WriterEnum;
import zhangyu.fool.generate.writer.model.ProjectConfig;
import zhangyu.fool.generate.writer.model.param.CommonParam;

import java.io.File;

/**
 * @author xiaomingzhang
 * @date 2021/6/8
 */
@Writer(type = WriterEnum.UTIL)
public class UtilWriter extends AbstractCodeWriter {

	public static final String UTIL_TEMPLATE_PATH = BuildPath.buildDir(TEMPLATE_BASE_PATH, "util");
	
	public static final String COPY_UTIL_TEMPLATE_NAME = "copy_util";

	public UtilWriter() {
		super(new ProjectConfig());
	}

	public UtilWriter(ProjectConfig projectConfig) {
		super(projectConfig);
	}


	@Override
	public void write(String destPath) {
		write(destPath, null);
	}

	@Override
	public CommonParam buildParam(String tableName, String entityName) {
		CommonParam commonParam = new CommonParam();
		commonParam.setBasePackageName(NameConvertUtil.getPackageName("utilPackage"));
		commonParam.setAuthor(Author.build());
		return commonParam;
	}


	@Override
	public void write(String destPath, String templateName) {
		log.info("===开始创建Util工具类  begin===");
		createCopyUtil(destPath);
		log.info("===Util工具类创建完成  end===");
	}

	/**
	 *
	 * @param destPath
	 */
	private void createCopyUtil(String destPath) {
		checkAndCreateDir(destPath);
		CommonParam commonParam = this.buildParam(null, null);
		String fullPath = destPath + File.separator +"CopyUtil.java";
		this.writeByParam(UTIL_TEMPLATE_PATH, COPY_UTIL_TEMPLATE_NAME, fullPath, commonParam);
		log.info("已创建 [CopyUtil.java]");
	}


}