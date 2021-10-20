package zhangyu.fool.generator.main.writer.doc;

import zhangyu.fool.generator.dao.DatabaseDAO;
import zhangyu.fool.generator.model.mysql.TableField;
import zhangyu.fool.generator.model.mysql.TableInfo;

import java.util.Arrays;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2021/10/18
 * 使用flink批任务同步mysql数据到hive,有个麻烦的步骤就是要整理两份语法相似的建表语句（flink table SQL 、 hive table sql）
 * 整理建表语句都是重复工作，所以写了个程序快速输出
 */
public class FlinkHiveSQLGenerator {
    /**
     * 获取表comment信息SQL,参数1:表名、参数2:库名
     */
    private static String queryTableCommentSql = "select * from Information_schema.tables where table_name= '%s' and table_schema = '%s'";


    public void printHiveSQL() {
        List<String> tableNameList = Arrays.asList("tb_qy_crm_clue_list_search_option");

        StringBuilder sqlStringBuilder = new StringBuilder();
        for (String name : tableNameList) {
            //获取表comment信息
            String tableComment = DatabaseDAO.getOne(String.format(queryTableCommentSql, name, "crm"), TableInfo.class).getTableComment();
            if (tableComment != null && !"".equals(tableComment)) {
                sqlStringBuilder.append("\n-- " + tableComment + "\n");
            } else {
                sqlStringBuilder.append("\n-- " + name + "\n");
            }
            sqlStringBuilder.append("drop table xmz_test." + name + "_ods" + "\n");
            sqlStringBuilder.append("\n");
            sqlStringBuilder.append("create external table xmz_test." + name + "_ods(" + "\n");
            // 获取表列信息
            String querySql = TableField.getSQL(name, "crm");
            List<TableField> fieldList = DatabaseDAO.getList(querySql, TableField.class);
            for (int i = 0; i < fieldList.size(); i++) {
                TableField tableField = fieldList.get(i);
                String columnName = tableField.getColumnName();
                sqlStringBuilder.append("`" + columnName + "` " + convertHiveType(tableField));
                if (tableField.getColumnComment() != null &&
                        !"".equals(tableField.getColumnComment().trim())) {
                    String columnComment = tableField.getColumnComment();
                    sqlStringBuilder.append(" COMMENT '" + columnComment + "'");
                }
                if (i != fieldList.size() - 1) {
                    sqlStringBuilder.append(",");
                }
                sqlStringBuilder.append("\n");
            }
            sqlStringBuilder.append(")");

            if (tableComment != null && !"".equals(tableComment)) {
                sqlStringBuilder.append(" COMMENT '" + tableComment + "'\n");
            }
            sqlStringBuilder.append(" row format delimited fields terminated by '\\t';");
            sqlStringBuilder.append("\n\n\n");
        }

        System.out.println(sqlStringBuilder.toString());

    }

    private String convertHiveType(TableField tableField) {
        String hiveType = "";
        switch (tableField.getDataType()) {
            case "varchar":
            case "text":
                hiveType = "string";
                break;
            case "datetime":
            case "timestamp":
                hiveType = "date";
                break;
            case "int":
                hiveType = tableField.getColumnType().toLowerCase().contains("unsigned")
                        ? "bigint" : tableField.getDataType();
                break;
            default:
                hiveType = tableField.getDataType();

        }
        return hiveType;
    }


    public void printFlinkSQL() {
        List<String> tableNameList = Arrays.asList("tb_crm_client_pool_rule");
        StringBuilder sqlStringBuilder = new StringBuilder();
        for (String name : tableNameList) {

            sqlStringBuilder.append("create table " + name + "(" + "\n");
            // 获取表列信息
            String querySql = TableField.getSQL(name, "crm");
            List<TableField> fieldList = DatabaseDAO.getList(querySql, TableField.class);
            for (int i = 0; i < fieldList.size(); i++) {
                TableField tableField = fieldList.get(i);
                String columnName = tableField.getColumnName();
                sqlStringBuilder.append("`" + columnName + "` " + convertFlinkType(tableField));
                if (tableField.getColumnComment() != null &&
                        !"".equals(tableField.getColumnComment().trim())) {
                    String columnComment = tableField.getColumnComment();
                    sqlStringBuilder.append(" COMMENT '" + columnComment + "'");
                }
                if (i != fieldList.size() - 1) {
                    sqlStringBuilder.append(",");
                }
                sqlStringBuilder.append("\n");
            }
            sqlStringBuilder.append(")\n");
            sqlStringBuilder.append("--\n");

            sqlStringBuilder.append("\n\n\n");
        }

        System.out.println(sqlStringBuilder.toString());

    }

    private String convertFlinkType(TableField tableField) {
        String flinkType = "";
        switch (tableField.getDataType()) {
            case "varchar":
            case "text":
                flinkType = "string";
                break;
            case "datetime":
            case "timestamp":
                flinkType = "TIMESTAMP(0)";
                break;
            case "bigint":
                //flinkType = "DECIMAL(20, 0)";
                flinkType = "bigint";
                break;
            case "int":
                flinkType = tableField.getColumnType().toLowerCase().contains("unsigned")
                        ? "bigint" : tableField.getDataType();
                break;
            default:
                flinkType = tableField.getDataType();
        }
        return flinkType;
    }


    public static void main(String[] args) {
        FlinkHiveSQLGenerator flinkHiveSQLGenerator = new FlinkHiveSQLGenerator();
        flinkHiveSQLGenerator.printHiveSQL();
    }


}
