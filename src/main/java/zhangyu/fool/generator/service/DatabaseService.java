package zhangyu.fool.generator.service;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generator.dao.DatabaseDAO;
import zhangyu.fool.generator.enums.ProjectEnum;
import zhangyu.fool.generator.model.TableField;
import zhangyu.fool.generator.model.mysql.TableColumn;
import zhangyu.fool.generator.model.mysql.TableInfo;
import zhangyu.fool.generator.util.NameConvertUtil;
import zhangyu.fool.generator.util.XmlUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author xiaomingzhang
 * @date 2021/7/29
 */
public class DatabaseService {
    private static final Logger log = LoggerFactory.getLogger(DatabaseService.class);
    /**
     * 存储xml结构里配置的表名和实体类名映射 <表名,实体类名>
     */
    private static Map<String, String> TABLE_NAME_MAP = null;

    private static final Map<String, List<TableField>> TABLE_FIELD_CACHE = new ConcurrentHashMap<>(16);

    private static final Lock lock = new ReentrantLock();


    private DatabaseDAO dataBaseDAO;

    /**
     * 获取配置文件里配置的所有 <表名,对象名>
     *
     * @return map<表名, 对象名>
     */
    public static Map<String, String> getTableNameMap() {
        Element rootElement = XmlUtil.getRootElement();
        Element tablesElement = rootElement.element(ProjectEnum.TABLES.getName());
        if (TABLE_NAME_MAP == null) {
            lock.lock();
            try {
                if (TABLE_NAME_MAP == null) {
                    TABLE_NAME_MAP = new HashMap<>(16);
                    if (tablesElement == null || tablesElement.elements() == null || tablesElement.elements().size() == 0) {
                        String sql = TableInfo.getSQL(DatabaseDAO.getDatabaseName());
                        List<String> tableNameList = DatabaseDAO.getList(sql, TableInfo.class).stream().map(TableInfo::getTableName).collect(Collectors.toList());
                        tableNameList.forEach(name -> TABLE_NAME_MAP.put(name, NameConvertUtil.lineToBigHump(name)));
                    } else {
                        List<Element> tableElements = tablesElement.elements();
                        tableElements.forEach(e -> TABLE_NAME_MAP.put(e.element(ProjectEnum.TABLE_NAME.getName()).getTextTrim()
                                , e.element(ProjectEnum.ENTITY_NAME.getName()).getTextTrim()));
                    }
                }
            } catch (Exception e) {
                log.error("获取表名发生异常", e);
            } finally {
                lock.unlock();
            }

        }
        return TABLE_NAME_MAP;
    }

    /**
     * 根据表名获取列信息
     *
     * @param tableName
     * @return
     */
    public static List<TableField> getFieldList(String tableName) {
        if (TABLE_FIELD_CACHE.get(tableName) == null) {
            /**
             * todo synchronized是一个比较重的锁，ReentrantLock可重入锁底层是CAS,属于比较轻量基本的锁，但是貌似不支持根据tableName加锁。
             * 测试过synchronized可以实现这个效果，但是仍不满意，等待后续重构
             */
            synchronized (tableName) {
                if (TABLE_FIELD_CACHE.get(tableName) == null) {
                    String sql = TableColumn.getSQL(tableName);
                    List<TableColumn> columnList = DatabaseDAO.getList(sql,TableColumn.class);
                    List<TableField> tableFields = columnList.stream().map(TableField::getField).collect(Collectors.toList());
                    TABLE_FIELD_CACHE.put(tableName, tableFields);
                }
            }
        }
        return TABLE_FIELD_CACHE.get(tableName);
    }

    public static TableField getPrimaryField(String tableName) {
        List<TableField> fieldList = getFieldList(tableName);
        return fieldList.stream().filter(e -> TableColumn.PRI.equals(e.getKeyType())).findFirst().get();
    }

    /**
     * 获取<strong>表主键</strong>的Java类型
     *
     * @param tableName
     * @return 例如 Integer、String、Long..
     */
    public static String getPrimaryType(String tableName) {
        return getPrimaryField(tableName).getJavaType();
    }

    /**
     * 获取表主键id名（转换为大驼峰后的）
     *
     * @param tableName
     * @return 例如 ： ArticleId、UserId
     */
    public static String getPrimaryName(String tableName) {
        String tableColumnName = getPrimaryField(tableName).getName();
        return NameConvertUtil.lineToBigHump(tableColumnName);
    }


    public static void main(String[] args) {
        Map<String, String> tableNameMap = getTableNameMap();
        List<String> tableNameList = tableNameMap.keySet().stream().collect(Collectors.toList());
        tableNameList.addAll(tableNameMap.keySet());
        tableNameList.addAll(tableNameMap.keySet());
        System.out.println(tableNameList.size());
        tableNameList.forEach(e -> new Thread(() -> DatabaseService.getFieldList(e)).start());
    }


}
