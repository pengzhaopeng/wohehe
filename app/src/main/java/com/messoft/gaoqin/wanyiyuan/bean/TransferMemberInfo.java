package com.messoft.gaoqin.wanyiyuan.bean;

import java.io.Serializable;

public class TransferMemberInfo implements Serializable{

    /**
     * memberId : 11
     * account : 13033999585
     * name : *哥
     * imgName : imgName
     */

    private String memberId;
    private String account;
    private String name;
    private String imgName;

    public String getMemberId() {
        return memberId == null ? "" : memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId == null ? "" : memberId;
    }

    public String getAccount() {
        return account == null ? "" : account;
    }

    public void setAccount(String account) {
        this.account = account == null ? "" : account;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public String getImgName() {
        return imgName == null ? "" : imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName == null ? "" : imgName;
    }
}
