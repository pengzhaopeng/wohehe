package com.messoft.gaoqin.wanyiyuan.bean;

public class AvailableLimitAmount {

    /**
     * totalLimitAmount : 1
     * availableTotalLimitAmount : 1
     * singleLimitAmount : 1
     * availableSingleLimitAmount : 1
     */

    private String totalLimitAmount;
    private String availableTotalLimitAmount;
    private String singleLimitAmount;
    private String availableSingleLimitAmount;
    private String wholesaleCapital;

    public String getWholesaleCapital() {
        return wholesaleCapital == null ? "" : wholesaleCapital;
    }

    public void setWholesaleCapital(String wholesaleCapital) {
        this.wholesaleCapital = wholesaleCapital == null ? "" : wholesaleCapital;
    }

    public String getTotalLimitAmount() {
        return totalLimitAmount == null ? "" : totalLimitAmount;
    }

    public void setTotalLimitAmount(String totalLimitAmount) {
        this.totalLimitAmount = totalLimitAmount == null ? "" : totalLimitAmount;
    }

    public String getAvailableTotalLimitAmount() {
        return availableTotalLimitAmount == null ? "" : availableTotalLimitAmount;
    }

    public void setAvailableTotalLimitAmount(String availableTotalLimitAmount) {
        this.availableTotalLimitAmount = availableTotalLimitAmount == null ? "" : availableTotalLimitAmount;
    }

    public String getSingleLimitAmount() {
        return singleLimitAmount == null ? "" : singleLimitAmount;
    }

    public void setSingleLimitAmount(String singleLimitAmount) {
        this.singleLimitAmount = singleLimitAmount == null ? "" : singleLimitAmount;
    }

    public String getAvailableSingleLimitAmount() {
        return availableSingleLimitAmount == null ? "" : availableSingleLimitAmount;
    }

    public void setAvailableSingleLimitAmount(String availableSingleLimitAmount) {
        this.availableSingleLimitAmount = availableSingleLimitAmount == null ? "" : availableSingleLimitAmount;
    }
}
