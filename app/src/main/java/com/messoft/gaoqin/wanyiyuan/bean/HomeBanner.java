package com.messoft.gaoqin.wanyiyuan.bean;

public class HomeBanner {

    private String updateTime;
    private String id;
    private String imgName;
    private String title;
    private String link;

    public String getLink() {
        return link == null ? "" : link;
    }

    public void setLink(String link) {
        this.link = link == null ? "" : link;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
