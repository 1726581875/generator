package zhangyu.fool.generator.enums;

/**
 * @author xmz
 * @date 2020/09/25
 * 工程基本属性枚举，对应profile.xml里配置的标签名
 */
public enum ProjectEnum {
	/* 工程根节点名字，groupId，artifactId */
	PROJECT_PATH("projectPath"), 
	GROUP_ID("groupId"),  
	ARTIFACT_ID("artifactId"),
	/* 数据源对应节点名 */
	URL("url"),
	DRIVER("driver"),
	USERNAME("username"),
	PASSWORD("password"),
	/* 表名、实体类名*/
	TABLES("tables"),
	TABLE_NAME("tableName"),
	ENTITY_NAME("entityName"),
	;

	private String name;
	
	private ProjectEnum(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
		
	
}
