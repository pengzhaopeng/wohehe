package com.messoft.gaoqin.wanyiyuan.bean;

public class GetNewsList {

    /**
     * content : 新闻内容<span>新闻内容</span><span>新闻内容</span><span>新闻内容</span>
     * id : 5
     * createTime : 2017-09-28 16:30:49
     * isDel : 0
     * title : 标题
     * updateTime : 2017-09-28 16:50:11
     * newsTypeId : 0
     */

    private String content;
    private String createTime;
    private String title;
    private String updateTime;

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content == null ? "" : content;
    }

    public String getCreateTime() {
        return createTime == null ? "" : createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? "" : createTime;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }

    public String getUpdateTime() {
        return updateTime == null ? "" : updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? "" : updateTime;
    }
}
