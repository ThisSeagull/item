package com.cq.start.domain.enums;

public enum InvoiceProgress {
    YES(1, "已开票"),

    NOTYET(0, "未开票");


    private Integer code;

    private String desc;

    InvoiceProgress(Integer code, String desc) {
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
