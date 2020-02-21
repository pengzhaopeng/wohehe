package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.MemberSettlementLineList;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemOrderShBinding;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;


/**
 *
 */
public class JsListAdapter extends BaseRecyclerViewAdapter<MemberSettlementLineList> {

    private BaseActivity mActivity;
//    private OrderModel mModel;

    public JsListAdapter(BaseActivity activity) {
        mActivity = activity;
//        mModel = new OrderModel();
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_order_sh);
    }

    class ViewHolder extends BaseRecyclerViewHolder<MemberSettlementLineList, ItemOrderShBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final MemberSettlementLineList data, final int position) {

            if (data != null) {
                //公共字段
                binding.tvTime.setText(data.getCreateTime());

                //订单号
                binding.tvOrderNumber.setText("订单号：" + data.getOrderCode());

                String img = Constants.MASTER_URL + "wyy/img/pimg/" + data.getProductId() + "/p1_200_200.jpg";
                ImgLoadUtil.displayEspImage(img, binding.iv, 1);
                binding.tvName.setText(data.getProductName());
//                binding.tvNumber.setText("x" + data.getProductNum());
                binding.tv1.setText("数量: " + data.getProductNum());
                binding.tvPrice.setText(data.getOrderAmount());

            }
        }
    }

}
