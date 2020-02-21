package com.messoft.gaoqin.wanyiyuan.bean;

public class HomeMenu {
    private String menuName;
    private int menuRsid;

    public HomeMenu(String menuName, int menuRsid) {
        this.menuName = menuName;
        this.menuRsid = menuRsid;
    }

    public String getMenuName() {
        return menuName == null ? "" : menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? "" : menuName;
    }

    public int getMenuRsid() {
        return menuRsid;
    }

    public void setMenuRsid(int menuRsid) {
        this.menuRsid = menuRsid;
    }
}
