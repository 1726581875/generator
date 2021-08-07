package zhangyu.fool.generate.model;

import lombok.Data;
import zhangyu.fool.generate.enums.TypeMappingEnum;
import zhangyu.fool.generate.model.mysql.TableColumn;
import zhangyu.fool.generate.util.NameConvertUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xiaomingzhang
 * @date 2021/5/27
 * 数据库表元数据
 */
@Data
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

    public static TableField getField(TableColumn tableColumn){
        String columnName = tableColumn.getField();
        String type = tableColumn.getType();
        String comment = tableColumn.getComment();
        String nullAble = tableColumn.getNullAble();
        String keyType = tableColumn.getKey();
        TableField field = new TableField();
        field.setName(columnName);
        field.setNameHump(NameConvertUtil.lineToHump(columnName));
        field.setNameBigHump(NameConvertUtil.lineToBigHump(columnName));
        field.setType(type);
        field.setJavaType(TypeMappingEnum.getJavaType(type));
        field.setComment(comment);
        field.setKeyType(keyType);
        if (comment.contains("|")) {
            field.setNameCn(comment.substring(0, comment.indexOf("|")));
        } else {
            field.setNameCn(comment);
        }
        field.setNullAble("YES".equals(nullAble));
        if (type.toUpperCase().contains("varchar".toUpperCase())) {
            String lengthStr = type.substring(type.indexOf("(") + 1, type.length() - 1);
            field.setLength(Integer.valueOf(lengthStr));
        } else {
            field.setLength(0);
        }
        return field;
    }
}
