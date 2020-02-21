package com.messoft.gaoqin.wanyiyuan.bean;

import java.io.Serializable;

public class MemberCapitalWaitLogList implements Serializable{

    /**
     * updateBy : -100
     * createTime : 2019-10-28 14:24:26
     * sort : 0
     * updateTime :
     * code : 011145
     * isArrival : 0
     * type : 0
     * createBy : -100
     * createName :
     * ext : {"cashBackAmount":0.5,"StringervalDays":2,"orderCode":"1572242223406","account":"13033999585","memberId":11,"cashBackNumber":5,"productAmount":100.0,"orderId":14}
     * amount : 10
     * id : 23
     * isDel : 0
     * arrivalTime : 2019-11-07 14:24:26
     * updateName :
     * account : 13033999585
     * memberId : 11
     */

    private String updateBy;
    private String createTime;
    private String sort;
    private String updateTime;
    private String code;
    private String isArrival;
    private String type;
    private String createBy;
    private String createName;
    private String ext;
    private String amount;
    private String id;
    private String isDel;
    private String arrivalTime;
    private String updateName;
    private String account;
    private String memberId;

    private String queueWaitTime;
    private double queueMaxAmount;
    private double queueWaitTotalAmount;

    public String getQueueWaitTime() {
        return queueWaitTime == null ? "" : queueWaitTime;
    }

    public void setQueueWaitTime(String queueWaitTime) {
        this.queueWaitTime = queueWaitTime == null ? "" : queueWaitTime;
    }

    public double getQueueMaxAmount() {
        return queueMaxAmount;
    }

    public void setQueueMaxAmount(double queueMaxAmount) {
        this.queueMaxAmount = queueMaxAmount;
    }

    public double getQueueWaitTotalAmount() {
        return queueWaitTotalAmount;
    }

    public void setQueueWaitTotalAmount(double queueWaitTotalAmount) {
        this.queueWaitTotalAmount = queueWaitTotalAmount;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIsArrival() {
        return isArrival;
    }

    public void setIsArrival(String isArrival) {
        this.isArrival = isArrival;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
