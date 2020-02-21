package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.MyBillBean;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemMyBillBinding;

public class MyBillAdapter extends BaseRecyclerViewAdapter<MyBillBean> {

    private BaseActivity mActivity;

    public MyBillAdapter(BaseActivity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_my_bill);
    }


    class ViewHolder extends BaseRecyclerViewHolder<MyBillBean, ItemMyBillBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint({"SetTextI18n", "ResourceAsColor"})
        @Override
        public void onBindViewHolder(final MyBillBean data, final int position) {
//            binding.iv.setBackgroundColor(data.getColorId());
            binding.iv.setBackgroundColor(Color.parseColor(data.getColorId()));
            binding.tvName.setText(data.getTitle());
            binding.tvMoney.setText(data.getMoney());
        }
    }
}
