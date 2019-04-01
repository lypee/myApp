package ly.pojo;

import java.io.Serializable;
import java.util.Date;

public class Plan implements Serializable {

    private Integer planId ;
    private Integer userId ;
    private String userName;
    private String planContent;
    private Date createTime;
    private Date updateTime;
    private Integer status;
    private Integer finishTimes;

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getPlanContent() {
        return planContent;
    }

    public void setPlanContent(String planContent) {
        this.planContent = planContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFinishTimes() {
        return finishTimes;
    }

    public void setFinishTimes(int finishTimes) {
        this.finishTimes = finishTimes;
    }
    public Plan()
    {

    }

    /**
     * 建造者模式 主类中开放的共有构造器
     * 暂时只开放三个属性
     * @param planBuilder
     */
    public Plan(planBuilder planBuilder)
    {
        this.planContent = planBuilder.planContent ;
        this.status = planBuilder.status ;
        this.userName = planBuilder .userName ;
        this.userId = planBuilder.userId ;
    }
    public static class planBuilder{
        private String planContent ;
        private Integer status ;
        private String userName ;
        private Integer userId ;
        public Plan build(){return new Plan(this) ;}

        public planBuilder buildPlanContent(String planContent) {
            this.planContent = planContent ;
            return this ;
        }

        public planBuilder buildStatus(Integer status) {
            this.status = status ;
            return this ;
        }

        public planBuilder buildUserName(String userName) {
            this.userName = userName ;
            return this ;
        }

        public planBuilder buildUserId(Integer userId) {
            this.userId = userId ;
            return this ;
        }
    }

}
