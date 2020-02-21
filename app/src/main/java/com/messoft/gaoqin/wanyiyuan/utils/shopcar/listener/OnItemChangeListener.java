package com.messoft.gaoqin.wanyiyuan.utils.shopcar.listener;


import com.messoft.gaoqin.wanyiyuan.utils.shopcar.bean.ICartItem;

import java.util.List;

public interface OnItemChangeListener {
    void normalCheckChange(List<ICartItem> beans, int position, boolean isChecked);

    void groupCheckChange(List<ICartItem> beans, int position, boolean isChecked);

    void childCheckChange(List<ICartItem> beans, int position, boolean isChecked);
}
