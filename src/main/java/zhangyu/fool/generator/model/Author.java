package zhangyu.fool.generator.model;

import lombok.Data;
import zhangyu.fool.generator.util.XmlUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xmz
 * @date: 2020/10/29
 */
@Data
public class Author {
    /**
     * 作者
     */
    private String author;
    /**
     * 日期
     */
    private String date;

    public static final Author AUTHOR = Author.build();


    public Author(String author, String date){
        this.author = author;
        this.date = date;
    }

    public static Author build(){
        String name = XmlUtil.getRootElement().getName();
        return new Author(name == null ? "xmz" : name
                ,new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
    }

}
