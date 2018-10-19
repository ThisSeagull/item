package com.cq.start.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.cq.start.SuperEntity;

import java.util.Date;
@TableName("system_user")
public class SystemUser extends SuperEntity<SystemUser> {
    private String loginName;
    private String nickName;
    private String password;
    @TableLogic
    private Integer status;
    private Date modifyTime;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
