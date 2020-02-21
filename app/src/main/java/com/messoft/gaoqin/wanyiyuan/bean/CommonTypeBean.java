package com.messoft.gaoqin.wanyiyuan.bean;

/**
 * 通用类型处理 类似
 * 1-只工作日送货(双休日、假日不用送);
 * 2-只双休日、假日送货(工作日不用送);
 * 3-工作日、双休日与假日均可送货;其他值-返回任意时间
 */
public class CommonTypeBean {
    private int type;
    private String msg;

    public CommonTypeBean(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
