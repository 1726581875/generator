package zhangyu.fool.generate.enums;
/**
 * 
 * @author xmz
 * 2020年9月12日
 * 数据库字段类型和java类型映射枚举类
 */
public enum TypeMappingEnum {
    OTHER_TO_STRING("other", "String"),
	BIGINT_TO_LONG("bigint","Long"),
    INT_TO_INTEGER("int", "Integer"),
    CHAR_TO_STRING("char", "String"),
    TEXT_TO_STRING("text", "String"),
    VARCHAR_TO_STRING("varchar", "String"),
    DATETIME_TO_DATE("datetime", "Date"),
    TIMESTAMP_TO_DATE("timestamp","Date"),
    DECIMAL_TO_BIGDECIMAL("decimal","BigDecimal")
	;
	
	private String dbType;
	
	private String javaType;
	
	private TypeMappingEnum(String dbType, String javaType){
		this.dbType = dbType;
		this.javaType = javaType;
	}

	public String getDbType() {
		return dbType;
	}

	public String getJavaType() {
		return javaType;
	}
    
	public static String getJavaType(String dbType){
		TypeMappingEnum[] typeMappingEnums = TypeMappingEnum.values();
		//遍历枚举
		for (TypeMappingEnum typeMappingEnum : typeMappingEnums) {
			if(dbType.contains(typeMappingEnum.getDbType())){
				return typeMappingEnum.getJavaType();	
			}
		}
		return TypeMappingEnum.OTHER_TO_STRING.getJavaType();
	}

}
