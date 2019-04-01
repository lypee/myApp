package ly.pojo.vo;

import java.util.Date;
/**
 * 封装传给前端视图的类
**/
public class userVo {
        private String userName ;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String gender ;
        private String code ;

}
