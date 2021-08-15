package zhangyu.fool.generator.main.writer;

import zhangyu.fool.generator.main.model.ProjectConfig;
import zhangyu.fool.generator.main.model.param.CommonParam;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author xmz
 * @date: 2021/08/15
 */
public class CommonWriter extends AbstractCodeWriter {

    private static final String TEMPLATE_NAME = "common";

    protected CommonWriter() {
        super(new ProjectConfig());
    }

    @Override
    public CommonParam buildParam(String tableName, String entityName) {
        return null;
    }

    @Override
    public void write(String destPath) {

    }

    /**
     * @param destPath
     * @param templatePath
     */
    public void write(String destPath, String templatePath, Map<String, Object> paramMap) {
        Map<String, Object> contentMap = new HashMap<>(1);
        contentMap.put("content", this.getFileContent(templatePath, paramMap));
        this.writeByTemplate(TEMPLATE_BASE_PATH, TEMPLATE_NAME, destPath, contentMap);
    }

    public String getFileContent(String templatePath, Map<String, Object> paramMap) {
        StringBuilder content = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(templatePath);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            String rowText = "";
            while ((rowText = br.readLine()) != null) {
                if (rowText.contains("$")) {
                    StringBuilder str = new StringBuilder();
                    char[] chars = rowText.toCharArray();
                    int index = 0;
                    boolean needReplace = false;
                    for (int i = 0; i < chars.length; i++) {
                        if (chars[i] == '$') {
                            needReplace = true;
                            str.append(Arrays.copyOfRange(chars, index, i));
                            index = i;
                        }
                        if (chars[i] == '}' && needReplace) {
                            needReplace = false;
                            String key = Arrays.copyOfRange(chars, index + 1, i).toString();
                            String value = paramMap.getOrDefault(key, "null").toString();
                            str.append(value);
                            index = i;
                        }
                        if (i == chars.length - 1) {
                            str.append(Arrays.copyOfRange(chars, index, i));
                            str.append("\n");
                        }

                    }
                }
            }

        } catch (Exception e) {
            log.error("", e);
        }
        return content.toString();
    }

}
