package com.messoft.gaoqin.wanyiyuan.bean;

public class OrderStatusCount {

    /**
     * waitPayCount : 1
     * waitDeliveryCount : 0
     * waitReceiptCount : 4
     * waitSigningCount : 0
     * completedCount : 1
     * closedCount : 3
     * cancelCount : 0
     */

    private String waitPayCount;
    private String waitDeliveryCount;
    private String waitReceiptCount;
    private String waitSigningCount;
    private String completedCount;
    private String closedCount;
    private String cancelCount;

    public String getWaitPayCount() {
        return waitPayCount;
    }

    public void setWaitPayCount(String waitPayCount) {
        this.waitPayCount = waitPayCount;
    }

    public String getWaitDeliveryCount() {
        return waitDeliveryCount;
    }

    public void setWaitDeliveryCount(String waitDeliveryCount) {
        this.waitDeliveryCount = waitDeliveryCount;
    }

    public String getWaitReceiptCount() {
        return waitReceiptCount;
    }

    public void setWaitReceiptCount(String waitReceiptCount) {
        this.waitReceiptCount = waitReceiptCount;
    }

    public String getWaitSigningCount() {
        return waitSigningCount;
    }

    public void setWaitSigningCount(String waitSigningCount) {
        this.waitSigningCount = waitSigningCount;
    }

    public String getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(String completedCount) {
        this.completedCount = completedCount;
    }

    public String getClosedCount() {
        return closedCount;
    }

    public void setClosedCount(String closedCount) {
        this.closedCount = closedCount;
    }

    public String getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(String cancelCount) {
        this.cancelCount = cancelCount;
    }
}
