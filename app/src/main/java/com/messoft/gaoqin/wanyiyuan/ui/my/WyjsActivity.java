package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.BlankCard;
import com.messoft.gaoqin.wanyiyuan.bean.ExistPayPassword;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.bean.UploadFile;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityWyjsBinding;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.BlankCardModel;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.model.QgModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.bitmap.CompressHelper;
import com.messoft.gaoqin.wanyiyuan.utils.bitmap.FileUtil;
import com.messoft.gaoqin.wanyiyuan.view.PayPassDialog.PayPassDialog;
import com.messoft.gaoqin.wanyiyuan.view.PayPassDialog.PayPassView;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;

/**
 * 我要寄售
 */
public class WyjsActivity extends BaseActivity<ActivityWyjsBinding> {

    private BaseNiceDialog mPayWayPop;
    private int payWay = -1; // 0.微信 1.支付宝 2.银行卡
    private BlankCardModel mBlankCardModel;
    private QgModel mQgModel;
    private BlankCard mDefaultBlankCard = null; //选中的银行卡  不选默认用默认银行卡
    private String mImg = null; //支付宝微信收款码
    private String mDbFileName = null;//支付宝微信收款码 文件上传后返回的地址
    private RxBus mRxBus;

    private LoginModel mLoginModel;

    String cardId = null;
    String cardNo = null;
    String cardName = null;
    String bankName = null;
    String banckBranchName = null;

    //    private String mName;
//    private String mWx;
//    private String mMobile;
    private String mWxpayCode = null;
    private String mAlipayCode = null;
    private String mOrderId;
    private String mGoodsId;
    private int mType; //0-卖家申请寄售 1-卖家补录收款信息
    private String mGoodsTypeId;
    private String mProductName;
    private String mShopName;
    private String mShopMobile;
    private String mWechat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wyjs);
    }

    @Override
    protected void initSetting() {
        setTitle("售卖信息");
        showContentView();
        mLoginModel = new LoginModel();
        mBlankCardModel = new BlankCardModel();
        mQgModel = new QgModel();
        if (getIntent() != null) {
            mOrderId = getIntent().getStringExtra("orderId");
            mGoodsTypeId = getIntent().getStringExtra("goodsTypeId");
            mProductName = getIntent().getStringExtra("productName");
            mShopName = getIntent().getStringExtra("shopName");
            mShopMobile = getIntent().getStringExtra("shopMobile");
            mGoodsId = getIntent().getStringExtra("goodsId");
            mWechat = getIntent().getStringExtra("wechat");
            mType = getIntent().getIntExtra("type", -1);
            bindingView.etTitle.setText(mProductName);

            bindingView.etTitle.setEnabled(false);

            bindingView.etName.setText(mShopName);
            bindingView.etPhone.setText(mShopMobile);
            bindingView.etWx.setText(mWechat);
        }
        initPayWayPop();
        initRxBus();

        //个人信息
        requestPersonInfo();

        //银行卡
        loadYhk();

    }

    @Override
    protected void initListener() {
        //支付方式
        bindingView.rlPayWay.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (mPayWayPop != null) {
                    mPayWayPop.show(getActivity().getSupportFragmentManager());
                }
            }
        });

        //没有二维码
        bindingView.rLMaNo.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                //上传码
                selectPhoto();
            }
        });

        //有二维码
        bindingView.rLMaHas.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                //上传码
                selectPhoto();
            }
        });

        //没有银行卡
        bindingView.rLCardNo.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                //添加银行卡信息
                BlankCardActivity.goPage(getActivity(), 1);
            }
        });

        //有银行卡
        bindingView.rLCardHas.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                //更换银行卡
                BlankCardActivity.goPage(getActivity(), 1);
            }
        });
        //提交
        bindingView.btnPay.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                doPost();
            }
        });
    }

    /**
     * 个人信息
     */
    public void requestPersonInfo() {
        mLoginModel.getLoginMemberInfo(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                LoginMemberInfo data = (LoginMemberInfo) object;
                if (data != null) {
//                    mName = data.getName();
//                    mMobile = data.getMobile();
//                    mWx = data.getWechat();
//                    bindingView.etName.setText(data.getName());
//                    bindingView.etPhone.setText(data.getMobile());
//                    bindingView.etWx.setText(data.getWechat());

                    mWxpayCode = data.getWxpayCode();
                    mAlipayCode = data.getAlipayCode();
                }

            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }


    /**
     * 提交
     */
    private void doPost() {
        String name = bindingView.etName.getText().toString().trim();
        String phone = bindingView.etPhone.getText().toString().trim();
        String wx = bindingView.etWx.getText().toString().trim();

        if (!StringUtils.isNoEmpty(name)) {
            ToastUtil.showShortToast("请输入姓名");
            return;
        }
        if (!StringUtils.isNoEmpty(phone)) {
            ToastUtil.showShortToast("请输入联系电话");
            return;
        }
        if (!StringUtils.isNoEmpty(wx)) {
            ToastUtil.showShortToast("请输入微信号");
            return;
        }
        if (payWay == -1) {
            ToastUtil.showShortToast("请选择支付方式");
            return;
        }

        if (payWay == 2) { //银行卡
            if (mDefaultBlankCard == null) {
                ToastUtil.showShortToast("请选择银行卡");
                return;
            }

            cardId = mDefaultBlankCard.getId();
            cardNo = mDefaultBlankCard.getCardNo();
            cardName = mDefaultBlankCard.getName();
            bankName = mDefaultBlankCard.getTypeName();
            banckBranchName = mDefaultBlankCard.getBranchName();
        } else {//非银行卡
            cardId = null;
            cardNo = null;
            cardName = null;
            bankName = null;
            banckBranchName = null;
        }
        if (payWay == 0 || payWay == 1) {
            if (mDbFileName == null) {
                ToastUtil.showShortToast("请上传收款码");
                return;
            }
        } else if (payWay == 2) {
            if (cardId == null) {
                ToastUtil.showShortToast("请选择银行卡");
                return;
            }
        }

        //先验证下用户是否存在支付密码
        mLoginModel.existPayPassword(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                ExistPayPassword bean = (ExistPayPassword) object;
                String mIsExist = bean.getIsExist();
                if (mIsExist.equals("1")) {
                    //发起支付 弹出输入密码框
                    showPayPwdPop(name, phone, wx);
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

    private void showPayPwdPop(String name, String phone, String wx) {
        //弹出密码框
        final PayPassDialog dialog = new PayPassDialog(this, R.style.dialog_pay_theme);
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
                //6位输入完成回调
                //验证支付密码
                mLoginModel.validatePayPassword(getActivity(), passContent, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        dialog.dismiss();
                        switch (mType){
                            case 0: //申请寄售
                                mQgModel.updateRushGoodsForm(getActivity(),
                                        1,
                                        mGoodsId,
                                        BusinessUtils.getBindAccountId(),
                                        name,
                                        phone,
                                        wx,
                                        mOrderId,
                                        mGoodsTypeId,
                                        payWay + "",
                                        mDbFileName,
                                        cardId, cardNo, cardName, bankName, banckBranchName,
                                        "1",
                                        new RequestImpl() {
                                            @Override
                                            public void loadSuccess(Object object) {
                                                ToastUtil.showShortToast("寄售成功");
                                                //刷新列表
                                                RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_QG_ORDER_LIST, 0));
                                                finish();
                                            }

                                            @Override
                                            public void loadFailed(int errorCode, String errorMessage) {
                                                ToastUtil.showShortToast(errorMessage);
                                            }
                                        });
                                break;
                            case 1: //补录收款信息
                                mQgModel.updateRushOrderForm1(getActivity(),
                                        1,
                                        mGoodsId,
                                        BusinessUtils.getBindAccountId(),
                                        name,
                                        phone,
                                        wx,
                                        mOrderId,
                                        mGoodsTypeId,
                                        payWay + "",
                                        mDbFileName,
                                        cardId, cardNo, cardName, bankName, banckBranchName,
                                        "1",
                                        new RequestImpl() {
                                            @Override
                                            public void loadSuccess(Object object) {
                                                ToastUtil.showShortToast("寄售成功");
                                                //刷新列表
                                                RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_QG_ORDER_LIST, 0));
                                                finish();
                                            }

                                            @Override
                                            public void loadFailed(int errorCode, String errorMessage) {
                                                ToastUtil.showShortToast(errorMessage);
                                            }
                                        });
                                break;
                        }
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
    }

    /**
     * 没有二维码
     */
    private void showNoCode() {
        bindingView.rLMaNo.setVisibility(View.VISIBLE);
        bindingView.rLMaHas.setVisibility(View.GONE);
        bindingView.rLCardNo.setVisibility(View.GONE);
        bindingView.rLCardHas.setVisibility(View.GONE);
    }

    /**
     * 有二维码
     */
    private void showHasCode() {
        bindingView.rLMaNo.setVisibility(View.GONE);
        bindingView.rLMaHas.setVisibility(View.VISIBLE);
        bindingView.rLCardNo.setVisibility(View.GONE);
        bindingView.rLCardHas.setVisibility(View.GONE);
    }

    /**
     * 没有银行卡
     */
    private void showNoCard() {
        if (payWay != 2) {
            return;
        }
        bindingView.rLMaNo.setVisibility(View.GONE);
        bindingView.rLMaHas.setVisibility(View.GONE);
        bindingView.rLCardNo.setVisibility(View.VISIBLE);
        bindingView.rLCardHas.setVisibility(View.GONE);
    }

    /**
     * 有银行卡
     */
    private void showHasCard() {
        if (payWay != 2) {
            return;
        }
        bindingView.rLMaNo.setVisibility(View.GONE);
        bindingView.rLMaHas.setVisibility(View.GONE);
        bindingView.rLCardNo.setVisibility(View.GONE);
        bindingView.rLCardHas.setVisibility(View.VISIBLE);
    }

    /**
     * 支付方式pop
     */
    @SuppressLint("CutPasteId")
    private void initPayWayPop() {
        mPayWayPop = NiceDialog.init()
                .setLayoutId(R.layout.dialog_pay_way)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        ImageView ivBack = holder.getConvertView().findViewById(R.id.iv_back);
                        RelativeLayout rlZfb = holder.getConvertView().findViewById(R.id.rl_zfb);
                        RelativeLayout rlWx = holder.getConvertView().findViewById(R.id.rl_wx);
                        RelativeLayout rlYhk = holder.getConvertView().findViewById(R.id.rl_yhk);

                        ivBack.setOnClickListener(new PerfectClickListener() {
                            @Override
                            protected void onNoDoubleClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        //支付宝
                        rlZfb.setOnClickListener(new PerfectClickListener() {
                            @Override
                            protected void onNoDoubleClick(View v) {
                                dialog.dismiss();
                                bindingView.etWay.setText("支付宝");
                                payWay = 1;
                                if (mImg == null && !StringUtils.isNoEmpty(mAlipayCode)) {
                                    showNoCode();
                                } else {

                                    //mImg 有优先展示,没有就展示支付宝
                                    if (mImg == null && StringUtils.isNoEmpty(mAlipayCode)) {
                                        mDbFileName = mAlipayCode;
                                        ImgLoadUtil.displayEspImage(SysUtils.getImgURL(mAlipayCode), bindingView.codeMa, 1);
                                    }
                                    showHasCode();
                                }
                            }
                        });

                        //微信
                        rlWx.setOnClickListener(new PerfectClickListener() {
                            @Override
                            protected void onNoDoubleClick(View v) {
                                dialog.dismiss();
                                bindingView.etWay.setText("微信");
                                payWay = 0;
                                if (mImg == null && !StringUtils.isNoEmpty(mWxpayCode)) {
                                    showNoCode();
                                } else {
                                    //mImg 有优先展示,没有就展示支付宝
                                    if (mImg == null && StringUtils.isNoEmpty(mWxpayCode)) {
                                        mDbFileName = mWxpayCode;
                                        ImgLoadUtil.displayEspImage(SysUtils.getImgURL(mWxpayCode), bindingView.codeMa, 1);
                                    }
                                    showHasCode();
                                }
                            }
                        });

                        //银行卡
                        rlYhk.setOnClickListener(new PerfectClickListener() {
                            @Override
                            protected void onNoDoubleClick(View v) {
                                dialog.dismiss();
                                bindingView.etWay.setText("银行卡");
                                payWay = 2;
                                if (mDefaultBlankCard == null) {
                                    showNoCard();
                                } else {
                                    showHasCard();
                                }
                            }
                        });
                    }
                })
                .setDimAmount(0.3f)
                .setShowBottom(true)
                .setOutCancel(true);
    }

    /**
     * 银行卡
     */
    private void loadYhk() {
        mBlankCardModel.getMemberBankList(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                List<BlankCard> cardList = (List<BlankCard>) object;
                if (cardList == null || cardList.size() < 1) {
                    //无银行卡
                    showNoCard();
                } else {
                    //有默认银行卡
                    showHasCard();
                    //取默认的银行卡
                    for (BlankCard blankCard : cardList) {
                        if (blankCard.getIsDefault().equals("1")) {
                            mDefaultBlankCard = blankCard;
                        }
                    }
                    if (mDefaultBlankCard == null) {
                        mDefaultBlankCard = cardList.get(0);
                    }
                    //设置银行卡
                    setBlankInfo();
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    public void setBlankInfo() {
        int[] blankBgAndLog = BusinessUtils.getBlankBgAndLog(mDefaultBlankCard.getTypeCode());
        bindingView.ivYhkBg.setImageResource(blankBgAndLog[0]);
        bindingView.ivYhkLogo.setImageResource(blankBgAndLog[1]);

        bindingView.cardName1.setText(mDefaultBlankCard.getName());
        bindingView.cardType1.setText(mDefaultBlankCard.getTypeName() + " (" + mDefaultBlankCard.getBranchName() + ")");
        bindingView.cardCode1.setText(mDefaultBlankCard.getCardNo());
    }

    private void selectPhoto() {
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "pictures");
        startActivityForResult(BGAPhotoPickerActivity.newIntent(WyjsActivity.this, takePhotoDir,
                1,
                null, false), Constants.REQUEST_CODE_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == Constants.REQUEST_CODE_CHOOSE_PHOTO || requestCode == Constants.REQUEST_CODE_PHOTO_PREVIEW) {
            ArrayList<String> selectedImages = BGAPhotoPickerActivity.getSelectedImages(data);
            if (selectedImages.size() > 0) {
                mImg = selectedImages.get(0);
                showHasCode();
                ImgLoadUtil.displayEspImage(mImg, bindingView.codeMa, 3);
            }
        }
        //上传
        uploadImg(mImg);
    }

    /**
     * 上传头像
     *
     * @param img
     */
    private void uploadImg(String img) {
        if (img == null) {
//            ToastUtil.showShortToast("图片路径为空");
            return;
        }
        File file = new File(img);
        //后缀名
        String ivSuffix = FileUtil.getExtensionName(file.getName());
        //压缩图片
        File newFile = CompressHelper.getDefault(this).compressToFile(file);
        //Base64
        String str64 = FileUtil.fileToBase64(newFile);
        HttpClient.Builder.getJavaCommonServer().uploadFile("member", ivSuffix, str64)
                .compose(RxSchedulers.compose())
                .compose(this.bindToLifecycle())
                .subscribe(new BaseObserver<UploadFile>(getActivity(), true, "文件上传中...") {
                    @Override
                    protected void onSuccess(UploadFile login) {
                        mDbFileName = login.getDbFileName();
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        ToastUtil.showShortToast(msg);
                    }
                });
    }

    /**
     * 注册RxBus
     */
    private void initRxBus() {
        mRxBus = RxBus.getInstanceBus();
        mRxBus.registerRxBus(mRxBus, RxBusMessage.class, rxBusMessage -> {
            //根据事件类型进行处理
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_WITHDRAW) {
                if (rxBusMessage.getData() != null) {
                    //更新银行卡
                    mDefaultBlankCard = (BlankCard) rxBusMessage.getData();
                    if (mDefaultBlankCard != null) {
                        showHasCard();
                        setBlankInfo();
                    }
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

    public static void goPage(Context context, String orderId, String goodsId, String goodsTypeId, String productName,
                              String shopName,
                              String shopMobile,
                              String wechat,
                              int type) {
        Intent intent = new Intent(context, WyjsActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("goodsId", goodsId);
        intent.putExtra("goodsTypeId", goodsTypeId);
        intent.putExtra("productName", productName);
        intent.putExtra("shopName", shopName);
        intent.putExtra("shopMobile", shopMobile);
        intent.putExtra("wechat", wechat);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
}
