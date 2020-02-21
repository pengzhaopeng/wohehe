package com.messoft.gaoqin.wanyiyuan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.bean.GetCartList;
import com.messoft.gaoqin.wanyiyuan.bean.NormalBean;
import com.messoft.gaoqin.wanyiyuan.bean.ShopBean;
import com.messoft.gaoqin.wanyiyuan.ui.shopcar.viewholder.ChildViewHolder;
import com.messoft.gaoqin.wanyiyuan.ui.shopcar.viewholder.GroupViewHolder;
import com.messoft.gaoqin.wanyiyuan.ui.shopcar.viewholder.NormalViewHolder;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.shopcar.CartAdapter;
import com.messoft.gaoqin.wanyiyuan.utils.shopcar.viewholder.CartViewHolder;

import java.util.List;

public class ShopCarAdapter extends CartAdapter<CartViewHolder> {



    public ShopCarAdapter(Context context, List datas) {
        super(context, datas);
    }



    @Override
    protected CartViewHolder getNormalViewHolder(View itemView) {
        return new NormalViewHolder(itemView, -1);
    }

    @Override
    protected CartViewHolder getGroupViewHolder(View itemView) {
        return (CartViewHolder) new GroupViewHolder(itemView, R.id.checkbox);
    }

    @Override
    protected CartViewHolder getChildViewHolder(View itemView) {
        return (CartViewHolder) (new ChildViewHolder(itemView, R.id.checkbox) {
            @Override
            public void onNeedCalculate() {
                if (onCheckChangeListener != null) {
                    onCheckChangeListener.onCalculateChanged(null);
                }
            }
        });
    }

    @Override
    protected int getChildItemLayout() {
        return R.layout.activity_main_item_child;
    }

    @Override
    protected int getGroupItemLayout() {
        return R.layout.activity_main_item_group;
    }

    @Override
    protected int getNormalItemLayout() {
        return R.layout.activity_main_item_normal;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ChildViewHolder) {
            ChildViewHolder childViewHolder = (ChildViewHolder) holder;
            childViewHolder.textView.setText(((GetCartList) mDatas.get(position)).getProductName());
            childViewHolder.textViewPrice.setText(
                    mContext.getString(R.string.rmb_X, ((GetCartList) mDatas.get(position)).getSkuPrice()));
            childViewHolder.textViewNum.setText(String.valueOf(((GetCartList) mDatas.get(position)).getSkuQty()));

            //图
            String url = Constants.MASTER_URL + "wyy/img/pimg/" + ((GetCartList) mDatas.get(position)).getProductId() + "/" + ((GetCartList) mDatas.get(position)).getImgAttrValueId() + "/pa" + 1 + "_600_600.jpg";
//            ImgLoadUtil.displayEspImage(url, ChildViewHolder.iv, 1, TimeUtils.date2TimeStamp(mDatas.get(position).getUpdateTime()));
            ImgLoadUtil.displayEspImage(url, childViewHolder.ivImg, 1);
            //属性
            childViewHolder.textViewProperty.setText(((GetCartList) mDatas.get(position)).getSkuProperty());

        } else if (holder instanceof GroupViewHolder) {
            GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
            groupViewHolder.textView.setText(((ShopBean) mDatas.get(position)).getShop_name());
        } else if (holder instanceof NormalViewHolder) {
            NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
            normalViewHolder.imgViewClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mDatas.size());
                }
            });
            normalViewHolder.textView.setText(mContext.getString(R.string.normal_tip_X,
                    ((NormalBean) mDatas.get(position)).getMarkdownNumber()));
        }
    }
}
