package com.messoft.gaoqin.wanyiyuan.bean;

public class DictionaryList {

    /**
     * id : 181
     * createTime : 2018-12-07 16:20:38
     * isEdit : 0
     * sort : 0
     * isDefault : 0
     * name : 建设银行
     * code : bank_type_jsyh
     * comment : 建设银行
     * parentCode : bank_type
     * createBy : 999999
     * createName : 系统管理员
     * isValid : 0
     */

    private String id;
    private String name;
    private String code;
    private String comment;
    private String parentCode;
    private String isValid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
}
