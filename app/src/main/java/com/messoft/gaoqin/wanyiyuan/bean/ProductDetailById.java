package com.messoft.gaoqin.wanyiyuan.bean;

public class ProductDetailById {
    private String detailDesc;
    private String specDesc;
    private String packDesc;

    public String getDetailDesc() {
        return detailDesc == null ? "" : detailDesc;
    }

    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc == null ? "" : detailDesc;
    }

    public String getSpecDesc() {
        return specDesc == null ? "" : specDesc;
    }

    public void setSpecDesc(String specDesc) {
        this.specDesc = specDesc == null ? "" : specDesc;
    }

    public String getPackDesc() {
        return packDesc == null ? "" : packDesc;
    }

    public void setPackDesc(String packDesc) {
        this.packDesc = packDesc == null ? "" : packDesc;
    }
}
