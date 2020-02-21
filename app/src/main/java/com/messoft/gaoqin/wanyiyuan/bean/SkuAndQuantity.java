package com.messoft.gaoqin.wanyiyuan.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * sku + 购买数量
 * 方便下单页面展示
 */
public class SkuAndQuantity implements Parcelable {
    private Sku sku;
    private int quantity;

    public SkuAndQuantity() {
    }

    public SkuAndQuantity(Sku sku, int quantity) {
        this.sku = sku;
        this.quantity = quantity;
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.sku, flags);
        dest.writeInt(this.quantity);
    }

    protected SkuAndQuantity(Parcel in) {
        this.sku = in.readParcelable(Sku.class.getClassLoader());
        this.quantity = in.readInt();
    }

    public static final Parcelable.Creator<SkuAndQuantity> CREATOR = new Parcelable.Creator<SkuAndQuantity>() {
        @Override
        public SkuAndQuantity createFromParcel(Parcel source) {
            return new SkuAndQuantity(source);
        }

        @Override
        public SkuAndQuantity[] newArray(int size) {
            return new SkuAndQuantity[size];
        }
    };
}
