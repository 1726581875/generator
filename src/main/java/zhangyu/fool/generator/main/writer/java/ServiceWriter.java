package zhangyu.fool.generator.main.writer.java;

import zhangyu.fool.generator.model.Author;
import zhangyu.fool.generator.service.DatabaseService;
import zhangyu.fool.generator.util.NameConvertUtil;
import zhangyu.fool.generator.main.writer.AbstractCodeWriter;
import zhangyu.fool.generator.main.annotation.Writer;
import zhangyu.fool.generator.main.enums.TypeSuffixEnum;
import zhangyu.fool.generator.main.enums.WriterEnum;
import zhangyu.fool.generator.main.model.ProjectConfig;
import zhangyu.fool.generator.main.model.param.CommonParam;
import zhangyu.fool.generator.main.model.param.ServiceParam;

import java.io.File;

/**
 * @author xiaomingzhang
 * @date 2021/6/8
 */
@Writer(type = WriterEnum.SERVICE)
public class ServiceWriter extends AbstractCodeWriter {

	public static final String SERVICE_TEMPLATE_PATH = TEMPLATE_BASE_PATH + "/service";

	public static final String SERVICE = "Service";

	public static final String SERVICE_IMPL = "ServiceImpl";

	public ServiceWriter(ProjectConfig projectConfig) {
		super(projectConfig);
	}

	@Override
	public CommonParam buildParam(String tableName, String entityName) {
		ServiceParam serviceParam = new ServiceParam();
		this.buildBaseParam(serviceParam);
		serviceParam.setEntityKey(DatabaseService.getPrimaryName(tableName));
		serviceParam.setEntityName(entityName);
		serviceParam.setEntityNameLow(NameConvertUtil.bigHumpToHump(entityName));
		serviceParam.setKeyType(DatabaseService.getPrimaryType(tableName));
		return serviceParam;
	}


	@Override
	public void write(String destPath) {
		write(destPath, null);
	}

	@Override
	public void write(String destPath, String templateName) {
		WriteConfig writeConfig = new WriteConfig();
		writeConfig.setDestPath(destPath);
		writeConfig.setTemplatePath(SERVICE_TEMPLATE_PATH);
		//生成service接口类
		writeConfig.setTemplateName(SERVICE);
		writeConfig.setTypeSuffixEnum(TypeSuffixEnum.SERVICE);
		this.forEachWrite(writeConfig);
		//生成service实现类
		writeConfig.setTypeSuffixEnum(TypeSuffixEnum.SERVICE_IMPL);
		writeConfig.setDestPath(destPath + File.separator + "impl");
		writeConfig.setTemplateName(SERVICE_IMPL);
		this.forEachWrite(writeConfig);

	}

}
