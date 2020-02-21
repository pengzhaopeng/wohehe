package com.messoft.gaoqin.wanyiyuan.bean;

import java.io.Serializable;

public class LoginMemberInfo implements Serializable{

    private String creditScore;
    private String sex;
    private String accountId;
    private String alipayCode;
    private String recommenderMobile;
    private String transferLimit;
    private String remarks;
    private String wxpayCode;
    private String capital;
    private String id;
    private String productSaleGains;
    private String recCode;
    private String level;
    private String failureTime;
    private String nickName;
    private String name;
    private String levelName;
    private String openid;
    private String unliquidatedCapital;
    private String roleId;
    private String parentRecCode;
    private String idCard;
    private String distributionGains;
    private double wholesaleCapital;
    private double disabledWholesaleCapital;
    private String imgName;
    private String email;
    private String account;
    private String wechat;
    private String mobile;
    private double shoppingPoints; //购物积分
    private double ecologyPoints; //生态分
    private double cashPoints; //代金券

    public double getDisabledWholesaleCapital() {
        return disabledWholesaleCapital;
    }

    public void setDisabledWholesaleCapital(double disabledWholesaleCapital) {
        this.disabledWholesaleCapital = disabledWholesaleCapital;
    }

    public double getShoppingPoints() {
        return shoppingPoints;
    }

    public void setShoppingPoints(double shoppingPoints) {
        this.shoppingPoints = shoppingPoints;
    }

    public double getEcologyPoints() {
        return ecologyPoints;
    }

    public void setEcologyPoints(double ecologyPoints) {
        this.ecologyPoints = ecologyPoints;
    }

    public double getCashPoints() {
        return cashPoints;
    }

    public void setCashPoints(double cashPoints) {
        this.cashPoints = cashPoints;
    }

    private String isVip; //是否付费(0.否，1.是)

    public String getIsVip() {
        return isVip == null ? "" : isVip;
    }

    public void setIsVip(String isVip) {
        this.isVip = isVip == null ? "" : isVip;
    }

    public String getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(String creditScore) {
        this.creditScore = creditScore;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAlipayCode() {
        return alipayCode;
    }

    public void setAlipayCode(String alipayCode) {
        this.alipayCode = alipayCode;
    }

    public String getRecommenderMobile() {
        return recommenderMobile;
    }

    public void setRecommenderMobile(String recommenderMobile) {
        this.recommenderMobile = recommenderMobile;
    }

    public String getTransferLimit() {
        return transferLimit;
    }

    public void setTransferLimit(String transferLimit) {
        this.transferLimit = transferLimit;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getWxpayCode() {
        return wxpayCode;
    }

    public void setWxpayCode(String wxpayCode) {
        this.wxpayCode = wxpayCode;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductSaleGains() {
        return productSaleGains;
    }

    public void setProductSaleGains(String productSaleGains) {
        this.productSaleGains = productSaleGains;
    }

    public String getRecCode() {
        return recCode;
    }

    public void setRecCode(String recCode) {
        this.recCode = recCode;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFailureTime() {
        return failureTime;
    }

    public void setFailureTime(String failureTime) {
        this.failureTime = failureTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnliquidatedCapital() {
        return unliquidatedCapital;
    }

    public void setUnliquidatedCapital(String unliquidatedCapital) {
        this.unliquidatedCapital = unliquidatedCapital;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getParentRecCode() {
        return parentRecCode;
    }

    public void setParentRecCode(String parentRecCode) {
        this.parentRecCode = parentRecCode;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getDistributionGains() {
        return distributionGains;
    }

    public void setDistributionGains(String distributionGains) {
        this.distributionGains = distributionGains;
    }

    public double getWholesaleCapital() {
        return wholesaleCapital;
    }

    public void setWholesaleCapital(double wholesaleCapital) {
        this.wholesaleCapital = wholesaleCapital;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
