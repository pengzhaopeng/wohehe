package com.messoft.gaoqin.wanyiyuan.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by wuhenzhizao on 2017/3/6.
 */
public class Sku implements Parcelable {
    private String id;
    private String mainImage;
    private String productId;
    private String productName;
    private String classCode; //标志零售区还是批发区商品等 pfq xrlsq
    private String classId;
    private String unitValue;
    private String payType; //支付方式(业务) 0.任意,1.钱包 2.现金
    private int stockQuantity;
    private boolean inStock;
    private double originPrice;
    private double sellingPrice;
    private long points;
    private List<SkuAttribute> attributes;

    public Sku() {
    }

    public Sku(String id, String mainImage, int stockQuantity, double originPrice, double sellingPrice,
               String productId, String productName,
               String classCode,
               String classId,
               String unitValue,
               String payType,
               long points,
               List<SkuAttribute> attributes) {
        this.id = id;
        this.mainImage = mainImage;
        this.stockQuantity = stockQuantity;
        this.originPrice = originPrice;
        this.sellingPrice = sellingPrice;
        this.productId = productId;
        this.productName = productName;
        this.classCode = classCode;
        this.classId = classId;
        this.unitValue = unitValue;
        this.payType = payType;
        this.points = points;
        this.attributes = attributes;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public String getPayType() {
        return payType == null ? "" : payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? "" : payType;
    }

    public String getClassId() {
        return classId == null ? "" : classId;
    }

    public void setClassId(String classId) {
        this.classId = classId == null ? "" : classId;
    }

    public String getClassCode() {
        return classCode == null ? "" : classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode == null ? "" : classCode;
    }

    public String getUnitValue() {
        return unitValue == null ? "" : unitValue;
    }

    public void setUnitValue(String unitValue) {
        this.unitValue = unitValue == null ? "" : unitValue;
    }

    public String getProductId() {
        return productId == null ? "" : productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? "" : productId;
    }

    public String getProductName() {
        return productName == null ? "" : productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? "" : productName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public double getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(double originPrice) {
        this.originPrice = originPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public List<SkuAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<SkuAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "Sku{" +
                "id='" + id + '\'' +
                ", mainImage='" + mainImage + '\'' +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", classCode='" + classCode + '\'' +
                ", classId='" + classId + '\'' +
                ", unitValue='" + unitValue + '\'' +
                ", payType='" + payType + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", inStock=" + inStock +
                ", originPrice=" + originPrice +
                ", sellingPrice=" + sellingPrice +
                ", points=" + points +
                ", attributes=" + attributes +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.mainImage);
        dest.writeString(this.productId);
        dest.writeString(this.productName);
        dest.writeString(this.classCode);
        dest.writeString(this.classId);
        dest.writeString(this.unitValue);
        dest.writeString(this.payType);
        dest.writeInt(this.stockQuantity);
        dest.writeByte(this.inStock ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.originPrice);
        dest.writeDouble(this.sellingPrice);
        dest.writeLong(this.points);
        dest.writeTypedList(this.attributes);
    }

    protected Sku(Parcel in) {
        this.id = in.readString();
        this.mainImage = in.readString();
        this.productId = in.readString();
        this.productName = in.readString();
        this.classCode = in.readString();
        this.classId = in.readString();
        this.unitValue = in.readString();
        this.payType = in.readString();
        this.stockQuantity = in.readInt();
        this.inStock = in.readByte() != 0;
        this.originPrice = in.readDouble();
        this.sellingPrice = in.readDouble();
        this.points = in.readLong();
        this.attributes = in.createTypedArrayList(SkuAttribute.CREATOR);
    }

    public static final Creator<Sku> CREATOR = new Creator<Sku>() {
        @Override
        public Sku createFromParcel(Parcel source) {
            return new Sku(source);
        }

        @Override
        public Sku[] newArray(int size) {
            return new Sku[size];
        }
    };
}
