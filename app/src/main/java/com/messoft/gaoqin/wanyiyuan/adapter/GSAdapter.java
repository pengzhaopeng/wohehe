package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.BeanOrderInfoList;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemGsBinding;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;

public class GSAdapter extends BaseRecyclerViewAdapter<BeanOrderInfoList> {

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_gs);
    }


    class ViewHolder extends BaseRecyclerViewHolder<BeanOrderInfoList, ItemGsBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final BeanOrderInfoList data, final int position) {
            ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getImgName()), binding.ivHeader, 0);
            binding.tvTitle.setText(data.getTitle());
            binding.tvPhone.setText(data.getAccount());
            binding.tvCreditScore.setText("信用评分：" + data.getCreditScore());
            //(0:微信,1:支付宝,2:银行卡)
            String payType = data.getPayType();
            switch (payType) {
                case "0":
                    binding.ivZfb.setImageResource(R.drawable.wx_36);
                    binding.payWay.setText("微信");
                    break;
                case "1":
                    binding.ivZfb.setImageResource(R.drawable.zfb_36);
                    binding.payWay.setText("支付宝");
                    break;
                case "2":
                    binding.ivZfb.setImageResource(R.drawable.yhk_36);
                    binding.payWay.setText("银行卡");
                    break;
            }
            //金额
            binding.number.setText(data.getBeanAmount());
            //时间
            binding.tvTime.setText("挂售时间：" + data.getCreateTime());
        }
    }
}
