package com.messoft.gaoqin.wanyiyuan.bean;

public class MemberCapitalLogList {

    /**
     * updateBy : -100
     * createTime : 2019-10-12 17:37:22
     * sort : 0
     * updateTime :
     * code : 10000010000007894783521570873042855
     * type : 6
     * createBy : -100
     * createName :
     * amount : 10
     * id : 26
     * balance : 30
     * isDel : 0
     * updateName :
     * payType : 0
     * account : 17655266008
     * memberId : 12
     */

    private String createTime;
    private String updateTime;
    private String code;
    private String type;
    private String amount;
    private String id;
    private String balance;
    private String payType;
    private String account;
    private String memberId;


    public String getUpdateTime() {
        return updateTime == null ? "" : updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? "" : updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
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
