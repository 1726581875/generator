package zhangyu.fool.generator.main.enums;

/**
 * @author xmz
 * @date: 2021/07/26
 * 不同文件名后缀
 */
public enum TypeSuffixEnum {

    ENTITY("Entity",".java"),
    MAPPER("Mapper","Mapper.java"),
    REPOSITORY("Repository","Repository.java"),
    MAPPER_XML("XML","Mapper.xml"),
    SERVICE("Service","Service.java"),
    SERVICE_IMPL("ServiceImpl","ServiceImpl.java"),
    CONTROLLER("Controller","Controller.java"),
    VO("VO","VO.java"),
    DTO("DTO","DTO.java"),
    CONTROLLER_TEST("ControllerTest","ControllerTest.java"),

    /**
     * vue页面组件
     */
    VIEW("vue page",".vue"),
    ;

    String type;
    String suffix;
    TypeSuffixEnum(String type,String suffix){
        this.type = type;
        this.suffix = suffix;
    }

    public String getType() {
        return type;
    }

    public String getSuffix() {
        return suffix;
    }

    public static String getSuffix(String type){
        TypeSuffixEnum[] values = values();
        for (TypeSuffixEnum typeSuffixEnum : values){
            if(typeSuffixEnum.getType().equals(type)){
                return typeSuffixEnum.getSuffix();
            }
        }
        return ENTITY.getSuffix();
    }

}
