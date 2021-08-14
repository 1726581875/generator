package zhangyu.fool.generator.main.writer.java;

import zhangyu.fool.generator.enums.ProjectEnum;
import zhangyu.fool.generator.model.Author;
import zhangyu.fool.generator.util.FileUtil;
import zhangyu.fool.generator.util.NameConvertUtil;
import zhangyu.fool.generator.main.writer.AbstractCodeWriter;
import zhangyu.fool.generator.main.annotation.Writer;
import zhangyu.fool.generator.main.enums.TypeSuffixEnum;
import zhangyu.fool.generator.main.enums.WriterEnum;
import zhangyu.fool.generator.main.model.ProjectConfig;
import zhangyu.fool.generator.main.model.param.CommonParam;
import zhangyu.fool.generator.main.model.param.TestParam;

import java.io.File;

/**
 * @author xiaomingzhang
 * @date 2021/6/8
 */
@Writer(type = WriterEnum.TEST)
public class TestWriter extends AbstractCodeWriter {

	private final String TEST_TEMPLATE_PATH = TEMPLATE_BASE_PATH + "/test";

	public TestWriter() {
		super(new ProjectConfig());
	}

	public TestWriter(ProjectConfig projectConfig) {
		super(projectConfig);
	}



	@Override
	public void write(String destPath) {
		write(destPath, null);
	}

	@Override
	public CommonParam buildParam(String tableName, String entityName) {
		TestParam testParam = new TestParam();
		this.buildBaseParam(testParam);
		testParam.setEntityName(entityName);
		if(entityName != null) {
			testParam.setEntityNameLow(NameConvertUtil.bigHumpToHump(entityName));
		}
		return testParam;
	}

	@Override
	public void write(String destPath, String templateName) {

		createBaseMvcTest(destPath, "base_mvc_test");

		createControllerTest(destPath + "/controller", "controller_test");
	}

	/**
	 * 生成BaseMvcTest.java
	 * @param destPath
	 * @param templateName
	 */
	private void createBaseMvcTest(String destPath, String templateName) {
		FileUtil.checkAndCreateDir(destPath);
		// 目标文件全路径
		String fullPath = destPath + "/BaseMvcTest.java";
		CommonParam commonParam = this.buildParam(null, null);
		this.writeByParam(TEST_TEMPLATE_PATH, templateName, fullPath, commonParam);
		log.info("已创建 [BaseMvcTest.java]");

	}

	/**
	 * 生成controller层测试类
	 * @param destPath
	 * @param templateName
	 */
	private void createControllerTest(String destPath, String templateName) {
		WriteConfig writeConfig = new WriteConfig();
		writeConfig.setDestPath(destPath);
		writeConfig.setTemplateName(templateName);
		writeConfig.setTemplatePath(TEST_TEMPLATE_PATH);
		writeConfig.setTypeSuffixEnum(TypeSuffixEnum.CONTROLLER_TEST);
		this.forEachWrite(writeConfig);
	}



}
