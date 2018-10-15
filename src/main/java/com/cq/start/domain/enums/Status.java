package com.cq.start.domain.enums;

public enum Status {
    Disabled(1, "禁用"),

    Enable(0, "启用");


    private Integer code;

    private String desc;

    Status(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
