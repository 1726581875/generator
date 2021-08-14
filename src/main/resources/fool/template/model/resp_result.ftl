package ${basePackagePath}.model;
import java.io.Serializable;
/**
 * @author ${author.author}
 * @date ${author.date}
 * 封装统一返回结果给前端
 */
public class RespResult implements Serializable{
    /**
     * 状态码
     */
    private Integer status;
    /**
     * 状态信息
     */
    private String msg;
    /**
     * 数据内容
     */
    private Object data;

    public RespResult() {}

    public RespResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static RespResult build(){
        return new RespResult();
    }

    public static RespResult success(){
        return new RespResult(200, "success" ,null);
    }
    public static RespResult success(String msg){
        return new RespResult(200, msg ,null);
    }
    public static RespResult success(Object date){
        return new RespResult(200, "success" ,date);
    }
    public static RespResult success(Object date, String msg){
        return new RespResult(200, msg ,date);
    }

    public static RespResult fail(){
        return new RespResult(500, "fail" ,null);
    }
    public static RespResult fail(String msg){
        return new RespResult(500, msg ,null);
    }


    /**
     * 判断是否成功
     * @return
     */
    public  boolean isSuccess(){
        return (status!=null && status.equals(200));
    }

    public Integer getStatus() {
        return status;
    }

    public RespResult setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RespResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public RespResult setData(Object data) {
        this.data = data;
        return this;
    }
    
    
    @Override
    public String toString() {
        return "RespResult{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

}
