package zhangyu.fool.generator.main.model.param;

import lombok.Data;

/**
 * @author xiaomingzhang
 * @date 2021/7/23
 */
@Data
public class ControllerParam extends CommonParam {
    /**
     * 主键java类型 如Long、Integer
     */
    private String keyType;

}
