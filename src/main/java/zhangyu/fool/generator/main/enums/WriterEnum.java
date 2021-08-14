package zhangyu.fool.generator.main.enums;

/**
 * @author xiaomingzhang
 * @date 2021/8/2
 */
public enum WriterEnum {

    /**
     * 值为生成代码相对路径
     */
    ENTITY("model/entity"),
    DAO("dao"),
    DTO("model/dto"),
    SERVICE("service"),
    CONTROLLER("controller"),
    VO("model"),
    CONFIG("config"),
    UTIL("util"),

    /** 以下则无含义 **/
    TEST("test"),
    SQL_FILE("test/sql"),
    SQL_DOCX("doc"),

    MAVEN_PROJECT("maven"),
    VUE_PROJECT("vue");

    String value;

    WriterEnum(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

}
