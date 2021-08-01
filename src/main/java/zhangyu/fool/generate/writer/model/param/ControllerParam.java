package zhangyu.fool.generate.writer.model.param;

import lombok.Data;

/**
 * @author xiaomingzhang
 * @date 2021/7/23
 */
@Data
public class ControllerParam extends CommonParam {

    private String controllerPkName;

    private String servicePkName;

    private String entityPkName;

    private String voPkName;

    /**
     * 主键java类型 如Long、Integer
     */
    private String keyType;

}
