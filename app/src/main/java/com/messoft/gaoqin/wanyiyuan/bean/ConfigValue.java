package com.messoft.gaoqin.wanyiyuan.bean;

public class ConfigValue {
    private String value;

    public String getValue() {
        return value == null ? "" : value;
    }

    public void setValue(String value) {
        this.value = value == null ? "" : value;
    }
}
