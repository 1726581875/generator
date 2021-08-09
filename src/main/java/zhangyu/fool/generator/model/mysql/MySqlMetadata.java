package zhangyu.fool.generator.model.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xiaomingzhang
 * @date 2021/8/9
 */
public interface MySqlMetadata extends Metadata{

    /**
     * 根据resultSet,解析获取数据
     * @param result
     * @throws SQLException
     * @return
     */
    MySqlMetadata getAnalyzedData(ResultSet result) throws SQLException;

}
