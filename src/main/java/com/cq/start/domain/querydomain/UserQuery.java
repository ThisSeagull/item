package com.cq.start.domain.querydomain;

import java.util.Collections;

public class UserQuery {

    private String mobile;
    private String name;
    private long size;
    private long current;

    public UserQuery(){
        this.size = 20L;
        this.current=1L;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
