package zhangyu.fool.generate.writer.enums;

/**
 * @author xiaomingzhang
 * @date 2021/8/2
 */
public enum WriterEnum {

    ENTITY("entity"),
    DAO("dao"),
    DTO("dto"),
    SERVICE("service"),
    CONTROLLER("controller"),
    VO("vo"),
    CONFIG("config"),
    UTIL("util"),
    TEST("test"),

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