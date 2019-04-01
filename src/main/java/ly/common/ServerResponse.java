package ly.common;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * 封装返回到前端的类
 * @param <T>
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {

    private int status ;
    private String msg  ;
    private T data ;

    private ServerResponse(int status)
    {
        this.status = status ;
    }

    private ServerResponse(int status, String msg, T data) {
        this.data = data  ;
        this.status = status ;
        this.msg = msg ;
    }
    private ServerResponse(int status , T data)
    {
        this.status  = status ;
        this.data = data  ;
    }
    private ServerResponse(int stataus, String msg) {
        this.msg = msg  ;
        this.status = status ;
    }

    //使序列化中不带此结果
    //序列化中忽略是否成功
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode() ;
    }

    public int getStatus(){return this.status ;}

    public void setStatus(){ this.status = status ;}

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 开放静态方法
     */
    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode() , data) ;
    }

    public static <T> ServerResponse<T> createBySuccess(String msg , T data  )
    {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode() , msg ,data) ;
    }

    public static <T>ServerResponse<T> createBySuccessMessage(String msg)
    {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode() , msg) ;
    }

    public static<T> ServerResponse<T> createByError()
    {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),
                ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode() , errorMessage) ;
    }

    public static<T> ServerResponse<T> createByErrorCodeMessage(int errorCode , String errorMessage)
    {
        return new ServerResponse<T>(errorCode , errorMessage) ;
    }

}

