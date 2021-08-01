package zhangyu.fool.generate.writer.vue;

import zhangyu.fool.generate.util.BuildPath;
import zhangyu.fool.generate.writer.CodeWriter;

/**
 * @author xmz
 * @date: 2020/10/11
 */
public abstract class WebWriter implements CodeWriter {

    // 前端模板的根路径
    protected final String WEB_TEMPLATE_PATH =  BuildPath.buildDir(TEMPLATE_BASE_PATH,"web");
    // view模板位置
    protected final String VIEW_TEMPLATE_PATH = BuildPath.buildDir(WEB_TEMPLATE_PATH,"views");


}
