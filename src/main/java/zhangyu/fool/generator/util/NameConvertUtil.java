package zhangyu.fool.generator.util;

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
	 * @param str
	 * @return
	 */
    public static String lineToHump(String str){
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 下划线转大驼峰
     * @param str
     * @return
     */
    public static String lineToBigHump(String str){
        String s = lineToHump(str);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String bigHumpToHump(String str){
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }


	/**
	 * 把包名转换成目录名
	 * 比如 aa.bb.cc -> aa//bb//cc//
	 * @param packageName
	 * @return
	 */
    public static String getDirNameByPackageName(String packageName){
       StringBuilder strBuilder = new StringBuilder(packageName);
       int index = 0;
       while((index = strBuilder.indexOf(".")) != -1){
    	   strBuilder.replace(index, index+1, "\\");
       } 
       strBuilder.append("\\");
       return strBuilder.toString();
    }
	
    /**
     * 从xml里读，获取全路径包名
     * 如，传入dao，返回com.small.chili.blog.dao
     * @param packageName 
     * @return
     */
    public static String getPackageName(String packageName){
        StringBuilder fullName = new StringBuilder();
        fullName.append(XmlUtil.getText("groupId"));
        fullName.append(".");
        fullName.append(XmlUtil.getText("artifactId"));
        if (packageName != null) {
            fullName.append(".");
            fullName.append(XmlUtil.getText(packageName));
        }
        return fullName.toString();
     }
    
    
    public static void main(String[] args) {
    	System.out.println(getPackageName("entityPackage"));
	}
}
