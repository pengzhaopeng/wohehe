package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.flyco.roundview.RoundTextView;
import com.jaeger.library.StatusBarUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.GetRushGoodsList;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityMyJsInfoBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.QgModel;
import com.messoft.gaoqin.wanyiyuan.utils.HideViewUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TextViewUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

public class MyJsInfoActivity extends BaseActivity<ActivityMyJsInfoBinding> {

    private QgModel mQgModel;
    private String goodId;
    private RxBus mRxBus;

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_js_info);
    }

    @Override
    protected void initSetting() {
        if (getActivity() != null && getActivity().getWindow() != null) {
            StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.theme_color), 0);
        }
        initRxBus();
        showContentView();
        mQgModel = new QgModel();
        if (getIntent() != null) {
            goodId = getIntent().getStringExtra("id");
            loadData();
        }

    }

    @Override
    protected void initListener() {
        bindingView.ivBack.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
        //折叠寄售方
        bindingView.rlJsfTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideViewUtils.newInstance(getActivity(), bindingView.llJsfContent,
                        bindingView.ivDown).toggle();
            }
        });
    }

    private void loadData() {
        mQgModel.getRushGoodsById(getActivity(), goodId, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                GetRushGoodsList data = (GetRushGoodsList) object;
                if (data != null) {
                    setData(data);
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setData(GetRushGoodsList data) {
        if (data.getImgsList() != null && data.getImgsList().size() > 0) {
            ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getImgsList().get(0)), bindingView.iv, 1, TimeUtils.date2TimeStamp(data.getUpdateTime()));
        }
        bindingView.tvTitle.setText(data.getGoodsName());

//        bindingView.tvNumber.setText("产品编号：" + data.getGoodsId());
        if (StringUtils.isNoEmpty(data.getEarningRate())) {
            bindingView.tvNumber.setText("产品编号：" + data.getEarningRate());
        }else{
            bindingView.tvNumber.setText("产品编号：" + data.getGoodsId());
        }

        bindingView.tvPrice.setText("￥" + data.getGoodsPrice());

        //寄售方
        bindingView.tvJsf.setText(data.getShopName());
        bindingView.tvLxfs.setText(data.getShopMobile());
        bindingView.tvWx.setText(data.getWechat());
        //申请时间
        bindingView.tvTime.setText(data.getCreateTime());

        //(0:微信,1:支付宝,2:银行卡)
        String payType = data.getPayType();
        switch (payType) {
            case "0":
                ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getPayCode()), bindingView.ivMa1);
                bindingView.llErweima.setVisibility(View.VISIBLE);
                TextViewUtils.setTvImg("微信收款码", R.drawable.wx_48, bindingView.ma11, 0);
                break;
            case "1":
                ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getPayCode()), bindingView.ivMa1);
                bindingView.llErweima.setVisibility(View.VISIBLE);
                TextViewUtils.setTvImg("支付宝收款码", R.drawable.zfb_48, bindingView.ma11, 0);
                break;
            case "2":
                bindingView.llYhk.setVisibility(View.VISIBLE);
                bindingView.cardName.setText(data.getCardName());
                bindingView.cardType.setText(data.getBankName() + " (" + data.getBanckBranchName() + ")");
                bindingView.cardCode.setText(data.getCardNo());
                break;
        }

        if (data.getStatus().equals("1") && data.getAuditStatus().equals("0")) {
            Long aLongStart = TimeUtils.longTimeToDay1(data.getBeginSwapTime());
            if (aLongStart >= 0) {
                //可以申请寄售
                setBtnState(bindingView.btnRight, View.VISIBLE, "申请寄售", "#FF9800", "#FF9800");
                //申请寄售
                bindingView.btnRight.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        WyjsActivity.goPage(getActivity(), data.getOrderId(), data.getGoodsId(), data.getGoodsTypeId(), data.getGoodsName(),
                                data.getShopName(),
                                data.getShopMobile(),
                                data.getWechat(),
                                0);
                    }
                });
            } else {
                setBtnState(bindingView.btnRight, View.VISIBLE, "等待寄售", "#999999", "#FF666666");
            }

        } else if (data.getStatus().equals("1") && data.getAuditStatus().equals("1")) {
            //正在等待寄售
            setBtnState(bindingView.btnRight, View.VISIBLE, "寄售中", "#999999", "#FF666666");
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

    /**
     * 注册RxBus
     */
    private void initRxBus() {
        mRxBus = RxBus.getInstanceBus();
        mRxBus.registerRxBus(mRxBus, RxBusMessage.class, rxBusMessage -> {
            //根据事件类型进行处理
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_QG_ORDER_LIST) {
                if (rxBusMessage.getI() == 0) {
                    //刷新数据
                    loadData();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRxBus != null) {
            mRxBus.unSubscribe(this);
        }
    }

    public static void goPage(Context context, String goodId) {
        Intent intent = new Intent(context, MyJsInfoActivity.class);
        intent.putExtra("id", goodId);
        context.startActivity(intent);
    }
}
