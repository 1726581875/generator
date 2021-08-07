package zhangyu.fool.generate.service;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generate.dao.DatabaseDAO;
import zhangyu.fool.generate.enums.ProjectEnum;
import zhangyu.fool.generate.model.TableField;
import zhangyu.fool.generate.model.mysql.TableColumn;
import zhangyu.fool.generate.util.NameConvertUtil;
import zhangyu.fool.generate.util.XmlUtil;

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
            if (TABLE_NAME_MAP == null) {
                try {
                    TABLE_NAME_MAP = new HashMap<>(16);
                    if (tablesElement == null || tablesElement.elements() == null || tablesElement.elements().size() == 0) {
                        List<String> tableNameList = DatabaseDAO.getTableNameList();
                        tableNameList.forEach(name -> TABLE_NAME_MAP.put(name, NameConvertUtil.lineToBigHump(name)));
                    } else {
                        List<Element> tableElements = tablesElement.elements();
                        tableElements.forEach(e -> TABLE_NAME_MAP.put(e.element(ProjectEnum.TABLE_NAME.getName()).getTextTrim()
                                , e.element(ProjectEnum.ENTITY_NAME.getName()).getTextTrim()));
                    }
                } catch (Exception e) {
                    log.error("获取表名发生异常", e);
                } finally {
                    lock.unlock();
                }
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
                    List<TableColumn> columnList = DatabaseDAO.getColumnByTableName(tableName);
                    List<TableField> tableFields = columnList.stream().map(TableField::getField).collect(Collectors.toList());
                    TABLE_FIELD_CACHE.put(tableName, tableFields);
                }
            }
        }
        return TABLE_FIELD_CACHE.get(tableName);
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
