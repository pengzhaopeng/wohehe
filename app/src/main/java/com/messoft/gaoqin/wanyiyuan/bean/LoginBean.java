package com.messoft.gaoqin.wanyiyuan.bean;

public class LoginBean {

    private String accessToken;
    private int id;
    private String secret;
    private String account;

    public String getAccessToken() {
        return accessToken == null ? "" : accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken == null ? "" : accessToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSecret() {
        return secret == null ? "" : secret;
    }

    public void setSecret(String secret) {
        this.secret = secret == null ? "" : secret;
    }

    public String getAccount() {
        return account == null ? "" : account;
    }

    public void setAccount(String account) {
        this.account = account == null ? "" : account;
    }
}
