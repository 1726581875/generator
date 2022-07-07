package zhangyu.fool.generator.model.mysql;

import lombok.Data;
import zhangyu.fool.generator.dao.DatabaseDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/6/17
 */
@Data
public class TableIndex implements MySqlMetadata {

    public static final String SQL = "show index from %s;";

    private String nonUnique;

    private String keyName;

    /**
     * 字段备注
     */
    private Integer seqInIndex;

    /**
     * int(11) unsigned
     */
    private String columnName;

    public static String getSQL(Object... params) {
        return String.format(SQL, params);
    }

    @Override
    public TableIndex getAnalyzedData(ResultSet result) throws SQLException {
        TableIndex tableIndex = new TableIndex();
        tableIndex.setNonUnique(result.getString("non_unique"));
        tableIndex.setKeyName(result.getString("key_name"));
        tableIndex.setSeqInIndex(result.getInt("seq_in_index"));
        tableIndex.setColumnName(result.getString("column_name"));
        return tableIndex;
    }


    public static void main(String[] args) {
        List<TableIndex> IndexList = DatabaseDAO.getList(TableIndex.getSQL("yqsqxx100064"), TableIndex.class);


        IndexList.forEach(System.out::println);
    }


}
