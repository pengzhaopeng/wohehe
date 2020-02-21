package com.messoft.gaoqin.wanyiyuan.bean;

/**
 * Created by Administrator on 2017/9/22 0022.
 * json返回数据的封装 单个数据
 */

public class BaseBean<T>{
    //可能是state或者status
    private int state;
    private String message;
    private T data;    //泛型T来表示object，可能是数组，也可能是对象
    private int total;

    public boolean isSuccess() {
        if (getState() == 0) {
            return true;
        }
        return false;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public void setMessage(String message) {
        this.message = message == null ? "" : message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
