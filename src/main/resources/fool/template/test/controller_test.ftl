package ${basePackagePath}.controller;

import ${basePackagePath}.BaseMvcTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ${author.author}
 * @date: ${author.date}
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ${entityName}ControllerTest extends BaseMvcTest {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 测试分页查找接口
     * 请求方法 get
     * url /${entityNameLow}/list
     */
    @Test
    public void whenQuery${entityName}ListSuccess() throws Exception {
        // 不传条件
        sendGet("/${entityNameLow}/list",null).isOk();

        //传条件
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("id","1");
        ResultActions resultActions = sendGet("/${entityNameLow}/list", paramMap).isOk().getResultActions();
        // 期望只返回一个
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.content.length()").value(1));
    }
    /**
     * 测试删除接口
     * 请求方法 delete
     * url /${entityNameLow}/{id}
     */
    @Test
    public void whenDrop${entityName}Success(){
        sendDelete("/${entityNameLow}/1",null).isOk();
    }


    /**
     * 测试更新/插入接口
     * 请求方法 post
     * url /${entityNameLow}/${entityNameLow}
     */
    @Test
    public void whenInsertOrUpdateSuccess(){
        // 插入
        Map<String,String> paramMap = new HashMap<>();
        //...自己加其他参数
        //有些数据库不为null字段，暂时不能生成
        sendPost("/${entityNameLow}/${entityNameLow}",paramMap).isOk();
        // 更新
        Map<String,String> updateParamMap = new HashMap<>();
        updateParamMap.put("id","1");
        //...自己加其他参数
        sendPost("/${entityNameLow}/${entityNameLow}",updateParamMap).isOk();
    }

    /**
     * 批量删除的接口
     * 请求方法 post
     * url /${entityNameLow}/${entityNameLow}
     */
    @Test
    public void whenBatchDeleteSuccess(){
        List<Integer> ${entityNameLow}IdList = new ArrayList<>();
        ${entityNameLow}IdList.add(1);
        ${entityNameLow}IdList.add(2);
        ${entityNameLow}IdList.add(3);

        sendPost("/${entityNameLow}/batch/delete",${entityNameLow}IdList).isOk().printMsg();

    }

}
