package zhangyu.fool.generator.main.writer.doc;

import zhangyu.fool.generator.dao.DatabaseDAO;
import zhangyu.fool.generator.model.mysql.TableField;
import zhangyu.fool.generator.model.mysql.TableIndex;
import zhangyu.fool.generator.model.mysql.TableInfo;
import zhangyu.fool.generator.service.DatabaseService;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xiaomingzhang
 * @date 2022/6/16
 * 根据mysql表结构，生成postgresSQL格式建表语句
 */
public class PostgresSqlWriter {

    /**
     * 获取表comment信息SQL,参数1:表名、参数2:库名
     */
    private static String queryTableCommentSql = "select * from Information_schema.tables where table_name= '%s' and table_schema = '%s'";


    public void printPostgresSQL() {
        List<String> tableNameList = Arrays.asList("table_name");

        StringBuilder sqlStringBuilder = new StringBuilder();
        for (String name : tableNameList) {

            //获取表comment信息
            String tableComment = DatabaseDAO.getOne(String.format(queryTableCommentSql, name, "record"), TableInfo.class).getTableComment();

            StringBuilder createTableSql = new StringBuilder("-- dm"+ name +"表建表sql  start \n CREATE TABLE \"public\".\"dm_"+ name +"\" (\n");

            // 获取表列信息
            String querySql = TableField.getSQL(name, "record");
            List<TableField> fieldList = DatabaseDAO.getList(querySql, TableField.class);
            for (int i = 0; i < fieldList.size(); i++) {
                TableField tableField = fieldList.get(i);
                createTableSql.append("\"" + tableField.getColumnName() + "\" " + convertType(tableField.getColumnType()) + ",\n");
                if(i == fieldList.size() - 1) {
                    zhangyu.fool.generator.model.TableField primaryField = DatabaseService.getPrimaryField(name);
                    createTableSql.append("CONSTRAINT \"dm_" + name +"_pkey\" PRIMARY KEY (\""+ primaryField.getName() +"\")\n");
                }


            }

            createTableSql.append(");\n");


            createTableSql.append("-- dm_"+ name +" 索引 开始====================\n");
            List<TableIndex> indexList = DatabaseDAO.getList(TableIndex.getSQL(name), TableIndex.class);

            Map<String, List<TableIndex>> indexMap = indexList.stream().collect(Collectors.groupingBy(TableIndex::getKeyName));

            indexMap.forEach((k,v) -> {
                v.sort(Comparator.comparingInt(TableIndex::getSeqInIndex));


                if(!"PRIMARY".equals(v.get(0).getKeyName())) {
                    if("0".equals(v.get(0).getNonUnique())){
                        createTableSql.append("CREATE UNIQUE INDEX "+ k +" on \"public\".\"dm_"+ name +"\" ("+ v.stream().map(TableIndex::getColumnName)
                                .collect(Collectors.joining(",")) +"); \n");

                    } else {
                        createTableSql.append("CREATE INDEX "+ k +" on \"public\".\"dm_"+ name +"\" ("+ v.stream().map(TableIndex::getColumnName)
                                .collect(Collectors.joining(",")) +"); \n");

                    }
                }
            });

            createTableSql.append("-- dm_"+ name +" 索引 结束====================\n");
            createTableSql.append("\n");

            for (int i = 0; i < fieldList.size(); i++) {
                TableField tableField = fieldList.get(i);
                createTableSql.append("COMMENT ON COLUMN \"public\".\"dm_"+ name +"\".\""+ tableField.getColumnName() +"\" IS '"+ tableField.getColumnComment() +"';\n");
            }

            createTableSql.append("COMMENT ON TABLE \"public\".\"dm_"+ name +"\" IS '" + tableComment + "';");

            System.out.println(createTableSql.toString());

            System.out.println("-- dm" + name + "  end\n");

            System.out.println("\n");
        }



    }



    private String convertType(String type) {
        if(type.contains("varchar") || type.contains("char")){
            return type;
        } else if(type.contains("datetime") || type.contains("timestamp")) {
            return "timestamp(6)";
        } else if(type.contains("bigint")) {
            return "int8";
        }
        else if(type.contains("int")) {
            return "int4";
        }

        return type;
    }



}
