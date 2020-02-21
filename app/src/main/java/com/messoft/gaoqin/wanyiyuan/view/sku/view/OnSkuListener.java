package com.messoft.gaoqin.wanyiyuan.view.sku.view;

import com.messoft.gaoqin.wanyiyuan.bean.Sku;
import com.messoft.gaoqin.wanyiyuan.bean.SkuAttribute;

public interface OnSkuListener {
    /**
     * 属性取消选中
     *
     * @param unselectedAttribute
     */
    void onUnselected(SkuAttribute unselectedAttribute);

    /**
     * 属性选中
     *
     * @param selectAttribute
     */
    void onSelect(SkuAttribute selectAttribute);

    /**
     * sku选中
     *
     * @param sku
     */
    void onSkuSelected(Sku sku);
}