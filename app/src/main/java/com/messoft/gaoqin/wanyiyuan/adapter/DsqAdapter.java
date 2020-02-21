package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.GetMemberWholesaleRushListByCache;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.bean.WholesaleRush;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemDsqBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.GoldModel;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;

/**
 * 定时抢批发额度
 */
public class DsqAdapter extends BaseRecyclerViewAdapter<GetMemberWholesaleRushListByCache> {

    private GoldModel mGoldModel;
    private BaseActivity mActivity;

    public DsqAdapter(BaseActivity activity) {
        mGoldModel = new GoldModel();
        mActivity = activity;
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_dsq);
    }


    class ViewHolder extends BaseRecyclerViewHolder<GetMemberWholesaleRushListByCache, ItemDsqBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final GetMemberWholesaleRushListByCache data, final int position) {
            if (data == null) return;
            binding.tvTime.setText(TimeUtils.timeFormat(data.getBeginTime(), "HH:mm:ss") + " - " +
                    TimeUtils.timeFormat(data.getEndTime(), "HH:mm:ss"));
            int state = data.getState(); //状态（0.未开始，1.进行中，2.已结束，3.已抢完）
            switch (state) {
                case 0:
                    binding.tvStatus.setText("尚未开始");
                    binding.tvStatus.setBackgroundResource(R.drawable.qg_2);
                    break;
                case 1:
                    binding.tvStatus.setText("立即抢购");
                    binding.tvStatus.setBackgroundResource(R.drawable.qg_1);
                    break;
                case 2:
                    binding.tvStatus.setText("已结束");
                    binding.tvStatus.setBackgroundResource(R.drawable.qg_3);
                    break;
                case 3:
                    binding.tvStatus.setText("已抢完");
                    binding.tvStatus.setBackgroundResource(R.drawable.qg_2);
                    break;
            }
            binding.tvStatus.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    switch (state) {
                        case 0:
                            ToastUtil.showShortToast("尚未开始");
                            break;
                        case 1:
                            mGoldModel.wholesaleRush(mActivity, "" + data.getId(), new RequestImpl() {
                                @Override
                                public void loadSuccess(Object object) {
                                    WholesaleRush bean = (WholesaleRush) object;
                                    shopPop(bean.getAmount(),0);
                                    RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_PFED, 0));
                                }

                                @Override
                                public void loadFailed(int errorCode, String errorMessage) {
                                    ToastUtil.showShortToast(errorMessage);
                                    if (errorCode==2) {
                                        shopPop(null,1);
                                    }
                                }
                            });
                            break;
                        case 2:
                            ToastUtil.showShortToast("立即抢购");
                            break;
                        case 3:
                            ToastUtil.showShortToast("已抢完");
                            break;
                    }
                }
            });
        }
    }

    /**
     * @param amount
     * @param type   0-成功 1-失败
     */
    private void shopPop(String amount, int type) {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_dsq)
                .setConvertListener((ViewConvertListener) (holder, dialog) -> {
                    TextView tvTitle = holder.getView(R.id.tv_title);
                    TextView tvMoney = holder.getView(R.id.tv_money);
                    TextView tvMsg = holder.getView(R.id.tv_msg);
                    TextView tvBottom = holder.getView(R.id.tv_bottom);
                    RoundTextView ivSure = holder.getView(R.id.btn);
                    if (type == 0) {
                        tvTitle.setText("恭喜你获得");
                        tvMoney.setText(amount);
                        tvMsg.setText("可用批发额度");
                        tvBottom.setVisibility(View.VISIBLE);
                    }else{
                        tvTitle.setText("非常遗憾");
                        tvMoney.setText("未抢到");
                        tvMsg.setText("可参与下一轮抢购");
                        tvBottom.setVisibility(View.GONE);
                    }
                    ivSure.setOnClickListener(new PerfectClickListener() {
                        @Override
                        protected void onNoDoubleClick(View v) {
                            dialog.dismiss();
                        }
                    });
                })
                .setDimAmount(0.3f)
//                .setShowBottom(true)
                .setMargin(24)
                .setOutCancel(false)
                .show(mActivity.getSupportFragmentManager());
    }
}
