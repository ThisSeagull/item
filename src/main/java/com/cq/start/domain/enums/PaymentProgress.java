package com.cq.start.domain.enums;

public enum PaymentProgress {
    YES(1, "已收款"),

    NOTYET(0, "未收款"),

    PAYMENTING(2, "收款中")
    ;


    private Integer code;

    private String desc;

    PaymentProgress(Integer code, String desc) {
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
