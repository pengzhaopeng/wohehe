package com.messoft.gaoqin.wanyiyuan.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/26 0026.
 * RxBus ben
 */

public class RxBusMessage<T> implements Serializable {

    private int type;
    private int i;
    private T data;
    private String msg;

    public RxBusMessage(T data) {
        this.data = data;
    }

    public RxBusMessage(int type, T data) {
        this.type = type;
        this.data = data;
    }

    public RxBusMessage(int type, T data, int position) {
        this.type = type;
        this.data = data;
        this.i = position;
    }

    public RxBusMessage(int type, int i) {
        this.type = type;
        this.i = i;
    }

    public RxBusMessage(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public RxBusMessage(int type, int i, String msg) {
        this.type = type;
        this.i = i;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? "" : msg;
    }
}
