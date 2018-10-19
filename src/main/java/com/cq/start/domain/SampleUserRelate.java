package com.cq.start.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
@TableName("sample_user_relate")
public class SampleUserRelate {
    @TableId(value = "ID", type = IdType.AUTO)
    private long id;
    private long sampleId;
    private long userId;
    @TableLogic
    private Integer status;
    private Date createTime;
    private Date modifyTime;

    @TableField(exist = false)
    private String userName;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSampleId() {
        return sampleId;
    }

    public void setSampleId(long sampleId) {
        this.sampleId = sampleId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
