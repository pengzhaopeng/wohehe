package com.messoft.gaoqin.wanyiyuan.bean;

import java.io.Serializable;

public class GetMemberNotifyList implements Serializable {

    /**
     * updateBy : -100
     * createTime : 2018-09-28 09:36:10
     * sort : 0
     * receiver : 0
     * updateTime : null
     * notifyId : 1
     * type : 1
     * createBy : -100
     * createName :
     * sender : 1
     * content : 111111111
     * id : 30
     * imgName :
     * isDel : 0
     * updateName :
     * nickName : 小戴
     * action :
     * target : 0
     * memberId : 8
     * targetType :
     * isRead : 0
     */

    private String updateBy;
    private String createTime;
    private String receiver;
    private String updateTime;
    private String notifyId;
    private String type;
    private String createBy;
    private String createName;
    private String sender;
    private String content;
    private String id;
    private String imgName;
    private String updateName;
    private String nickName;
    private String action;
    private String target;
    private String actionName;
    private String memberId;
    private String isRead;
    private String notifyCreateTime;

    public GetMemberNotifyList(String createTime, String receiver, String sender, String content, String imgName) {
        this.createTime = createTime;
        this.receiver = receiver;
        this.sender = sender;
        this.content = content;
        this.imgName = imgName;
    }

    public String getNotifyCreateTime() {
        return notifyCreateTime == null ? "" : notifyCreateTime;
    }

    public void setNotifyCreateTime(String notifyCreateTime) {
        this.notifyCreateTime = notifyCreateTime == null ? "" : notifyCreateTime;
    }

    public String getTarget() {
        return target == null ? "" : target;
    }

    public void setTarget(String target) {
        this.target = target == null ? "" : target;
    }

    public String getActionName() {
        return actionName == null ? "" : actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName == null ? "" : actionName;
    }

    public String getUpdateBy() {
        return updateBy == null ? "" : updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? "" : updateBy;
    }

    public String getCreateTime() {
        return createTime == null ? "" : createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? "" : createTime;
    }

    public String getReceiver() {
        return receiver == null ? "" : receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver == null ? "" : receiver;
    }

    public String getUpdateTime() {
        return updateTime == null ? "" : updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? "" : updateTime;
    }

    public String getNotifyId() {
        return notifyId == null ? "" : notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId == null ? "" : notifyId;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type == null ? "" : type;
    }

    public String getCreateBy() {
        return createBy == null ? "" : createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? "" : createBy;
    }

    public String getCreateName() {
        return createName == null ? "" : createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName == null ? "" : createName;
    }

    public String getSender() {
        return sender == null ? "" : sender;
    }

    public void setSender(String sender) {
        this.sender = sender == null ? "" : sender;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content == null ? "" : content;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id;
    }

    public String getImgName() {
        return imgName == null ? "" : imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName == null ? "" : imgName;
    }

    public String getUpdateName() {
        return updateName == null ? "" : updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName == null ? "" : updateName;
    }

    public String getNickName() {
        return nickName == null ? "" : nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? "" : nickName;
    }

    public String getAction() {
        return action == null ? "" : action;
    }

    public void setAction(String action) {
        this.action = action == null ? "" : action;
    }

    public String getMemberId() {
        return memberId == null ? "" : memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId == null ? "" : memberId;
    }

    public String getIsRead() {
        return isRead == null ? "" : isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead == null ? "" : isRead;
    }
}
