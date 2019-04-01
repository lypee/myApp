package ly.pojo;

import java.io.Serializable;
import java.util.Date;

public class Record  implements Serializable {
    private Integer userId ;
    private Integer recordId ;
    private String recordContent ;
    private Date createTime ;
    private Date updateTime ;
    private Integer status ;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getRecordContent() {
        return recordContent;
    }

    public void setRecordContent(String recordContent) {
        this.recordContent = recordContent;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    //开放共有构造器 给spring


    public Record()
    {

    }
    /**
     * 暂时只写一个 建造记录内容的类
     * todo
     * @param recordBuilder
     */
    //建造者 内部类
    public Record(recordBuilder recordBuilder)
    {
        this.recordContent = recordBuilder.recordContent ;
    }
    //添加记录的静态工厂类
    public static class recordBuilder{
        private String recordContent ;
        public recordBuilder buildRecordContent(String recordContent)
        {
            this.recordContent = recordContent ;
            return this ;
        }
        public Record build(){
            return new Record(this) ;
        }

    }
}
