package com.cq.start.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

@TableName("orders_deliver_records")
public class OrderDeliverRecords extends SuperEntity<OrderDeliverRecords> {
    private Long orderId;
    private Long createUserId;
    @TableLogic
    private Integer status;
    private String  courierNumber;
    private String  deliverNumber;
    private String remarks;


    @TableField(exist = false)
    private String userName;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCourierNumber() {
        return courierNumber;
    }

    public void setCourierNumber(String courierNumber) {
        this.courierNumber = courierNumber;
    }

    public String getDeliverNumber() {
        return deliverNumber;
    }

    public void setDeliverNumber(String deliverNumber) {
        this.deliverNumber = deliverNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
