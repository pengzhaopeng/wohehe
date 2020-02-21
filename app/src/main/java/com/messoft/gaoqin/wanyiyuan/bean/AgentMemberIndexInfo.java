package com.messoft.gaoqin.wanyiyuan.bean;

public class AgentMemberIndexInfo {

    /**
     * saleAmount : 0
     * gainsTotalToday : 3520.01
     * transferNum : 0
     * agentAmount : 5.00
     * orderToday : 13
     * deliverNum : 0
     */

    private String saleAmount;
    private String transferNum;
    private String agentAmount;
    private String orderToday;
    private String deliverNum;
    private String agentAmountToday;
    private String saleAmountToday;

    public String getAgentAmountToday() {
        return agentAmountToday == null ? "" : agentAmountToday;
    }

    public void setAgentAmountToday(String agentAmountToday) {
        this.agentAmountToday = agentAmountToday == null ? "" : agentAmountToday;
    }

    public String getSaleAmountToday() {
        return saleAmountToday == null ? "" : saleAmountToday;
    }

    public void setSaleAmountToday(String saleAmountToday) {
        this.saleAmountToday = saleAmountToday == null ? "" : saleAmountToday;
    }

    public String getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(String saleAmount) {
        this.saleAmount = saleAmount;
    }



    public String getTransferNum() {
        return transferNum;
    }

    public void setTransferNum(String transferNum) {
        this.transferNum = transferNum;
    }

    public String getAgentAmount() {
        return agentAmount;
    }

    public void setAgentAmount(String agentAmount) {
        this.agentAmount = agentAmount;
    }

    public String getOrderToday() {
        return orderToday;
    }

    public void setOrderToday(String orderToday) {
        this.orderToday = orderToday;
    }

    public String getDeliverNum() {
        return deliverNum;
    }

    public void setDeliverNum(String deliverNum) {
        this.deliverNum = deliverNum;
    }
}
