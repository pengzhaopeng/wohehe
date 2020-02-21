package com.messoft.gaoqin.wanyiyuan.bean;

import java.io.Serializable;

public class BlankCard implements Serializable{

    /**
     * updateBy :
     * createTime : 2019-10-24 09:16:39
     * sort : 0
     * updateTime :
     * typeCode : bank_type_jtyh
     * createBy :
     * createName :
     * cardNo : 6221234567891234
     * id : 7
     * typeName : 交通银行
     * isDel : 0
     * updateName :
     * isDefault : 0
     * name : 蓝震宇
     * branchName : 我们都是好孩子
     * memberId : 15
     */

    private String typeCode;
    private String cardNo;
    private String id;
    private String typeName;
    private String isDefault;
    private String name;
    private String branchName;
    private String memberId;

    public String getTypeCode() {
        return typeCode == null ? "" : typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode == null ? "" : typeCode;
    }

    public String getCardNo() {
        return cardNo == null ? "" : cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? "" : cardNo;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id;
    }

    public String getTypeName() {
        return typeName == null ? "" : typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? "" : typeName;
    }

    public String getIsDefault() {
        return isDefault == null ? "" : isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault == null ? "" : isDefault;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public String getBranchName() {
        return branchName == null ? "" : branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName == null ? "" : branchName;
    }

    public String getMemberId() {
        return memberId == null ? "" : memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId == null ? "" : memberId;
    }
}
