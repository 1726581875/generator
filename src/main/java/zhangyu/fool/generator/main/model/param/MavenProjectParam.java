package zhangyu.fool.generator.main.model.param;

import lombok.Data;

/**
 * @author xiaomingzhang
 * @date 2021/8/5
 */
@Data
public class MavenProjectParam extends CommonParam {

    private String artifactId;

    private String groupId;

    private String driver;

    private String url;

    private String username;

    private String password;



}
