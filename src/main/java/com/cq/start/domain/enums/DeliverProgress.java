package com.cq.start.domain.enums;

public enum DeliverProgress {
    YES(1, "已发货"),

    NOTYET(0, "未发货"),

    DELIVING(0, "发货中"),
    ;


    private Integer code;

    private String desc;

    DeliverProgress(Integer code, String desc) {
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
