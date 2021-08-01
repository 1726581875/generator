package zhangyu.fool.generate.model;

import java.util.Map;

/**
 * @author xiaomingzhang
 * @date 2021/6/8
 * 类公共属性参数
 */
public class ClassCommonAttribute {

    /**
     * 类全名 CourseService
     */
    private String className;
    /**
     * 实体类名首字母大写 Course
     */
    private String entityName;
    /**
     * 实体类名首字母小写 course
     */
    private String entityNameLower;
    /**
     * 基础包名
     */
    private String basePackageName;
    /**
     * 其他属性
     */
    private Map<String,Object> otherAttribute;

    public ClassCommonAttribute(){}

    public static ClassCommonAttribute build(){
        return new ClassCommonAttribute();
    }

    public String getClassName() {
        return className;
    }

    public ClassCommonAttribute setClassName(String className) {
        this.className = className;
        return this;
    }

    public String getEntityName() {
        return entityName;
    }

    public ClassCommonAttribute setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public String getEntityNameLower() {
        return entityNameLower;
    }

    public ClassCommonAttribute setEntityNameLower(String entityNameLower) {
        this.entityNameLower = entityNameLower;
        return this;
    }

    public String getBasePackageName() {
        return basePackageName;
    }

    public ClassCommonAttribute setBasePackageName(String basePackageName) {
        this.basePackageName = basePackageName;
        return this;
    }

    public Map<String, Object> getOtherAttribute() {
        return otherAttribute;
    }

    public ClassCommonAttribute setOtherAttribute(Map<String, Object> otherAttribute) {
        this.otherAttribute = otherAttribute;
        return this;
    }
}
