package zhangyu.fool.generate.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhangyu.fool.generate.enums.ProjectEnum;
import zhangyu.fool.generate.model.Table;
import zhangyu.fool.generate.service.DatabaseService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author xmz 
 * @date 2020/9/12
 * xml文件读取工具类
 */
public class XmlUtil {
	/**
	 * 配置文件所在位置
 	 */
	public static final String PROFILE_PATH = "src\\main\\resources\\profile.xml";

	/**
	 * 存储xml结构里的<节点名,节点元素对象>
 	 */
	private static Map<String, Element> elementMap = null;

	private static Logger log = LoggerFactory.getLogger(XmlUtil.class);

	/**
	 * 使用单例模式
	 * 获取标签名和值的映射nameValueMap
	 * @return
	 * @throws DocumentException
	 */
	private static Map<String, Element> getElementMap() {
		if (null == elementMap) {
			synchronized (XmlUtil.class) {
				if (null == elementMap) {
					elementMap = new HashMap<>(16);
					recursiveNodeTree(getRootElement(), elementMap);
				}
			}
		}
		return elementMap;
	}


	/**
	 * 获取List<Table>对象
	 *
	 * @return map
	 */
	public static List<Table> getTableList() {
		List<Table> tableList = new ArrayList<>();
		Map<String, String> tableNameMap = DatabaseService.getTableNameMap();
		tableNameMap.forEach((tableName,objectName) -> {
            Table table = new Table(tableName, NameConvertUtil.bigHumpToHump(objectName),objectName);
			tableList.add(table);
		});
		return tableList;
	}



	/**
	 * 递归遍历节点树，设置值到nameValueMap
	 * 
	 * @param node
	 * @param map
	 */
	@Deprecated
	public static void recursiveNode(Element node, Map<String, Element> map) {
		// 递归遍历当前节点所有的子节点
		List<Element> elementList = node.elements();
		elementList.forEach(e ->{
		    String text = e.getTextTrim();
			// 如果节点里有内容(有值)
			if (!text.equals("")) {			
				map.put(e.getName(), e);
			}
			recursiveNode(e, map);// 递归
		});	

	}

	public static void recursiveNodeTree(Element node, Map<String, Element> map) {
		map.put(node.getName(), node);
		// 递归遍历当前节点所有的子节点
		List<Element> elementList = node.elements();
		elementList.forEach(e ->{		   								
			recursiveNodeTree(e, map);
		});	

	}
	
	/**
	 * 获取根节点
	 * 
	 * @return rootElement
	 */
	public static Element getRootElement() {
		
		// 只生成配置文件中的第一个table节点
		File file = new File(PROFILE_PATH);
		SAXReader reader = new SAXReader();
		// 读取xml文件到Document中
		Document doc = null;
		try {
			doc = reader.read(file);
		} catch (DocumentException e) {
			log.error("====读取profile.xml文件失败====", e);
		}
		// 获取xml文件的根节点
		return doc.getRootElement();
	}

	/**
	 * 根据标签名，获取里面的值
	 * @param nodeName
	 * @return
	 * 
	 */
	public static String getText(String nodeName) {
		getElementMap();
		Element element = elementMap.get(nodeName);
		if(element == null){
			log.warn("标签<"+nodeName+"><"+nodeName+"/>不存在,无法获取到值");
			return null;
		}
		if(element.getTextTrim().equals("")){
			log.warn("标签<"+nodeName+"><"+nodeName+"/>内没有值");
			return null;
		}	
		return element.getTextTrim();
	}
	
	/**
	 * 根据ProjectEnum枚举类获取到标签的内容
	 * @param projectEnum
	 * @return
	 */
	public static String getText(ProjectEnum projectEnum) {
		getElementMap();
		String nodeName = projectEnum.getName();
		return getText(nodeName);
	}
	

	public static Element getNode(String nodeName) {
		getElementMap();
		return elementMap.get(nodeName);
	}
	
	
	public static void main(String[] args) throws DocumentException {
		String url = XmlUtil.getText(ProjectEnum.URL);
		System.out.println(url);
	}

}