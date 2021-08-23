package zhangyu.fool.generator.dao;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import zhangyu.fool.generator.BaseTest;
import zhangyu.fool.generator.annotation.BindTest;
import zhangyu.fool.generator.model.mysql.TableColumn;
import zhangyu.fool.generator.model.mysql.TableInfo;
import zhangyu.fool.generator.model.mysql.TableSql;

import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2021/8/13
 */
@BindTest(DatabaseDAO.class)
public class DatabaseDAOTest extends BaseTest {

    /**
     * 测试 getList方法
     * 获取表信息
     */
    @Test
    public void getTableInfoListTest(){
        List<TableInfo> infoList = getTableInfoList();
        Assert.assertTrue("should not be empty", CollectionUtils.isNotEmpty(infoList));
    }

    /**
     * 获取表的列信息
     */
    @Test
    public void getTableColumnTest() {
        List<TableInfo> infoList = getTableInfoList();
        for (TableInfo table : infoList) {
            String sql = TableColumn.getSQL(table.getTableName());
            List<TableColumn> tableColumns = DatabaseDAO.getList(sql, TableColumn.class);
            Assert.assertTrue("should not be empty", CollectionUtils.isNotEmpty(infoList));
        }
    }

    /**
     * 获取表的建表sql语句测试
     */
    @Test
    public void getTableCreateSqlTest() {
        List<TableInfo> infoList = getTableInfoList();
        for (TableInfo table : infoList) {
            String sql = TableSql.getSQL(table.getTableName());
            TableSql tableSql = DatabaseDAO.getOne(sql, TableSql.class);
            Assert.assertNotNull("should not be null",tableSql);
            Assert.assertNotNull("should not be null",tableSql.getTableSql());
        }
    }

    private List<TableInfo> getTableInfoList() {
        String databaseName = DatabaseDAO.getDatabaseName();
        String sql = TableInfo.getSQL(databaseName);
        List<TableInfo> infoList = DatabaseDAO.getList(sql, TableInfo.class);
        return infoList;
    }


}
