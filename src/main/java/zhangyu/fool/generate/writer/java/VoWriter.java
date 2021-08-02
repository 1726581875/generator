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
@Writer(type = WriterEnum.VO)
public class VoWriter extends AbstractCodeWriter {

	public static final String VO_TEMPLATE_PATH = BuildPath.buildDir(TEMPLATE_BASE_PATH, "vo");

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
		commonParam.setIsMyBatis(projectConfig.isUseMyBatis());
		commonParam.setIsLombok(projectConfig.isUseLombok());
		commonParam.setIsJpa(projectConfig.isUseJpa());
		commonParam.setIsMyBatisPlus(projectConfig.isUseMyBatisPlus());
		commonParam.setBasePackageName(NameConvertUtil.getPackageName("voPackage"));
		commonParam.setAuthor(Author.build());
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
