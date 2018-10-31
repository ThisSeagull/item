package com.cq.start.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Orders extends SuperEntity<Orders>{

    private String code;//订单号
    private Long createUserId;//订单创建者
    private Long belongUserId;//订单所属者，前台用户id
    private Long sampleId;//产品id
    private Integer realPrice;//实际价格
    private Integer num;
    private Integer deliveryType;//运货方式
    private String remarks;//备注
    private String contractPath;//合同的路径
    private Integer invoiceStatus;//是否开发票
    private Integer freight;//运费
    private Integer totalPrice;//总价是 实际价格*数量 +运费
    private Integer deliverProgress;//发货进度 0未发货 1已发货 2部分发货 部分发货根据有效的发货记录就可以了
    private Integer invoiceProgress;//开票进度 0 未开票 1已开票
    private Integer paymentProgress;//收款进度 0 未收款 1已收款
    private String  deliveryDate;//交货日期
    private Integer realType;
    private String paymentProgressRemarks;
    private String invoiceProgressRemarks;
    private String  createDate;
    @TableLogic
    private Integer status;//0 有效 1删除
    @TableField(exist = false)
    private String  userName;
    @TableField(exist = false)
    private String  createUserName;
    @TableField(exist = false)
    private String  sampleDescription;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(Long belongUserId) {
        this.belongUserId = belongUserId;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public Integer getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(Integer realPrice) {
        this.realPrice = realPrice;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getContractPath() {
        return contractPath;
    }

    public void setContractPath(String contractPath) {
        this.contractPath = contractPath;
    }

    public Integer getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Integer getFreight() {
        return freight;
    }

    public void setFreight(Integer freight) {
        this.freight = freight;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Integer getDeliverProgress() {
        return deliverProgress;
    }

    public void setDeliverProgress(Integer deliverProgress) {
        this.deliverProgress = deliverProgress;
    }

    public Integer getInvoiceProgress() {
        return invoiceProgress;
    }

    public void setInvoiceProgress(Integer invoiceProgress) {
        this.invoiceProgress = invoiceProgress;
    }

    public Integer getPaymentProgress() {
        return paymentProgress;
    }

    public void setPaymentProgress(Integer paymentProgress) {
        this.paymentProgress = paymentProgress;
    }

    public String getSampleDescription() {
        return sampleDescription;
    }

    public void setSampleDescription(String sampleDescription) {
        this.sampleDescription = sampleDescription;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getPaymentProgressRemarks() {
        return paymentProgressRemarks;
    }

    public void setPaymentProgressRemarks(String paymentProgressRemarks) {
        this.paymentProgressRemarks = paymentProgressRemarks;
    }

    public String getInvoiceProgressRemarks() {
        return invoiceProgressRemarks;
    }

    public void setInvoiceProgressRemarks(String invoiceProgressRemarks) {
        this.invoiceProgressRemarks = invoiceProgressRemarks;
    }

    public Integer getRealType() {
        return realType;
    }

    public void setRealType(Integer realType) {
        this.realType = realType;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}

