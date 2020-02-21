package com.messoft.gaoqin.wanyiyuan.utils.shopcar.bean;

import java.util.List;



public interface IGroupItem<CHILD extends IChildItem> extends ICartItem {
    List<CHILD> getChilds();

    void setChilds(List<CHILD> childs);
}
