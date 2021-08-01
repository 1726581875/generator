package zhangyu.fool.generate.service;

import org.dom4j.Element;
import zhangyu.fool.generate.dao.DataBaseDAO;
import zhangyu.fool.generate.enums.ProjectEnum;
import zhangyu.fool.generate.util.NameConvertUtil;
import zhangyu.fool.generate.util.XmlUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomingzhang
 * @date 2021/7/29
 */
public class DatabaseService {

    /**
     * 存储xml结构里配置的表名和实体类名映射 <表名,实体类名>
     */
    private static final Map<String, String> TABLE_NAME_MAP = new HashMap<>(16);

    private DataBaseDAO dataBaseDAO;

    /**
     * 获取配置文件里配置的所有 <表名,对象名>
     *
     * @return map<表名, 对象名>
     */
    public static Map<String, String> getTableNameMap() {
        Element rootElement = XmlUtil.getRootElement();
        List<Element> tableElements = rootElement.element(ProjectEnum.TABLES.getElementName()).elements();
        if (tableElements != null && tableElements.size() > 0) {
            tableElements.forEach(e -> TABLE_NAME_MAP.put(e.element(ProjectEnum.TABLE_NAME.getElementName()).getTextTrim()
                    ,e.element(ProjectEnum.ENTITY_NAME.getElementName()).getTextTrim()));
        } else {
            List<String> tableNameList = DataBaseDAO.getTableNameList();
            tableNameList.forEach(name -> TABLE_NAME_MAP.put(name, NameConvertUtil.lineToBigHump(name)));
        }
        return TABLE_NAME_MAP;
    }


}
