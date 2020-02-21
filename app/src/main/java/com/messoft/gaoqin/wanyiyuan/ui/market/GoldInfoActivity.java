package com.messoft.gaoqin.wanyiyuan.ui.market;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.BeanOrderInfoById;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityGoldInfoBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.GoldModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

/**
 * 金豆产品详情
 */
public class GoldInfoActivity extends BaseActivity<ActivityGoldInfoBinding> {

    private GoldModel mModel;
    private String mId;
    private String mMemberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold_info);
    }

    @Override
    protected void initSetting() {
        setTitle("产品详情");
        showContentView();
        mModel = new GoldModel();
        if (getIntent() != null) {
            mId = getIntent().getStringExtra("id");
        }

        loadData();
    }

    @Override
    protected void initListener() {
        bindingView.btnPay.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (BusinessUtils.getBindAccountId().equals(mMemberId)) {
                    ToastUtil.showShortToast("不能操作自己的转让订单");
                    return;
                }
                mModel.rechargeBeanOrderForm(getActivity(), mId, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        ToastUtil.showShortToast("充值成功~");
                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_GS_ORDER, 0));
                    }

                    @Override
                    public void loadFailed(int errorCode, String errorMessage) {
                        ToastUtil.showShortToast(errorMessage);
                    }
                });
            }
        });
    }

    private void loadData() {
        mModel.getBeanOrderInfoById(getActivity(), mId, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                BeanOrderInfoById data = (BeanOrderInfoById) object;
                mMemberId = data.getMemberId();
                setData(data);
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setData(BeanOrderInfoById data) {
        bindingView.number.setText(data.getBeanAmount()+"个");
        bindingView.tvTitle.setText(data.getTitle());
        ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getImgName()),bindingView.ivHeader,0);
        bindingView.tvName.setText(data.getName());
        bindingView.tvPhone.setText(data.getContactMobile());
        bindingView.tvWx.setText(data.getWechat());
        //收款方式
        //(0:微信,1:支付宝,2:银行卡)
        String payType = data.getPayType();
        switch (payType){
            case "0":
                bindingView.ivZfb.setImageResource(R.drawable.wx_48);
                bindingView.payWay.setText("微信");
                break;
            case "1":
                bindingView.ivZfb.setImageResource(R.drawable.zfb_48);
                bindingView.payWay.setText("支付宝");
                break;
            case "2":
                bindingView.ivZfb.setImageResource(R.drawable.yhk);
                bindingView.payWay.setText("银行卡");
                break;
        }
        //实付金额
        bindingView.tvMoney.setText(data.getAmount()+"元");
        //转让金额
        bindingView.tvMoney1.setText(data.getBeanAmount()+"元");
    }

    public static void goPage(Context context, String id) {
        Intent intent = new Intent(context, GoldInfoActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
}
