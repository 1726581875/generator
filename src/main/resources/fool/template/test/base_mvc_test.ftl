package ${basePackagePath};

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author ${author.author}
 * @date: ${author.date}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//配置事务进行回滚，不产生脏数据，并且便于测试用例的循环利用
@Transactional
public class BaseMvcTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    protected MockMvc mockMvc;


    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                //.alwaysExpect(MockMvcResultMatchers.status().isOk())
                //.alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    /**
     * 发送一个get请求
     * @param url
     * @param paramMap
     * @return
     * @throws Exception
     */
    public Expect sendGet(String url, Map<String,String> paramMap){
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url);
        if (!CollectionUtils.isEmpty(paramMap)) {
            paramMap.forEach((key,value) -> requestBuilder.param(key,value));
        }

        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(requestBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Expect(resultActions);
    }

    /**
     * 发送一个post请求
     * @param url
     * @param paramMap
     * @return
     * @throws Exception
     */
    public Expect sendPost(String url, Map<String,Object> paramMap){
        ResultActions resultActions = null;
        try {
        String jsonStr = objectMapper.writeValueAsString(paramMap);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url);
        requestBuilder.contentType(MediaType.APPLICATION_JSON).content(jsonStr);
        resultActions = mockMvc.perform(requestBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Expect(resultActions);
    }

    public Expect sendPost(String url, Object paramObject){
        ResultActions resultActions = null;
        try {
        String jsonStr = objectMapper.writeValueAsString(paramObject);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url);
        requestBuilder.contentType(MediaType.APPLICATION_JSON)
                .content(jsonStr);

            resultActions = mockMvc.perform(requestBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Expect(resultActions);
    }

    public Expect sendDelete(String url, Map<String,String> paramMap){
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(url);
        if (!CollectionUtils.isEmpty(paramMap)) {
            paramMap.forEach((key,value) -> requestBuilder.param(key,value));
        }
        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(requestBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Expect(resultActions);
    }


    public Expect sendPut(String url, Map<String,String> paramMap){
        ResultActions resultActions = null;
        try {
        String jsonStr = objectMapper.writeValueAsString(paramMap);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(url);
        requestBuilder.contentType(MediaType.APPLICATION_JSON)
                .content(jsonStr);

            resultActions = mockMvc.perform(requestBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Expect(resultActions);
    }

    /**
     * 上传文件
     * @param url
     * @param paramName
     * @param fileFullPath
     * @return
     * @throws Exception
     */
    public Expect sendFile(String url,String paramName,String fileFullPath){
        MockMultipartHttpServletRequestBuilder requestBuilder = null;
        ResultActions resultActions = null;
        try {
        File file = new File(fileFullPath);
        try(InputStream fileInputStream = new FileInputStream(file);) {
            requestBuilder = MockMvcRequestBuilders.multipart(url)
                    .file(new MockMultipartFile(paramName, file.getName(), "multipart/form-data", fileInputStream));
        }
        resultActions = mockMvc.perform(requestBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Expect(resultActions);
    }

    /**
     * 上传一个Text文件
     * @param url
     * @param paramName
     * @param fileName
     * @param context
     * @return
     * @throws Exception
     */
    public Expect sendTextFile(String url, String paramName,String fileName, String context){
        MockMultipartHttpServletRequestBuilder requestBuilder = null;
        ResultActions resultActions = null;
        try {
        requestBuilder = MockMvcRequestBuilders.multipart(url)
                .file(new MockMultipartFile(paramName, fileName, "multipart/form-data", context.getBytes("UTF-8")));
        resultActions = mockMvc.perform(requestBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Expect(resultActions);
    }

   public static class Expect{

        private ResultActions resultActions;

        public  Expect(ResultActions resultActions){
            this.resultActions = resultActions;
        }

        /**
         * 提供一个外部获取ResultActions的方法，提高可用性
         * @return ResultActions
         */
        public ResultActions getResultActions(){
            return this.resultActions;
        }

        public int getStatus(){
            return resultActions.andReturn().getResponse().getStatus();
        }

        /**
         * 期望http状态200
         * @return
         */
        public Expect isOk(){
            try {
                resultActions.andExpect(MockMvcResultMatchers.status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

       public Expect printMsg(){
           try {
               resultActions.andDo(MockMvcResultHandlers.print());
           } catch (Exception e) {
               e.printStackTrace();
           }
           return this;
       }

        /**
         * 期望http状态500
         * @return
         */
        public Expect error500(){
            try {
                resultActions.andExpect(MockMvcResultMatchers.status().is(500));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Expect error404(){
            try {
                resultActions.andExpect(MockMvcResultMatchers.status().is(404));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Expect error415(){
            try {
                resultActions.andExpect(MockMvcResultMatchers.status().is(415));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        /**
         * 获取返回结果json字符串
         * @return
         */
        public String getResultJson(){
            String result = null;
            try {
                result = resultActions.andReturn().getResponse().getContentAsString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }

        /**
         * 期望返回的json对应键的值为对应值
         * @param key 键
         * @param expectValue 期望值
         * @return
         * @throws Exception
         */
        public Expect expectIs(String key,String expectValue){
            try {
                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$." + key).value(expectValue));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        /**
         * 期望返回的json对应键的值不为null
         * @param keyName
         * @return
         * @throws Exception
         */
        public Expect notNull(String keyName){
            try {
                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$." + keyName).isNotEmpty());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }


    }


}