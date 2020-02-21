package com.messoft.gaoqin.wanyiyuan.bean;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

public class GetAreaList {


    /**
     * id : 1
     * createTime : 2015-06-02 22:01:40
     * parentId : 111
     * isDel : 0
     * title : 北京市
     * level : 2
     * sort : 0
     * updateTime : 2015-06-02 22:01:40
     */

    private int id;
    private String createTime;
    private String parentId;
    private String isDel;
    private String title;
    private String level;
    private String sort;
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime == null ? "" : createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getParentId() {
        return parentId == null ? "" : parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIsDel() {
        return isDel == null ? "" : isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level == null ? "" : level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSort() {
        return sort == null ? "" : sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getUpdateTime() {
        return updateTime == null ? "" : updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
