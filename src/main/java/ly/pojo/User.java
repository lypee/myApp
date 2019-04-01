package ly.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * User pojo
 * 将序列化后的User 作为value 存入redis
 */
public class User implements Serializable {
    private static final long serialVersionUID = 3487495895819393L;
    //uuid userId
    private int userId ;
    //用户注册名
    private String userName;

    public String getUserOpenId() {
        return userOpenId;
    }

    public void setUserOpenId(String userOpenId) {
        this.userOpenId = userOpenId;
    }

    //用户的唯一openId
    private String userOpenId ;

    //创建时间 , 在数据库中更改
    private Date  createTime ;
    //上一次登陆的时间 , 在后端中不做操作
    private Date lastLoginTime ;
    //用户的手机号
    private String userPhone ;
    //用户的权限
    private Integer role ;
    //用户邮箱
    private String email ;
    //用户性别
    private String gender;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public User(){

    }
    //建造者内部类
    public User(userBuilder userBuilder)
    {
        this.userName = userBuilder.userName ;
        this.email = userBuilder.email ;
        this.gender = userBuilder.gender ;
        this.userId = userBuilder.userId ;
        this.userOpenId = userBuilder.userOpenId ;
    }
    public static class userBuilder{
        public User build(){
            return new User(this) ;
        }
        private String gender ;
        private String email ;
        private String userName ;
        private String userOpenId ;
        private Integer userId ;

        public userBuilder buildGender(String gender) {
            this.gender = gender ;
            return this ;
        }

        public userBuilder buildEmail(String email) {
            this.email = email ;
            return this;
        }

        public userBuilder buildName(String userName) {
            this.userName = userName ;
            return this;
        }


        public userBuilder buildUserId(Integer userId) {
            this.userId = userId ;
            return this ;
        }

        public userBuilder buildUserOpenId(String userOpenId) {
            this.userOpenId = userOpenId ;
            return this ;
        }
    }

}
