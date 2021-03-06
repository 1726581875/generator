package zhangyu.fool.generator.main.writer.doc;

import org.apache.poi.xwpf.usermodel.*;
import zhangyu.fool.generator.dao.DatabaseDAO;
import zhangyu.fool.generator.main.annotation.Writer;
import zhangyu.fool.generator.main.enums.WriterEnum;
import zhangyu.fool.generator.model.mysql.TableColumn;
import zhangyu.fool.generator.model.mysql.TableInfo;
import zhangyu.fool.generator.service.DatabaseService;
import zhangyu.fool.generator.util.FileUtil;
import zhangyu.fool.generator.util.ObjectToMapUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2021/08/08
 * 生成mysql数据库说明文档docx
 */
@Writer(type = WriterEnum.SQL_DOCX)
public class SqlDocxWriter extends AbstractDocWriter {

    public static final String DOC_PATH = TEMPLATE_BASE_PATH + "/resources/doc/database.docx";

    public static void main(String[] args) {
        new SqlDocxWriter().write("D:\\data\\doc\\database3.docx");
    }


    @Override
    public void write(String destPath) {
        FileUtil.mkdirs(destPath);
        //读，模板位置
        String templatePath = DOC_PATH;
        //写，生成文件路径
        String destFullPath = destPath.contains(".docx") ? destPath : destPath + File.separator + "数据库说明文档.docx";

        Map<String, String> tableNameMap = DatabaseService.getTableNameMap();
        String databaseName = DatabaseDAO.getDatabaseName();
        String sql = TableInfo.getSQL(databaseName);
        List<TableInfo> tableInfList = DatabaseDAO.getList(sql,TableInfo.class);
        Map<String, String> tableCommentMap = tableInfList.stream().collect(Collectors.toMap(TableInfo::getTableName, TableInfo::getTableComment, (a1, a2) -> a1));
        // 读取word模板
        File file = new File(templatePath);
        try(FileInputStream fileInputStream = new FileInputStream(file);
            XWPFDocument document = new XWPFDocument(fileInputStream)) {
            tableNameMap.keySet().forEach(tableName -> foreachCreateTable(document, tableName, tableCommentMap));
            //移除模板表格
            document.removeBodyElement(2);
            document.removeBodyElement(2);
            //生成文件
            File outputFile = new File(destFullPath);
            FileOutputStream fos = new FileOutputStream(outputFile);
            document.write(fos);
        }catch (Exception e) {
            log.error("",e);
        }
        log.info("模板生成完成");
    }

    @Override
    public void write(String destPath, String templateName) {

    }


    public void foreachCreateTable(XWPFDocument document, String tableName, Map<String, String> tableCommentMap) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("tableName",tableName);
        paramMap.put("tableComment",tableCommentMap.get(tableName));
        List<Map<String, Object>> rowDataList = getRowList(tableName);

        //读模板,拿第一个表格
        XWPFTable templateTable = document.getTables().get(0);
        //读模板，拿第三个段落
        XWPFParagraph xwpfParagraph = document.getParagraphs().stream().filter(e -> e.getText().contains("{")).findFirst().get();
        this.addParagraphInDocFooter(document,xwpfParagraph,paramMap);


        // 创建新表格,默认一行一列
        XWPFTable newCreateTable = document.createTable();
        // 获取模板表格所有行
        List<XWPFTableRow> TempTableRows = templateTable.getRows();
        // 标签行index
        int tagRowsIndex = 0;
        for (int i = 0, size = TempTableRows.size(); i < size; i++) {
            // 获取到表格行的第一个单元格
            String rowText = TempTableRows.get(i).getCell(0).getText();
            if (rowText.indexOf("##{foreachRows}##") > -1) {
                tagRowsIndex = i;
                break;
            }
        }

        /* 复制模板行和标签行之前的行 */
        for (int i = 0; i < tagRowsIndex; i++) {
            XWPFTableRow newCreateRow = newCreateTable.createRow();
            // 复制行
            CopyTableRow(newCreateRow, TempTableRows.get(i));
            // 处理不循环标签的替换
            replaceTableRow(newCreateRow, paramMap);
        }

        /* 循环生成模板行 */
        XWPFTableRow tempRow = TempTableRows.get(tagRowsIndex + 1);// 获取到模板行
        for (int i = 0; i < rowDataList.size(); i++) {
            XWPFTableRow newCreateRow = newCreateTable.createRow();
            // 复制模板行
            CopyTableRow(newCreateRow, tempRow);
            // 处理标签替换
            replaceTableRow(newCreateRow, rowDataList.get(i));
        }

        /* 复制模板行和标签行之后的行 */
        for (int i = tagRowsIndex + 2; i < TempTableRows.size(); i++) {
            XWPFTableRow newCreateRow = newCreateTable.createRow();
            CopyTableRow(newCreateRow, TempTableRows.get(i));// 复制行
            // 处理不循环标签的替换
            replaceTableRow(newCreateRow, paramMap);
        }
        // 移除多出来的第一行
        newCreateTable.removeRow(0);
        document.createParagraph();// 添加回车换行
    }

    List<Map<String, Object>> getRowList(String tableName) {
        String sql = TableColumn.getSQL(tableName);
        List<TableColumn> columnList = DatabaseDAO.getList(sql,TableColumn.class);
        return columnList.stream().map(ObjectToMapUtil::toMap).collect(Collectors.toList());
    }

    /**
     * 根据 模板段落 和 数据 在文档末尾生成段落
     *
     * @author Juveniless
     * @date 2017年11月27日 上午11:49:42
     * @param templateParagraph
     *            模板段落
     *            循环数据集
     * @param parametersMap
     *            不循环数据集
     *
     */
    public void addParagraphInDocFooter(XWPFDocument document, XWPFParagraph templateParagraph, Map<String, Object> parametersMap) {

            XWPFParagraph createParagraph = document.createParagraph();
            // 设置段落样式
            createParagraph.getCTP().setPPr(templateParagraph.getCTP().getPPr());
            // 移除原始内容
            for (int pos = 0; pos < createParagraph.getRuns().size(); pos++) {
                createParagraph.removeRun(pos);
            }
            // 添加Run标签
            for (XWPFRun s : templateParagraph.getRuns()) {
                XWPFRun targetrun = createParagraph.createRun();
                CopyRun(targetrun, s);
            }

            replaceParagraph(createParagraph, parametersMap);

    }


    /**
     * 根据参数parametersMap对表格的一行进行标签的替换
     *
     * @author Juveniless
     * @date 2017年11月23日 下午2:09:24
     * @param tableRow
     *            表格行
     * @param parametersMap
     *            参数map
     *
     */
    public void replaceTableRow(XWPFTableRow tableRow, Map<String, Object> parametersMap) {
        List<XWPFTableCell> tableCells = tableRow.getTableCells();
        for (XWPFTableCell xWPFTableCell : tableCells) {
            List<XWPFParagraph> paragraphs = xWPFTableCell.getParagraphs();
            for (XWPFParagraph xwpfParagraph : paragraphs) {
                replaceParagraph(xwpfParagraph, parametersMap);
            }
        }

    }

    /**
     * 根据map替换段落元素内的{**}标签
     * @author Juveniless
     * @date 2017年12月4日 下午3:09:00
     * @param xWPFParagraph
     * @param parametersMap
     *
     */
    public void replaceParagraph(XWPFParagraph xWPFParagraph, Map<String, Object> parametersMap) {
        List<XWPFRun> runs = xWPFParagraph.getRuns();
        String xWPFParagraphText = xWPFParagraph.getText();
        String regEx = "\\{.+?\\}";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(xWPFParagraphText);//正则匹配字符串{****}

        if (matcher.find()) {
            // 查找到有标签才执行替换
            int beginRunIndex = xWPFParagraph.searchText("{", new PositionInParagraph()).getBeginRun();// 标签开始run位置
            int endRunIndex = xWPFParagraph.searchText("}", new PositionInParagraph()).getEndRun();// 结束标签
            StringBuffer key = new StringBuffer();

            if (beginRunIndex == endRunIndex) {
                // {**}在一个run标签内
                XWPFRun beginRun = runs.get(beginRunIndex);
                String beginRunText = beginRun.text();

                int beginIndex = beginRunText.indexOf("{");
                int endIndex = beginRunText.indexOf("}");
                int length = beginRunText.length();

                if (beginIndex == 0 && endIndex == length - 1) {
                    // 该run标签只有{**}
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(beginRunIndex);
                    insertNewRun.getCTR().setRPr(beginRun.getCTR().getRPr());
                    // 设置文本
                    key.append(beginRunText.substring(1, endIndex));
                    insertNewRun.setText(getValueBykey(key.toString(),parametersMap));
                    xWPFParagraph.removeRun(beginRunIndex + 1);
                } else {
                    // 该run标签为**{**}** 或者 **{**} 或者{**}**，替换key后，还需要加上原始key前后的文本
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(beginRunIndex);
                    insertNewRun.getCTR().setRPr(beginRun.getCTR().getRPr());
                    // 设置文本
                    key.append(beginRunText.substring(beginRunText.indexOf("{")+1, beginRunText.indexOf("}")));
                    String textString=beginRunText.substring(0, beginIndex) + getValueBykey(key.toString(),parametersMap)
                            + beginRunText.substring(endIndex + 1);
                    insertNewRun.setText(textString);
                    xWPFParagraph.removeRun(beginRunIndex + 1);
                }

            }else {
                // {**}被分成多个run

                //先处理起始run标签,取得第一个{key}值
                XWPFRun beginRun = runs.get(beginRunIndex);
                String beginRunText = beginRun.text();
                int beginIndex = beginRunText.indexOf("{");
                if (beginRunText.length()>1  ) {
                    key.append(beginRunText.substring(beginIndex+1));
                }
                ArrayList<Integer> removeRunList = new ArrayList<>();//需要移除的run
                //处理中间的run
                for (int i = beginRunIndex + 1; i < endRunIndex; i++) {
                    XWPFRun run = runs.get(i);
                    String runText = run.text();
                    key.append(runText);
                    removeRunList.add(i);
                }

                // 获取endRun中的key值
                XWPFRun endRun = runs.get(endRunIndex);
                String endRunText = endRun.text();
                int endIndex = endRunText.indexOf("}");
                //run中**}或者**}**
                if (endRunText.length()>1 && endIndex!=0) {
                    key.append(endRunText.substring(0,endIndex));
                }



                //*******************************************************************
                //取得key值后替换标签

                //先处理开始标签
                if (beginRunText.length()==2 ) {
                    // run标签内文本{
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(beginRunIndex);
                    insertNewRun.getCTR().setRPr(beginRun.getCTR().getRPr());
                    // 设置文本
                    insertNewRun.setText(getValueBykey(key.toString(),parametersMap));
                    xWPFParagraph.removeRun(beginRunIndex + 1);//移除原始的run
                }else {
                    // 该run标签为**{**或者 {** ，替换key后，还需要加上原始key前的文本
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(beginRunIndex);
                    insertNewRun.getCTR().setRPr(beginRun.getCTR().getRPr());
                    // 设置文本
                    String textString=beginRunText.substring(0,beginRunText.indexOf("{"))+getValueBykey(key.toString(),parametersMap);
                    insertNewRun.setText(textString);
                    xWPFParagraph.removeRun(beginRunIndex + 1);//移除原始的run
                }

                //处理结束标签
                if (endRunText.length()==1 ) {
                    // run标签内文本只有}
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(endRunIndex);
                    insertNewRun.getCTR().setRPr(endRun.getCTR().getRPr());
                    // 设置文本
                    insertNewRun.setText("");
                    xWPFParagraph.removeRun(endRunIndex + 1);//移除原始的run

                }else {
                    // 该run标签为**}**或者 }** 或者**}，替换key后，还需要加上原始key后的文本
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(endRunIndex);
                    insertNewRun.getCTR().setRPr(endRun.getCTR().getRPr());
                    // 设置文本
                    String textString=endRunText.substring(endRunText.indexOf("}")+1);
                    insertNewRun.setText(textString);
                    xWPFParagraph.removeRun(endRunIndex + 1);//移除原始的run
                }

                //处理中间的run标签
                for (int i = 0; i < removeRunList.size(); i++) {
                    XWPFRun xWPFRun = runs.get(removeRunList.get(i));//原始run
                    XWPFRun insertNewRun = xWPFParagraph.insertNewRun(removeRunList.get(i));
                    insertNewRun.getCTR().setRPr(xWPFRun.getCTR().getRPr());
                    insertNewRun.setText("");
                    xWPFParagraph.removeRun(removeRunList.get(i) + 1);//移除原始的run
                }

            }// 处理${**}被分成多个run

            replaceParagraph( xWPFParagraph, parametersMap);

        }//if 有标签

    }



    private String getValueBykey(String key, Map<String, Object> map) {
        String returnValue="";
        if (key != null) {
            try {
                returnValue=map.get(key)!=null ? map.get(key).toString() : "";
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("key:"+key+"***"+e);
                returnValue="";
            }

        }
        return returnValue;
    }




    /**
     * 复制表格行XWPFTableRow格式
     *
     * @param target
     *            待修改格式的XWPFTableRow
     * @param source
     *            模板XWPFTableRow
     */
    private void CopyTableRow(XWPFTableRow target, XWPFTableRow source) {

        int tempRowCellsize = source.getTableCells().size();// 模板行的列数
        for (int i = 0; i < tempRowCellsize - 1; i++) {
            target.addNewTableCell();// 为新添加的行添加与模板表格对应行行相同个数的单元格
        }
        // 复制样式
        target.getCtRow().setTrPr(source.getCtRow().getTrPr());
        // 复制单元格
        for (int i = 0; i < target.getTableCells().size(); i++) {
            copyTableCell(target.getCell(i), source.getCell(i));
        }
    }

    /**
     * 复制单元格XWPFTableCell格式
     *
     * @author Juveniless
     * @date 2017年11月27日 下午3:41:02
     * @param newTableCell
     *            新创建的的单元格
     * @param templateTableCell
     *            模板单元格
     *
     */
    private void copyTableCell(XWPFTableCell newTableCell, XWPFTableCell templateTableCell) {
        // 列属性
        newTableCell.getCTTc().setTcPr(templateTableCell.getCTTc().getTcPr());
        // 删除目标 targetCell 所有文本段落
        for (int pos = 0; pos < newTableCell.getParagraphs().size(); pos++) {
            newTableCell.removeParagraph(pos);
        }
        // 添加新文本段落
        for (XWPFParagraph sp : templateTableCell.getParagraphs()) {
            XWPFParagraph targetP = newTableCell.addParagraph();
            copyParagraph(targetP, sp);
        }
    }


    /**
     * 复制文本段落XWPFParagraph格式
     *
     * @author Juveniless
     * @date 2017年11月27日 下午3:43:08
     * @param newParagraph
     *            新创建的的段落
     * @param templateParagraph
     *            模板段落
     *
     */
    private void copyParagraph(XWPFParagraph newParagraph, XWPFParagraph templateParagraph) {
        // 设置段落样式
        newParagraph.getCTP().setPPr(templateParagraph.getCTP().getPPr());
        // 添加Run标签
        for (int pos = 0; pos < newParagraph.getRuns().size(); pos++) {
            newParagraph.removeRun(pos);

        }
        for (XWPFRun s : templateParagraph.getRuns()) {
            XWPFRun targetrun = newParagraph.createRun();
            CopyRun(targetrun, s);
        }

    }

    /**
     * 复制文本节点run
     * @author Juveniless
     * @date 2017年11月27日 下午3:47:17
     * @param newRun
     *            新创建的的文本节点
     * @param templateRun
     *            模板文本节点
     *
     */
    private void CopyRun(XWPFRun newRun, XWPFRun templateRun) {
        newRun.getCTR().setRPr(templateRun.getCTR().getRPr());
        // 设置文本
        newRun.setText(templateRun.text());


    }

    /**
     * 根据map替换表格中的{key}标签
     * @author Juveniless
     * @date 2017年12月4日 下午2:47:36
     * @param xwpfTable
     * @param parametersMap
     *
     */
    public void replaceTable(XWPFTable xwpfTable, Map<String, Object> parametersMap){
        List<XWPFTableRow> rows = xwpfTable.getRows();
        for (XWPFTableRow xWPFTableRow : rows ) {
            List<XWPFTableCell> tableCells = xWPFTableRow.getTableCells();
            for (XWPFTableCell xWPFTableCell : tableCells ) {
                List<XWPFParagraph> paragraphs2 = xWPFTableCell.getParagraphs();
                for (XWPFParagraph xWPFParagraph : paragraphs2) {
                    replaceParagraph(xWPFParagraph, parametersMap);
                }
            }
        }

    }




}
