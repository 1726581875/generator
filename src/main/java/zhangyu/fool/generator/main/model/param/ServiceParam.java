package zhangyu.fool.generator.main.model.param;

import lombok.Data;

/**
 * @author xiaomingzhang
 * @date 2021/7/23
 */
@Data
public class ServiceParam extends CommonParam {
    /**
     * 实体类主键名首字母大写 如ArticleId
     */
    private String entityKey;
    /**
     * 主键java类型 如Long
     */
    private String keyType;

}
