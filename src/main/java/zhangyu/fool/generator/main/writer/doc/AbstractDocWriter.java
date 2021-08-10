package zhangyu.fool.generator.main.writer.doc;

import zhangyu.fool.generator.main.model.ProjectConfig;
import zhangyu.fool.generator.main.model.param.CommonParam;
import zhangyu.fool.generator.main.writer.AbstractCodeWriter;

/**
 * @author xiaomingzhang
 * @date 2021/8/9
 */
public abstract class AbstractDocWriter extends AbstractCodeWriter {

    protected AbstractDocWriter() {
        super(null);
    }


    @Override
    public CommonParam buildParam(String tableName, String entityName) {
        return null;
    }
}
