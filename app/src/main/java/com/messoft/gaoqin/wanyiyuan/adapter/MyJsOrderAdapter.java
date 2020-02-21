package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.roundview.RoundTextView;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.GetRushGoodsList;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemOrderQgBinding;
import com.messoft.gaoqin.wanyiyuan.model.QgModel;
import com.messoft.gaoqin.wanyiyuan.ui.my.WyjsActivity;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;

/**
 * 我的待寄售列表
 */
public class MyJsOrderAdapter extends BaseRecyclerViewAdapter<GetRushGoodsList> {

    private BaseActivity mActivity;
    private QgModel mQgModel;

    public MyJsOrderAdapter(BaseActivity activity) {
        this.mActivity = activity;
        mQgModel = new QgModel();
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_order_qg);
    }

    class ViewHolder extends BaseRecyclerViewHolder<GetRushGoodsList, ItemOrderQgBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final GetRushGoodsList data, final int position) {
            if (data == null) return;
            //抢购和寄售的公共字段
            binding.tvTime.setText(data.getCreateTime());
            if (data.getImgsList() != null && data.getImgsList().size() > 0) {
                ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getImgsList().get(0)), binding.iv, 1, TimeUtils.date2TimeStamp(data.getUpdateTime()));
            }
            binding.tvName.setText(data.getGoodsName());
            binding.tvPrice.setText("￥" + data.getGoodsPrice());

            if (StringUtils.isNoEmpty(data.getEarningRate())) {
                binding.tv1.setText("产品编号：" + data.getEarningRate());
            }else{
                binding.tv1.setText("产品编号：" + data.getGoodsId());
            }

            //待寄售是个商品 没有订单编号
            binding.rlTitle.setVisibility(View.GONE);
            if (data.getStatus().equals("1") && data.getAuditStatus().equals("0")) {
                Long aLongStart = TimeUtils.longTimeToDay1(data.getBeginSwapTime());
                if (aLongStart >= 0) {
                    //可以申请寄售
                    setBtnState(binding.btnRight, View.VISIBLE, "申请寄售", "#FF9800", "#FF9800");
                    //申请寄售
                    binding.btnRight.setOnClickListener(new PerfectClickListener() {
                        @Override
                        protected void onNoDoubleClick(View v) {
                            WyjsActivity.goPage(mActivity, data.getOrderId(), data.getGoodsId(), data.getGoodsTypeId(), data.getGoodsName(),
                                    data.getShopName(),
                                    data.getShopMobile(),
                                    data.getWechat(),
                                    0);
                        }
                    });
                } else {
                    setBtnState(binding.btnRight, View.VISIBLE, "等待寄售", "#999999", "#FF666666");
                }

            } else if (data.getStatus().equals("1") && data.getAuditStatus().equals("1")) {
                //正在等待寄售
                setBtnState(binding.btnRight, View.VISIBLE, "寄售中", "#999999", "#FF666666");
            }
        }
    }

    private void setBtnState(RoundTextView tv, int visible, String msg, String strokeColor, String textColor) {
        if (visible != -1) {
            tv.setVisibility(visible);
        }
        tv.setText(msg);
        tv.getDelegate().setStrokeColor(Color.parseColor(strokeColor));
        tv.setTextColor(Color.parseColor(textColor));
    }

}
