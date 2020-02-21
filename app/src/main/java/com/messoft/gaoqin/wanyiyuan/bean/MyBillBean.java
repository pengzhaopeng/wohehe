package com.messoft.gaoqin.wanyiyuan.bean;

public class MyBillBean {
    private String title;
    private String colorId;
    private String money;

    public MyBillBean(String title, String colorId, String money) {
        this.title = title;
        this.colorId = colorId;
        this.money = money;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getMoney() {
        return money == null ? "" : money;
    }

    public void setMoney(String money) {
        this.money = money == null ? "" : money;
    }
}
