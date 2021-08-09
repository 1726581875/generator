package zhangyu.fool.generator.main.enums;

/**
 * @author xiaomingzhang
 * @date 2021/7/23
 */
public enum OrmTypeEnum {
    JPA("jpa"),
    MYBATIS("mybatis"),
    MYBATIS_PLUS("mybatis-plus")
    ;
    String type;
    OrmTypeEnum(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
