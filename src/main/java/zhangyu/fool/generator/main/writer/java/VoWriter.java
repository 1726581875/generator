package zhangyu.fool.generator.main.writer.java;

import zhangyu.fool.generator.model.Author;
import zhangyu.fool.generator.util.NameConvertUtil;
import zhangyu.fool.generator.main.writer.AbstractCodeWriter;
import zhangyu.fool.generator.main.annotation.Writer;
import zhangyu.fool.generator.main.enums.WriterEnum;
import zhangyu.fool.generator.main.model.ProjectConfig;
import zhangyu.fool.generator.main.model.param.CommonParam;

import java.io.File;

/**
 * @author xiaomingzhang
 * @date 2021/6/8
 */
@Writer(type = WriterEnum.VO)
public class VoWriter extends AbstractCodeWriter {

	public static final String VO_TEMPLATE_PATH = TEMPLATE_BASE_PATH + "/model";

	public static final String RESULT_TEMPLATE_NAME = "resp_result";
	
	public static final String PAGE_VO_TEMPLATE_NAME = "page_vo";


	public VoWriter(ProjectConfig projectConfig) {
		super(projectConfig);
	}


	@Override
	public void write(String destPath) {
		log.info("===开始创建VO类  begin===");
		createRespResult(destPath);
		createPageVO(destPath);
		log.info("===VO类创建完成  den===");
	}

	@Override
	public CommonParam buildParam(String tableName, String entityName) {
		CommonParam commonParam = new CommonParam();
		this.buildBaseParam(commonParam);
		return commonParam;
	}

	private void createPageVO(String destPath) {
		String fullPath = destPath + File.separator+ "PageVO.java";
		CommonParam commonParam = this.buildParam(null, null);
		this.writeByParam(VO_TEMPLATE_PATH, PAGE_VO_TEMPLATE_NAME, fullPath, commonParam);
		log.info("已创建 [PageVO.java]");
	}

	private void createRespResult(String destPath) {
		String fullPath = destPath + File.separator +"RespResult.java";
		CommonParam commonParam = this.buildParam(null, null);
		this.writeByParam(VO_TEMPLATE_PATH, RESULT_TEMPLATE_NAME, fullPath, commonParam);
		log.info("已创建 [RespResult.java]");
	}

	@Override
	public void write(String destPath, String templateName) {
		log.info("===开始创建VO类  begin===");
		createRespResult(destPath);
		createPageVO(destPath);
		log.info("===VO类创建完成  den===");
	}

}
