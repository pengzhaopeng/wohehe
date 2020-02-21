package com.messoft.gaoqin.wanyiyuan.ui.shopcar.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.utils.shopcar.viewholder.CartViewHolder;


public class NormalViewHolder extends CartViewHolder {
    public TextView textView;
    public ImageView imgViewClose;

    public NormalViewHolder(View itemView, int chekbox_id) {
        super(itemView, chekbox_id);
        textView = itemView.findViewById(R.id.tv);
        imgViewClose = itemView.findViewById(R.id.img_close);
    }
}
