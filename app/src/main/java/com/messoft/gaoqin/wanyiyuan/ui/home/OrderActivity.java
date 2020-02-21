package com.messoft.gaoqin.wanyiyuan.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.OrderProductListAdapter;
import com.messoft.gaoqin.wanyiyuan.alipay.MyALipayUtils;
import com.messoft.gaoqin.wanyiyuan.app.ActivityManage;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.AddressBean;
import com.messoft.gaoqin.wanyiyuan.bean.ExistPayPassword;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.bean.OrderInfo;
import com.messoft.gaoqin.wanyiyuan.bean.PayOrderByWx;
import com.messoft.gaoqin.wanyiyuan.bean.PayOrderByZfb;
import com.messoft.gaoqin.wanyiyuan.bean.PaymentMethodList;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.bean.SkuAndQuantity;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityOrderBinding;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.AddressModel;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.model.PayModel;
import com.messoft.gaoqin.wanyiyuan.ui.my.AddAddressActivity;
import com.messoft.gaoqin.wanyiyuan.ui.my.AddressActivity;
import com.messoft.gaoqin.wanyiyuan.ui.my.InputLoginPwdActivity;
import com.messoft.gaoqin.wanyiyuan.ui.my.SetPayPwdOneActivity;
import com.messoft.gaoqin.wanyiyuan.ui.my.WalletActivity;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DebugUtil;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.NumberUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.PayPassDialog.PayPassDialog;
import com.messoft.gaoqin.wanyiyuan.view.PayPassDialog.PayPassView;
import com.messoft.gaoqin.wanyiyuan.wxapi.WXPay;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 下单界面
 * 2010/12/27 改动：积分商城不会跟优惠券混搭 不会同时出现
 */
public class OrderActivity extends BaseActivity<ActivityOrderBinding> {

    private AddressModel mAddressModel;
    private LoginModel mLoginModel;
    private PayModel mPayModel;
    private RxBus mRxBus;
    private String mAid = null;//收货地址id
    private int mType; // 0-商品详情过来下单  9-生活必备不需要各种限制
    private List<SkuAndQuantity> mData;

    private OrderProductListAdapter mProductListAdapter;
    private String mAddressId = null;

    private int totalNum = 0;
    private double totalMoney = 0.00; //商品的总额不包括各种抵扣完的总额 计算订单总额就不动他
    private double totalMoneyUse = 0.00; //抵扣完最后要提交的总额
    private long totalPoint = 0;
    private OrderInfo mOrderInfo;//提交订单后
    private double mCapital = 0.00;//个人余额
    private String mClassCode;
    private int isPickUp = 0; //是否提货 0.不提货 1.提货
    private String mPayType; //支付方式 0-任意 1-钱包 2-现金 3-代金券+钱包 4-代金券+现金 5-代金券+任意
    private double mCashPoints = 0; //我的代金券
    private double mCashPointsUse = 0;//抵扣的代金券 用于提交订单

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initSetting() {
        showContentView();
        setTitle("提交订单");
        initRxBus();
        mAddressModel = new AddressModel();
        mPayModel = new PayModel();
        mLoginModel = new LoginModel();
        mProductListAdapter = new OrderProductListAdapter(getActivity());
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
            mData = getIntent().getParcelableArrayListExtra("data");

            initOnRefresh(getActivity(), bindingView.rc);
            mProductListAdapter.addAll(mData);
            bindingView.rc.setAdapter(mProductListAdapter);

            //遍历data 算出总数量和总金额
            for (SkuAndQuantity datum : mData) {
                totalNum += datum.getQuantity();
                totalMoney += (datum.getSku().getSellingPrice() * datum.getQuantity());
                totalPoint += datum.getQuantity() * datum.getSku().getPoints();
            }
            totalMoneyUse = totalMoney;
            bindingView.tvTotalNumber.setText("共" + totalNum + "件，");
//            bindingView.tvTotalPrice.setText(NumberUtils.formatNumber(totalMoney));
            BusinessUtils.setPriceAndPoints(bindingView.tvTotalPrice, 13, totalPoint, totalMoneyUse);

            //批发区商品要展示提货按钮
            String classCode = mData.get(0).getSku().getClassCode();
            if (classCode.equals("pfq")) {
                bindingView.llTh.setVisibility(View.VISIBLE);
            }
            mPayType = mData.get(0).getSku().getPayType();
            //根据payType是否显示代金券抵扣 并计算
            if (mPayType.equals("3") || mPayType.equals("4") || mPayType.equals("5")) {
                //获取我的优惠券
                mLoginModel.getLoginMemberInfo(getActivity(), new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        LoginMemberInfo bean = (LoginMemberInfo) object;
                        if (bean != null) {
                            bindingView.rlDjq.setVisibility(View.VISIBLE);
                            mCashPoints = bean.getCashPoints();
                            //代金券 是否 大于商品总额
                            if (mCashPoints <= totalMoney) {
                                bindingView.tvDjq.setText(getString(R.string.djq,String.valueOf(mCashPoints), String.valueOf(mCashPoints)) );
                            } else {
                                bindingView.tvDjq.setText(getString(R.string.djq, String.valueOf(totalMoney), String.valueOf(totalMoney)));
                            }
                        }
                    }

                    @Override
                    public void loadFailed(int errorCode, String errorMessage) {
                        ToastUtil.showShortToast(errorMessage);
                    }
                });
            } else {
                bindingView.rlDjq.setVisibility(View.GONE);
            }
        }

        loadAddress();
    }

    @Override
    protected void initListener() {
        //跳转收货地址 有收货地址
        bindingView.rlHasAddress.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (mOrderInfo != null) {
                    ToastUtil.showShortToast("订单已提交，不能切换");
                    return;
                }
                AddressActivity.goPage(getActivity(), 1);
            }
        });
        //跳转收货地址 无收货地址
        bindingView.rlNoAddress.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                AddAddressActivity.goPage(getActivity(), 1);
            }
        });
        //不提货
        bindingView.llTh1.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (mOrderInfo != null) {
                    ToastUtil.showShortToast("订单已提交，不能切换");
                    return;
                }
                isPickUp = 0;
                bindingView.tvTh1.setTextColor(Color.parseColor("#FF9800"));
                bindingView.tvTh11.setTextColor(Color.parseColor("#FF9800"));
                bindingView.llTh1.setBackgroundResource(R.drawable.shape_tuihuo_select);

                bindingView.tvTh2.setTextColor(Color.parseColor("#333333"));
                bindingView.tvTh21.setTextColor(Color.parseColor("#333333"));
                bindingView.llTh2.setBackgroundResource(R.drawable.shape_tuihuo_normal);
            }
        });
        //提货
        bindingView.llTh2.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (mOrderInfo != null) {
                    ToastUtil.showShortToast("订单已提交，不能切换");
                    return;
                }
                isPickUp = 1;
                bindingView.tvTh2.setTextColor(Color.parseColor("#FF9800"));
                bindingView.tvTh21.setTextColor(Color.parseColor("#FF9800"));
                bindingView.llTh2.setBackgroundResource(R.drawable.shape_tuihuo_select);


                bindingView.tvTh1.setTextColor(Color.parseColor("#333333"));
                bindingView.tvTh11.setTextColor(Color.parseColor("#333333"));
                bindingView.llTh1.setBackgroundResource(R.drawable.shape_tuihuo_normal);
            }
        });

        //代金券勾选
        bindingView.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mOrderInfo != null) {
                    ToastUtil.showLongToast("订单已经生成，请直接支付当前订单~");
                    return;
                }
                if (isChecked) { //选中
                    if (mCashPoints <= totalMoney) { //代金券 小于 总额
                        totalMoneyUse = totalMoney - mCashPoints;

                        //计算需要抵扣的优惠券 用于提交订单
                        mCashPointsUse = mCashPoints; //全抵
                    } else {
                        totalMoneyUse = 0;

                        mCashPointsUse = totalMoney;//部分抵扣 值是商品总额
                    }
                    BusinessUtils.setPriceAndPoints(bindingView.tvTotalPrice, 13, totalPoint, totalMoneyUse);
                } else { //不选
                    //复位一下
                    totalMoneyUse = totalMoney;
                    mCashPointsUse = 0;//不抵扣
                    BusinessUtils.setPriceAndPoints(bindingView.tvTotalPrice, 13, totalPoint, totalMoney);
                }
            }
        });

        //下单
        bindingView.tvOrder.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                //先提交订单 在 查询钱包余额 再支付
                //如果已经提交就不用提交
                if (mOrderInfo != null) {
                    //查询钱包余额
                    checkQianBao();
                } else {
//                    goOrder();
                    checkDjqOrPoints();
                }
            }
        });
    }

    /**
     * 2019/12/27 改动
     * 先判断是否全部能用优惠券抵扣掉 或者 全部积分抵扣掉
     * 如果能就不用去后续操作 直接验证完密码提交订单就完成
     * 如果不能就进行后续操作 跟第一期一样改
     */
    private void checkDjqOrPoints() {
        if (!StringUtils.isNoEmpty(mAddressId)) {
            ToastUtil.showShortToast("请选择收货地址");
            return;
        }
        if (totalMoneyUse == 0) { //直接全部用积分抵扣，输入密码 提交订单就完成
            showPayPwdPop(1);
        } else { //继续走一期提交订单流程
            goOrder(0);
        }
//        if (mClassCode.equals("jfsc")) {//积分商城的 不用考虑优惠券
//            if (totalMoneyUse == 0) { //直接全部用积分抵扣，输入密码 提交订单就完成
//                showPayPwdPop(1);
//            } else { //继续走一期提交订单流程
//                goOrder(0);
//            }
//        }else {
//
//        }
    }

    /**
     * 提交订单
     * changeType=0  一期的代码不动
     * changeType=1  积分或者代金券完全够用 直接验证完密码就提交订单就完事
     */
    @SuppressWarnings("unchecked")
    private void goOrder(int changeType) {
        if (!StringUtils.isNoEmpty(BusinessUtils.getBindAccountId())) {
            ToastUtil.showShortToast("下单人id 不能为空");
            return;
        }
        if (!StringUtils.isNoEmpty(mAddressId)) {
            ToastUtil.showShortToast("请选择收货地址");
            return;
        }
        //获取订单项JSON数组.
        JSONArray orderItemDataArr = new JSONArray();
        try {
            for (SkuAndQuantity datum : mData) {
                JSONObject orderItemDataObject = new JSONObject();
                orderItemDataObject.put("skuId", datum.getSku().getId());
                orderItemDataObject.put("skuQty", datum.getQuantity());
                orderItemDataArr.put(orderItemDataObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //送货类型 暂取第一条
//        ArrayList<Map<String, String>> houseRes = new ArrayList<>();
//        RecyclerView recy = findViewById(R.id.recy_setting);
//        for (int i = 0; i < recy.getChildCount(); i++) {
//            Map<String, String> mDeviceHeaderMap = new HashMap<>();
//            RelativeLayout layout = (RelativeLayout) recy.getChildAt(i);
//            TextView tv_content = layout.findViewById(R.id.tv_content);
//            EditText tv_description = layout.findViewById(R.id.tv_content_description);
//            mDeviceHeaderMap.put(tv_content.getText().toString(), tv_description.getText().toString());
//            houseRes.add(mDeviceHeaderMap);
//        View childAt = bindingView.rc.getChildAt(0);
//        TextView tvPersongType = childAt.findViewById(R.id.tv_peisong_type);
//        //取第一个item 的 textview保存的tag id
//        int firstDeliveryType = 1;
//        if (tvPersongType.getTag(0) != null) {
//            firstDeliveryType = (int) tvPersongType.getTag(0);
//        }
        //配送方式 取第一条
//        String firstDeliveryType = mProductListAdapter.getPsWayAry().valueAt(0);
        String firstDeliveryType = "3";

        //备注信息 同样取第一条
        String firstRemark = mProductListAdapter.getEtTextAry().valueAt(0);

        //提交
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberId", BusinessUtils.getBindAccountId());
            jsonObject.put("receiveId", mAddressId);
            jsonObject.put("orderItemData", orderItemDataArr.toString());
            jsonObject.put("deliveryType", firstDeliveryType);
            jsonObject.put("remark", firstRemark);
            jsonObject.put("isPickUp", isPickUp);
            int type = mData.get(0).getSku().getClassCode().equals("jfsc") ? 2 : 1;
            jsonObject.put("orderType", type);//订单类型：1.商城订单 2.积分订单
            jsonObject.put("useVoucher", mCashPointsUse); //优惠券

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().addOrderForm(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(this.bindToLifecycle())
                .subscribe(new BaseObserver<List<OrderInfo>>(getActivity(), true, "提交订单中...") {
                    @Override
                    protected void onSuccess(List<OrderInfo> login) {
                        if (login != null && login.size() > 0 && login.get(0) != null) {
                            mOrderInfo = login.get(0);

                            if (login.get(0).getOrderItemData() != null && login.get(0).getOrderItemData().size() > 0 &&
                                    login.get(0).getOrderItemData().get(0) != null) {
                                mClassCode = login.get(0).getOrderItemData().get(0).getClassCode();
                            }
                            //提交订单就不能勾选优惠券勾选框
                            bindingView.checkbox.setEnabled(false);
                            if (changeType == 0) {
                                ToastUtil.showShortToast("提交订单成功~");
                                //查询钱包余额
                                checkQianBao();
                            } else if (changeType == 1) {
                                OrderResultActivity.goPage(getActivity(), mClassCode);
                                finish();
                            }

                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        ToastUtil.showShortToast(msg);
                    }
                });
    }

    /**
     * 先查询钱包余额(通过查询登录人信息)
     */
    private void checkQianBao() {
        mLoginModel.getLoginMemberInfo(getActivity(), new RequestImpl() {

            @Override
            public void loadSuccess(Object object) {
                LoginMemberInfo bean = (LoginMemberInfo) object;
                if (bean != null && bean.getCapital() != null) {
                    mCapital = Double.parseDouble(bean.getCapital());
                    showOrderPayPop();
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    /**
     * 弹出确认支付框
     */
    //选中按钮
    boolean ckQb = true;
    boolean ckZfb = false;
    boolean ckWx = false;

    private void showOrderPayPop() {
        ckQb = true;
        ckZfb = false;
        ckWx = false;
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_pay_money_sure)
                .setConvertListener(new ViewConvertListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        TextView tvMoney = holder.getView(R.id.tv_money);
                        TextView qianBaoMoney = holder.getView(R.id.tv_qianbao_money);
                        TextView qbye = holder.getView(R.id.tv_qbye);
                        TextView tvSure = holder.getView(R.id.tv_next);
                        tvMoney.setText("确认支付" + NumberUtils.formatNumber(totalMoneyUse) + "元");
                        qianBaoMoney.setText("钱包余额：" + mCapital);
                        qbye.setText("钱包余额：" + mCapital);
                        tvSure.setText("确认支付" + NumberUtils.formatNumber(totalMoneyUse) + "元");
                        CheckBox cbQb = holder.getView(R.id.cb_qb);
                        CheckBox cbZfb = holder.getView(R.id.cb_zfb);
                        CheckBox cbWx = holder.getView(R.id.cb_wx);

//                        cbQb.setOnCheckedChangeListener(this);
//                        cbZfb.setOnCheckedChangeListener(this);
//                        cbWx.setOnCheckedChangeListener(this);

                        RelativeLayout rlQb = holder.getView(R.id.rl_lq);
                        RelativeLayout rlZfb = holder.getView(R.id.rl_zfb);
                        RelativeLayout rlWx = holder.getView(R.id.rl_wx);

                        switch (mPayType) {
                            case "1": //任意,1.钱包 2.现金
                                rlZfb.setVisibility(View.GONE);
                                rlWx.setVisibility(View.GONE);
                                break;
                            case "2":
                                rlQb.setVisibility(View.GONE);
                                ckQb = false;
                                cbZfb.setChecked(false);
                                ckZfb = true;
                                cbZfb.setChecked(true);
                                ckWx = false;
                                cbWx.setChecked(false);
                                break;
                        }
//                        String[] strList = {"支付宝支付","微信支付","钱包余额支付"};

                        //钱包
                        cbQb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                ckQb = b;

                                cbZfb.setChecked(false);
                                cbWx.setChecked(false);
                                if (b) {
                                    cbQb.setChecked(true);
                                }
                                DebugUtil.error("showOrderPayPop: 钱包：" + ckQb);
                            }
                        });
                        //支付宝
                        cbZfb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                ckZfb = b;

                                cbQb.setChecked(false);
                                cbWx.setChecked(false);
                                if (b) {
                                    cbZfb.setChecked(true);
                                }
                                DebugUtil.error("showOrderPayPop: 支付宝：" + ckZfb);
                            }
                        });
                        //微信
                        cbWx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                ckWx = b;

                                cbQb.setChecked(false);
                                cbZfb.setChecked(false);
                                if (b) {
                                    cbWx.setChecked(true);
                                }
                                DebugUtil.error("showOrderPayPop: 微信：" + ckWx);
                            }
                        });
                        //发起支付
                        tvSure.setOnClickListener(new PerfectClickListener() {
                            @Override
                            protected void onNoDoubleClick(View v) {
                                DebugUtil.error("showOrderPayPop: 三种结果：钱包" + ckQb + "--支付宝" + ckZfb + "---微信" + ckWx);
                                if (!ckQb && !ckZfb && !ckWx) {
                                    ToastUtil.showShortToast("请选择支付方式");
                                    return;
                                }
                                //钱包支付
                                if (ckQb && !ckZfb && !ckWx) {
                                    dialog.dismiss();
                                    if (mCapital >= totalMoneyUse) {
                                        //先验证下用户是否存在支付密码
                                        mLoginModel.existPayPassword(getActivity(), new RequestImpl() {
                                            @Override
                                            public void loadSuccess(Object object) {
                                                ExistPayPassword bean = (ExistPayPassword) object;
                                                String mIsExist = bean.getIsExist();
                                                if (mIsExist.equals("1")) {
                                                    //发起支付 弹出输入密码框
                                                    showPayPwdPop(0);
                                                } else {
                                                    DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(), null, "您未设置支付密码，前往设置吗？", new DialogWithYesOrNoUtils.DialogCallBack() {
                                                        @Override
                                                        public void exectEvent(DialogInterface dialog) {
                                                            //设置新的
                                                            SetPayPwdOneActivity.goPage(getActivity(), 0, null, null);
                                                        }

                                                        @Override
                                                        public void exectCancel(DialogInterface dialog) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void loadFailed(int errorCode, String errorMessage) {
                                                ToastUtil.showShortToast(errorMessage);
                                            }
                                        });

                                    } else {
                                        //提示充值
                                        DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(),
                                                "温馨提示",
                                                "钱包余额不足，请前往充值",
                                                "前往充值",
                                                "取消",
                                                new DialogWithYesOrNoUtils.DialogCallBack() {
                                                    @Override
                                                    public void exectEvent(DialogInterface dialog1) {
                                                        WalletActivity.goPage(getActivity(), 1);
                                                        dialog1.dismiss();
                                                    }

                                                    @Override
                                                    public void exectCancel(DialogInterface dialog) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                    }
                                }
                                //支付宝支付
                                if (!ckQb && ckZfb && !ckWx) {
                                    dialog.dismiss();
                                    getPaymentMethodList(2, "");
                                }
                                //微信支付
                                if (!ckQb && !ckZfb && ckWx) {
                                    dialog.dismiss();
                                    getPaymentMethodList(3, "");
                                }
                            }
                        });
                    }
                })
                .setDimAmount(0.7f)
                .setShowBottom(true)
//                .setOutCancel(false)
                .show(getActivity().getSupportFragmentManager());
    }

    /**
     * 密码输入框
     * changeType=0  一期的代码不动
     * changeType=1  积分或者代金券完全够用 直接验证完密码就提交订单就完事
     */
    private void showPayPwdPop(int changeType) {
        //先验证下用户是否存在支付密码
        mLoginModel.existPayPassword(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                ExistPayPassword bean = (ExistPayPassword) object;
                String mIsExist = bean.getIsExist();
                if (mIsExist.equals("1")) {
                    //发起支付 弹出输入密码框
                    final PayPassDialog dialog = new PayPassDialog(getActivity(), R.style.dialog_pay_theme);
                    //弹框自定义配置
                    dialog.setAlertDialog(false)
                            .setWindowSize(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f)
                            .setOutColse(false)
                            .setGravity(R.style.dialogOpenAnimation, Gravity.BOTTOM);
                    //组合控件自定义配置
                    PayPassView payView = dialog.getPayViewPass();
                    payView.setForgetText("忘记支付密码?");
                    payView.setForgetColor(getResources().getColor(R.color.colorAccent));
                    payView.setForgetSize(16);
                    payView.setPayClickListener(new PayPassView.OnPayClickListener() {
                        @Override
                        public void onPassFinish(String passContent) {
                            //验证支付密码
                            mLoginModel.validatePayPassword(getActivity(), passContent, new RequestImpl() {
                                @Override
                                public void loadSuccess(Object object) {
                                    if (changeType == 0) {
                                        getPaymentMethodList(1, passContent);
                                    } else if (changeType == 1) {
                                        goOrder(changeType);
                                    }
                                    dialog.dismiss();
                                }

                                @Override
                                public void loadFailed(int errorCode, String errorMessage) {
                                    ToastUtil.showShortToast(errorMessage);
                                    payView.cleanAllTv();
                                }
                            });
                        }

                        @Override
                        public void onPayClose() {
                            dialog.dismiss();
                            //关闭回调
                        }

                        @Override
                        public void onPayForget() {
                            dialog.dismiss();
                            //忘记密码回调
//                ToastUtil.showShortToast("忘记密码回调");
                            SysUtils.startActivity(getActivity(), InputLoginPwdActivity.class);
                        }
                    });
                } else {
                    DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(), null, "您未设置支付密码，前往设置吗？", new DialogWithYesOrNoUtils.DialogCallBack() {
                        @Override
                        public void exectEvent(DialogInterface dialog) {
                            //设置新的
                            SetPayPwdOneActivity.goPage(getActivity(), 0, null, null);
                        }

                        @Override
                        public void exectCancel(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });

    }

    /**
     * 先查询支付方式列表
     * 1-钱包 2-支付宝 3-微信
     */
    private void getPaymentMethodList(int payType, String password) {
        mPayModel.getPaymentMethodList(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                List<PaymentMethodList> payTypeList = (List<PaymentMethodList>) object;
                if (payTypeList != null && payTypeList.size() > 0) {
                    //发起订单支付
                    switch (payType) {
                        case 1:
                            PaymentMethodList bean = null;
                            for (PaymentMethodList paymentMethodList : payTypeList) {
                                if (paymentMethodList.getCode().equals(Constants.PAY_TYPE_QB)) {
                                    bean = paymentMethodList;
                                    break;
                                }
                            }
                            payOrder(payType, bean, password);
                            break;
                        case 2:
                            PaymentMethodList bean1 = null;
                            for (PaymentMethodList paymentMethodList : payTypeList) {
                                if (paymentMethodList.getCode().equals(Constants.PAY_TYPE_ZFB)) {
                                    bean1 = paymentMethodList;
                                    break;
                                }
                            }
                            payOrder(payType, bean1, password);
                            break;
                        case 3:
                            PaymentMethodList bean2 = null;
                            for (PaymentMethodList paymentMethodList : payTypeList) {
                                if (paymentMethodList.getCode().equals(Constants.PAY_TYPE_WX)) {
                                    bean2 = paymentMethodList;
                                    break;
                                }
                            }
                            payOrder(payType, bean2, password);
                            break;
                    }
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    /**
     * 发起支付
     *
     * @param payType  1-钱包 2-支付宝 3-微信
     * @param bean
     * @param password
     */
    private void payOrder(int payType, PaymentMethodList bean, String password) {
        if (mOrderInfo == null || mOrderInfo.getId() == null) {
            ToastUtil.showShortToast("订单id不能为空");
            return;
        }
        switch (payType) {
            case 1:
                mPayModel.payOrderByQb(getActivity(),
                        mOrderInfo.getId(),
                        password,
                        bean.getId(),
                        bean.getCode(),
                        bean.getName(),
                        bean.getPayTypeId(),
                        bean.getPayTypeName(), new RequestImpl() {
                            @Override
                            public void loadSuccess(Object object) {
                                ToastUtil.showShortToast("支付成功");
//                                if (mClassCode.equals("pfq")) { //批发区
//                                    paySucess(0);
//                                } else if (mClassCode.contains("lsq")) {
//                                    paySucess(1);
//                                }
                                paySucess(0);

                            }

                            @Override
                            public void loadFailed(int errorCode, String errorMessage) {
                                ToastUtil.showShortToast(errorMessage);
                            }
                        });
                break;
            case 2:
                mPayModel.payOrderByZfb(getActivity(),
                        mOrderInfo.getId(),
                        bean.getId(),
                        bean.getCode(),
                        bean.getName(),
                        bean.getPayTypeId(),
                        bean.getPayTypeName(), new RequestImpl() {
                            @Override
                            public void loadSuccess(Object object) {
                                PayOrderByZfb zfb = (PayOrderByZfb) object;
                                if (zfb != null) {
                                    if (mClassCode.equals("pfq")) { //批发区
                                        Constants.ZFB_PAY_CODE = Constants.ZFB_PAY_ORDER_PFQ;
                                    } else if (mClassCode.contains("lsq")) {
                                        Constants.ZFB_PAY_CODE = Constants.ZFB_PAY_ORDER_LSQ;
                                    }
                                    ToastUtil.showShortToast("支付宝下单成功");
                                    MyALipayUtils.ALiPayBuilder builder = new MyALipayUtils.ALiPayBuilder();
                                    builder.build().toALiPay(getActivity(), zfb.getOrderInfo());
                                }
                            }

                            @Override
                            public void loadFailed(int errorCode, String errorMessage) {
                                ToastUtil.showShortToast(errorMessage);
                            }
                        });
                break;
            case 3:
                mPayModel.payOrderByWx(getActivity(),
                        mOrderInfo.getId(),
                        bean.getId(),
                        bean.getCode(),
                        bean.getName(),
                        bean.getPayTypeId(),
                        bean.getPayTypeName(), new RequestImpl() {
                            @Override
                            public void loadSuccess(Object object) {
                                PayOrderByWx wx = (PayOrderByWx) object;
                                if (wx != null) {
                                    ToastUtil.showShortToast("微信下单成功");
                                    if (mClassCode.equals("pfq")) { //批发区
                                        Constants.WX_PAY_CODE = Constants.WX_PAY_ORDER_PFQ;
                                    } else if (mClassCode.contains("lsq")) {
                                        Constants.WX_PAY_CODE = Constants.WX_PAY_ORDER_LSQ;
                                    }
                                    //跳转微信付钱
                                    WXPay.getInstance().Pay(wx);
                                }
                            }

                            @Override
                            public void loadFailed(int errorCode, String errorMessage) {
                                ToastUtil.showShortToast(errorMessage);
                            }
                        });
                break;
        }
    }

    /**
     * 请求收货地址
     */
    private void loadAddress() {
        mAddressModel.getAddress(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                showContentView();
                List<AddressBean> data = (List<AddressBean>) object;
                if (data != null && data.size() > 0 && data.get(0) != null) {
                    hadAddress();
                    AddressBean address = data.get(0);
                    mAddressId = address.getId();
                    setAddress(address);
                } else {
                    noAddress();
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
                noAddress();
            }
        });
    }

    private void setAddress(AddressBean address) {
        if (address != null) {
            mAid = address.getId();
            bindingView.tvName.setText(address.getContactName());
            bindingView.tvPhone.setText(address.getContactMobile());
            @SuppressLint("StringFormatMatches") String addressDesc = String.format(getResources().getString(R.string.address_info),
                    address.getProvinceText(),
                    address.getCityText(),
                    address.getDistrictText(),
                    address.getAddress());
            bindingView.tvAddress.setText(addressDesc);
        } else {
            mAid = null;
            noAddress();
        }

    }

    private void hadAddress() {
        bindingView.rlHasAddress.setVisibility(View.VISIBLE);
        bindingView.rlNoAddress.setVisibility(View.GONE);
    }

    private void noAddress() {
        bindingView.rlHasAddress.setVisibility(View.GONE);
        bindingView.rlNoAddress.setVisibility(View.VISIBLE);
    }

    /**
     * 支付成功后操作
     * 2019/11/29更新  i 去除
     *
     * @param i
     */
    private void paySucess(int i) {
//        switch (i) {
//            case 0: //零售区
//                OrderResultActivity.goPage(getActivity(), mClassCode);
//                ActivityManage.findActivity(ProductInfoActivity.class);
//                finish();
//                break;
//            case 1://批发区
//                OrderResultActivity.goPage(getActivity(), mClassCode);
//                ActivityManage.findActivity(ProductInfoActivity.class);
//                finish();
//                break;
//            case 2://充值
//                break;
//        }
        OrderResultActivity.goPage(getActivity(), mClassCode);
        ActivityManage.findActivity(ProductInfoActivity.class);
        finish();
    }

    /**
     * 注册RxBus
     */
    private void initRxBus() {
        mRxBus = RxBus.getInstanceBus();
        mRxBus.registerRxBus(mRxBus, RxBusMessage.class, rxBusMessage -> {
            //根据事件类型进行处理
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_ORDER_ADDRESS) {
                if (rxBusMessage.getData() != null) {
                    AddressBean getAddress = (AddressBean) rxBusMessage.getData();
                    mAddressId = getAddress.getId();
                    setAddress(getAddress);
                } else if (rxBusMessage.getI() == 1) {
                    //更新收货地址
                    loadAddress();
                } else {
                    setAddress(null);
                    mAddressId = null;
                }
            }

            //微信支付成功
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.PAY_SUCESS_WX) {
                int i = rxBusMessage.getI();
                paySucess(i);
            }

            //支付宝支付成功
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.PAY_SUCESS_ZFB) {
                int i = rxBusMessage.getI();
                paySucess(i);
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

    public static void goPage(Context context, int type, List<SkuAndQuantity> data) {
        Intent intent = new Intent(context, OrderActivity.class);
        intent.putExtra("type", type);
        intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) data);
        context.startActivity(intent);
    }
}
