package zhangyu.fool.generate.model;

/**
 * @author xiaomingzhang
 * @date 2021/5/27
 * 数据库表元数据
 */
public class TableField {
    /**
     * 字段名，如course_id
     */
    private String name;
    /**
     * 字段名小驼峰，如courseId
     */
    private String nameHump;
    /**
     * 字段名大驼峰：CourseId
     */
    private String nameBigHump;
    /**
     * 中文名：课程
     */
    private String nameCn;
    /**
     * 字段类型：char(8)
     */
    private String type;
    /**
     * java类型：String
     */
    private String javaType;
    /**
     * 注释：课程|ID
     */
    private String comment;
    /**
     * 是否可为空
     */
    private Boolean nullAble;
    /**
     * 字符串长度
     */
    private Integer length;
    /**
     * 键类型
     */
    private String keyType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameHump() {
        return nameHump;
    }

    public void setNameHump(String nameHump) {
        this.nameHump = nameHump;
    }

    public String getNameBigHump() {
        return nameBigHump;
    }

    public void setNameBigHump(String nameBigHump) {
        this.nameBigHump = nameBigHump;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getNullAble() {
        return nullAble;
    }

    public void setNullAble(Boolean nullAble) {
        this.nullAble = nullAble;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", nameHump='" + nameHump + '\'' +
                ", nameBigHump='" + nameBigHump + '\'' +
                ", nameCn='" + nameCn + '\'' +
                ", type='" + type + '\'' +
                ", javaType='" + javaType + '\'' +
                ", comment='" + comment + '\'' +
                ", nullAble=" + nullAble +
                ", length=" + length +
                ", keyType='" + keyType + '\'' +
                '}';
    }
}
