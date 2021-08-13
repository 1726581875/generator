package zhangyu.fool.generator.service;

import org.junit.Assert;
import org.junit.Test;
import zhangyu.fool.generator.BaseTest;
import zhangyu.fool.generator.model.TableField;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author xiaomingzhang
 * @date 2021/8/13
 */
public class DatabaseServiceTest extends BaseTest {


    @Test
    public void getTableNameMapTest(){
        Map<String, String> tableNameMap = DatabaseServiceImpl.getTableNameMap();
        Assert.assertTrue("tableNameMap not be empty", tableNameMap != null || tableNameMap.size() > 0);
    }

    @Test
    public void getFieldListTest(){
        consumerAndAssert(tableName ->{
            List<TableField> fieldList = DatabaseServiceImpl.getFieldList(tableName);
            Assert.assertTrue("get fieldList not be empty", fieldList != null || fieldList.size() > 0);
        });
    }

    @Test
    public void getPrimaryField(){
        consumerAndAssert(tableName ->{
            TableField primaryField = DatabaseServiceImpl.getPrimaryField(tableName);
            Assert.assertNotNull("primaryField not be null", primaryField);
        });
    }

    @Test
    public void getPrimaryType(){
        consumerAndAssert(tableName ->{
            String primaryType = DatabaseServiceImpl.getPrimaryType(tableName);
            Assert.assertNotNull("primaryType not be null", primaryType);
        });
    }


    @Test
    public void getPrimaryName(){
        consumerAndAssert(tableName ->{
            String primaryName = DatabaseServiceImpl.getPrimaryName(tableName);
            Assert.assertNotNull("primaryName not be null", primaryName);
        });
    }

    public void consumerAndAssert(Consumer<String> action){
        Map<String, String> tableNameMap = DatabaseServiceImpl.getTableNameMap();
        tableNameMap.keySet().forEach(action);
    }


}
