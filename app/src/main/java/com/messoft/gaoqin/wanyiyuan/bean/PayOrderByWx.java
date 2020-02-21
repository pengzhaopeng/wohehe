package com.messoft.gaoqin.wanyiyuan.bean;

import com.google.gson.annotations.SerializedName;

public class PayOrderByWx {

    /**
     * sign : EC0F2C339D17B3B39BE67F7BEDFDECD51562A24BEC03A6F8EFBF74D58A0F41FE
     * partnerid : 1510055661
     * timeStamp : 1573180776
     * prepayid : wx0810393907426755c4f52c551216031300
     * package : Sign=WXPay
     * appid : wx8b1caefe11f1462f
     * nonceStr : SCkZ8tJsvCwJRDjX8tUJed2IoR3gDheH
     */

    private String sign;
    private String partnerid;
    private String timestamp;
    private String prepayid;
    @SerializedName("package")
    private String packageX;
    private String appid;
    private String noncestr;

    public String getSign() {
        return sign == null ? "" : sign;
    }

    public void setSign(String sign) {
        this.sign = sign == null ? "" : sign;
    }

    public String getPartnerid() {
        return partnerid == null ? "" : partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid == null ? "" : partnerid;
    }


    public String getPrepayid() {
        return prepayid == null ? "" : prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid == null ? "" : prepayid;
    }

    public String getPackageX() {
        return packageX == null ? "" : packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX == null ? "" : packageX;
    }

    public String getAppid() {
        return appid == null ? "" : appid;
    }

    public void setAppid(String appid) {
        this.appid = appid == null ? "" : appid;
    }

    public String getTimestamp() {
        return timestamp == null ? "" : timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp == null ? "" : timestamp;
    }

    public String getNoncestr() {
        return noncestr == null ? "" : noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr == null ? "" : noncestr;
    }
}
