package zhangyu.fool.generate.writer.vue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generate.model.Table;
import zhangyu.fool.generate.util.BuildPath;
import zhangyu.fool.generate.util.FileUtil;
import zhangyu.fool.generate.util.XmlUtil;
import zhangyu.fool.generate.writer.AbstractCodeWriter;
import zhangyu.fool.generate.writer.Writer;
import zhangyu.fool.generate.writer.model.ProjectConfig;
import zhangyu.fool.generate.writer.model.param.CommonParam;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomingzhang
 * @date 2021/07/30
 */
public class VueProjectWriter extends AbstractCodeWriter {

    private final static Logger log = LoggerFactory.getLogger(VueProjectWriter.class);
    /**
     * vue模板位置
     */
    private String VUE_TEMPLATE_PATH = BuildPath.buildDir(TEMPLATE_BASE_PATH, "web");
    /**
     * vue根目录
     */
    private String VUE_ROOT_PATH = BuildPath.buildDir(XmlUtil.getText("projectPath"), "vue-" + XmlUtil.getText("artifactId"));

    private String VUE_PUBLIC_PATH = BuildPath.buildDir(VUE_ROOT_PATH, "public");

    private String VUE_SRC_PATH = BuildPath.buildDir(VUE_ROOT_PATH, "src");

    private String VUE_ROUTER_PATH = BuildPath.buildDir(VUE_SRC_PATH, "router");

    private String VUE_COMPONENTS_PATH = BuildPath.buildDir(VUE_SRC_PATH, "components");

    private String VUE_VIEWS_PATH = BuildPath.buildDir(VUE_SRC_PATH, "views");

    private String VUE_ASSETS_PATH = BuildPath.buildDir(VUE_SRC_PATH, "assets");

    public VueProjectWriter() {
        super(new ProjectConfig());
    }

    public VueProjectWriter(ProjectConfig projectConfig) {
        super(projectConfig);
    }


    public void write() {
        log.info("=======开始生成vue工程  begin======");

        // 1.创建工程基本目录
        initBaseDir();
        // 2.创建工程基础配置文件
        initBaseConfig();
        // 3.创建router.js
        createRouterJs(VUE_ROUTER_PATH);

        // 3.创建component组件
        createComponent();

        log.info("=======vue工程生成完成  end======");
    }


    private void createComponent() {
        new ViewWriter().write(VUE_VIEWS_PATH);
    }


    private void initBaseConfig() {

        // babel.config.js
        writeByTemplate(VUE_TEMPLATE_PATH, "babel.config",
                VUE_ROOT_PATH + File.separator + "babel.config.js", null);

        // package.json
        writeByTemplate(VUE_TEMPLATE_PATH, "package",
                VUE_ROOT_PATH + File.separator + "package.json", null);

        // package-lock.json
/*
        writeByTemplate(VUE_TEMPLATE_PATH, "package-lock",
                VUE_ROOT_PATH + File.separator + "package-lock.json", null);
*/

        // vue.config.js
        writeByTemplate(VUE_TEMPLATE_PATH, "vue.config",
                VUE_ROOT_PATH + File.separator + "vue.config.js", null);

        // main.js
        writeByTemplate(VUE_TEMPLATE_PATH, "main",
                VUE_SRC_PATH + File.separator + "main.js", null);
        // App.vue
        createAppJS(VUE_SRC_PATH);

        // index.html
        writeByTemplate(VUE_TEMPLATE_PATH, "index",
                VUE_PUBLIC_PATH + File.separator + "index.html", null);

    }

    private void createAppJS(String destPath) {
        FileUtil.mkdirs(destPath);
        List<Table> tableList = XmlUtil.getTableList();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tableList", tableList);
        writeByTemplate(VUE_TEMPLATE_PATH, "App",
                destPath + File.separator + "App.vue", paramMap);
    }

    private void createRouterJs(String destPath) {
        FileUtil.mkdirs(destPath);
        List<Table> tableList = XmlUtil.getTableList();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tableList", tableList);
        writeByTemplate(VUE_TEMPLATE_PATH + File.separator + "router", "index",
                destPath + File.separator + "index.js", paramMap);
    }

    private void initBaseDir() {
        /* 创建vue基本目录 */
        FileUtil.mkdirs(VUE_ROOT_PATH);
        FileUtil.mkdirs(VUE_SRC_PATH);
        FileUtil.mkdirs(VUE_PUBLIC_PATH);
        FileUtil.mkdirs(VUE_COMPONENTS_PATH);
        FileUtil.mkdirs(VUE_VIEWS_PATH);
        FileUtil.mkdirs(VUE_ASSETS_PATH);
    }


    @Override
    public void write(String destPath) {

        VUE_ROOT_PATH = BuildPath.buildDir(destPath, "vue-" + XmlUtil.getText("artifactId"));

        VUE_PUBLIC_PATH = BuildPath.buildDir(VUE_ROOT_PATH, "public");

        VUE_SRC_PATH = BuildPath.buildDir(VUE_ROOT_PATH, "src");

        VUE_ROUTER_PATH = BuildPath.buildDir(VUE_SRC_PATH, "router");

        VUE_COMPONENTS_PATH = BuildPath.buildDir(VUE_SRC_PATH, "components");

        VUE_VIEWS_PATH = BuildPath.buildDir(VUE_SRC_PATH, "views");

        VUE_ASSETS_PATH = BuildPath.buildDir(VUE_SRC_PATH, "assets");

        this.write();
    }

    @Override
    public CommonParam buildParam(String tableName, String entityName) {
        return null;
    }

    @Override
    public void write(String destPath, String templateName) {
    }

}
