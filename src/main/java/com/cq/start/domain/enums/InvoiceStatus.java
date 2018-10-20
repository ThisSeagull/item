package com.cq.start.domain.enums;

public enum InvoiceStatus {
    Wanted(1, "不需要开发票"),

    Unwanted(0, "需要开发票");


    private Integer code;

    private String desc;

    InvoiceStatus(Integer code, String desc) {
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
