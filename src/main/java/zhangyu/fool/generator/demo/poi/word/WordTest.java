package zhangyu.fool.generator.demo.poi.word;

import zhangyu.fool.generator.service.DatabaseService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xmz
 * @date: 2021/08/08
 * 根据模板生成word
 * 源码出处：https://www.cnblogs.com/Juveniless/p/11241431.html
 */
public class WordTest {


    public static void main(String[] args) throws IOException {
        // 存储报表全部数据
        Map<String, Object> wordDataMap = new HashMap<>();
        // 存储报表中不循环的数据
        Map<String, Object> parametersMap = new HashMap<>();

        Map<String, String> tableNameMap = DatabaseService.getTableNameMap();
        List<Map<String, Object>> table1 = new ArrayList<>();
        tableNameMap.keySet().forEach(tableName -> {
            Map<String, Object> map = new HashMap<>();
            map.put("tableName", tableName);
            table1.add(map);
        });

        wordDataMap.put("table1", table1);
        wordDataMap.put("parametersMap", parametersMap);
        File file = new File("D:\\data\\test\\database.docx");//改成你本地文件所在目录


        // 读取word模板
        FileInputStream fileInputStream = new FileInputStream(file);
        WordTemplate template = new WordTemplate(fileInputStream);

        // 替换数据
        template.replaceDocument(wordDataMap);


        //生成文件
        File outputFile=new File("D:\\data\\test\\database2.docx");//改成你本地文件所在目录
        FileOutputStream fos  = new FileOutputStream(outputFile);
        template.getDocument().write(fos);

    }

}
