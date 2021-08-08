package zhangyu.fool.generate.demo.poi.word;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author xmz
 * @date: 2021/08/08
 * 根据模板写
 */
public class DOCWriter {
    public static void searchAndReplace(String srcPath, String destPath,
                                        Map<String, String> map) {
        try {
            XWPFDocument document = new XWPFDocument(POIXMLDocument
                    .openPackage(srcPath));
            Iterator it = document.getTablesIterator();

            while (it.hasNext()) {
                XWPFTable table = (XWPFTable) it.next();
                int rcount = table.getNumberOfRows();
                for (int i = 0; i < rcount; i++) {
                    XWPFTableRow row = table.getRow(i);
                    List<XWPFTableCell> cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {
                        for (Map.Entry<String, String> e : map.entrySet()) {
                            if (cell.getText().equals(e.getKey())) {
                                cell.removeParagraph(0);
                                cell.setText(e.getValue());
                            }
                        }
                    }
                }
            }

            XWPFTable table = document.createTable();

            List<XWPFTable> tables = document.getTables();
            document.setTable(1,tables.get(0));

            FileOutputStream outStream = null;
            outStream = new FileOutputStream(destPath);
            document.write(outStream);
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        HashMap map = new HashMap();
        map.put("${name}", "王五");
        map.put("${tel}", "8886666");
        String srcPath = "D:\\data\\test\\1.docx";
        String destPath = "D:\\data\\test\\2.docx";
        searchAndReplace(srcPath, destPath, map);
    }
}
