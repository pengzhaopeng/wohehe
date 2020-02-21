package com.messoft.gaoqin.wanyiyuan.bean;

public class SkuList {
    private String id; //SKU id
    private String productId;
    private String productName;
    private int qty; //库存量
    private double price; //商城价
    private String unitValue; //单位值
    private double marketPrice; //结算价
    private String imgAttrValueId; //图片 id
    private String property; //属性
    private String classCode; //标志零售区还是批发区商品等 pfq xrlsq
    private String classId;
    private String className;
    private long points;//积分

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getClassId() {
        return classId == null ? "" : classId;
    }

    public void setClassId(String classId) {
        this.classId = classId == null ? "" : classId;
    }

    public String getClassName() {
        return className == null ? "" : className;
    }

    public void setClassName(String className) {
        this.className = className == null ? "" : className;
    }

    public String getProperty() {
        return property == null ? "" : property;
    }

    public void setProperty(String property) {
        this.property = property == null ? "" : property;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id;
    }

    public String getProductId() {
        return productId == null ? "" : productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? "" : productId;
    }

    public String getUnitValue() {
        return unitValue == null ? "" : unitValue;
    }

    public void setUnitValue(String unitValue) {
        this.unitValue = unitValue == null ? "" : unitValue;
    }

    public String getProductName() {
        return productName == null ? "" : productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? "" : productName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(float marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getImgAttrValueId() {
        return imgAttrValueId == null ? "" : imgAttrValueId;
    }

    public void setImgAttrValueId(String imgAttrValueId) {
        this.imgAttrValueId = imgAttrValueId == null ? "" : imgAttrValueId;
    }

    public String getClassCode() {
        return classCode == null ? "" : classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode == null ? "" : classCode;
    }
}
