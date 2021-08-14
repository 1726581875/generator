package zhangyu.fool.generator.util;

import zhangyu.fool.generator.enums.ProjectEnum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xmz
 * 2020年9月12日
 * 名字转换工具类
 */
public class NameConvertUtil {

    static final Pattern linePattern = Pattern.compile("_(\\w)");
	/**
	 * 下划线转小驼峰
     * course_record -> courseRecord
	 * @param str
	 * @return
	 */
    public static String lineToHump(String str){
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 下划线转大驼峰
     * course_record -> CourseRecord
     * @param str
     * @return
     */
    public static String lineToBigHump(String str){
        String s = lineToHump(str);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰转小驼峰
     * CourseRecord -> courseRecord
     * @param str
     * @return
     */
    public static String bigHumpToHump(String str){
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

}
