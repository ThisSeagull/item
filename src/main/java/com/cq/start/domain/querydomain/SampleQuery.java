package com.cq.start.domain.querydomain;

import java.util.List;

public class SampleQuery {

    private long size;
    private long current;
    private String userMobile;
    private String userName;
    private String sampleNumber;
    private List<Long> belongUserList;

    public SampleQuery(){
        this.size = 20L;
        this.current=1L;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(String sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public List<Long> getBelongUserList() {
        return belongUserList;
    }

    public void setBelongUserList(List<Long> belongUserList) {
        this.belongUserList = belongUserList;
    }
}
