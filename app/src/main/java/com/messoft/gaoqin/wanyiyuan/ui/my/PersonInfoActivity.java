package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.bean.UploadFile;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.UIUtils;
import com.messoft.gaoqin.wanyiyuan.utils.bitmap.CompressHelper;
import com.messoft.gaoqin.wanyiyuan.utils.bitmap.FileUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityPersonInfoBinding;

import java.io.File;
import java.util.ArrayList;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 个人资料
 */
public class PersonInfoActivity extends BaseActivity<ActivityPersonInfoBinding> {

    private int CHOOSE_PHOTO = 0x01;
    private String mImg;
    private String mDbFileName = null;//图像 文件上传后返回的地址
    private boolean isChangMsg = false;//是否修改了个人信息
    private LoginModel mLoginModel;

    private RxBus mRxBus;

    private String mName;
    private String mWx;
    private String mMobile;
    private String mWxpayCode;
    private String mAlipayCode;

    @Override
    public void onBackPressed() {
        if (isChangMsg) {
            saveInfo();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
    }

    @Override
    protected void initSetting() {
        setTitle("个人资料");
        showContentView();
        mLoginModel = new LoginModel();
        initRxBus();
        loadData();
    }

    @Override
    protected void initListener() {
        //头像
        bindingView.rlImg.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                selectPhoto();
            }
        });
        //姓名
        bindingView.rlName.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SetPersonInfoItemActivity.goPage(getActivity(), 0, mName);
            }
        });
        //微信
        bindingView.rlWx.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SetPersonInfoItemActivity.goPage(getActivity(), 1, mWx);
            }
        });

        //微信收款码
        bindingView.rlWxCode.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                WxAndAliCodeActivity.goPage(getActivity(),0,mWxpayCode);
            }
        });
        bindingView.rlAliCode.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                WxAndAliCodeActivity.goPage(getActivity(),1,mAlipayCode);
            }
        });
    }

    private void loadData() {
        mLoginModel.getLoginMemberInfo(getActivity(), new RequestImpl() {

            @Override
            public void loadSuccess(Object object) {
                LoginMemberInfo data = (LoginMemberInfo) object;
                ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getImgName()), bindingView.ivHeader, 0);
                mName = data.getName();
                bindingView.tvName.setText(mName);
                mMobile = data.getMobile();
                bindingView.tvPhone.setText(mMobile);
                mWx = data.getWechat();
                bindingView.tvWx.setText(mWx);

                mWxpayCode = data.getWxpayCode();
                mAlipayCode = data.getAlipayCode();
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    private void selectPhoto() {
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "pictures");
        startActivityForResult(BGAPhotoPickerActivity.newIntent(PersonInfoActivity.this, takePhotoDir,
                1,
                null, false), CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CHOOSE_PHOTO) {
            ArrayList<String> selectedImages = BGAPhotoPickerActivity.getSelectedImages(data);
            if (selectedImages.size() > 0) {
                mImg = selectedImages.get(0);
                ImgLoadUtil.displayEspImage(mImg, bindingView.ivHeader, 3);
            }
        } else if (requestCode == Constants.REQUEST_CODE_PHOTO_PREVIEW) {
            ArrayList<String> selectedImages = BGAPhotoPickerActivity.getSelectedImages(data);
            if (selectedImages.size() > 0) {
                mImg = selectedImages.get(0);
                ImgLoadUtil.displayEspImage(mImg, bindingView.ivHeader, 3);
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
                .subscribe(new BaseObserver<UploadFile>(getActivity(), true, "图片上传中...") {
                    @Override
                    protected void onSuccess(UploadFile login) {
                        isChangMsg = true;
                        mDbFileName = login.getDbFileName();
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        ToastUtil.showShortToast(msg);
                    }
                });
    }

    private void saveInfo() {
        mLoginModel.updateLoginMemberInfo(getActivity(),
                mName,
                mDbFileName,
                mWx,
                mWxpayCode,
                mAlipayCode, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        //通知外面刷新
                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_MY_INFO, 0));
                        UIUtils.postTaskDelay(() -> {
                            ToastUtil.showShortToast("信息更新成功");
                            finish();
                        }, 500);
                    }

                    @Override
                    public void loadFailed(int errorCode, String errorMessage) {
                        ToastUtil.showShortToast(errorMessage);
                    }
                });
    }

    /**
     * 注册RxBus
     */
    private void initRxBus() {
        mRxBus = RxBus.getInstanceBus();
        mRxBus.registerRxBus(mRxBus, RxBusMessage.class, new Consumer<RxBusMessage>() {
            @Override
            public void accept(@NonNull RxBusMessage rxBusMessage) throws Exception {
                //根据事件类型进行处理
                if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_PERSON_INFO) {
                    if (rxBusMessage.getI() == 0) {
                        isChangMsg = true;
                        //姓名
                        mName = rxBusMessage.getMsg();
                        bindingView.tvName.setText(mName);
                    }
                    if (rxBusMessage.getI() == 1) {
                        isChangMsg = true;
                        //微信
                        mWx = rxBusMessage.getMsg();
                        bindingView.tvWx.setText(mWx);
                    }
                    if (rxBusMessage.getI() == 2) {
                        isChangMsg = true;
                        //微信收款码
                        mWxpayCode = rxBusMessage.getMsg();
                    }
                    if (rxBusMessage.getI() == 3) {
                        isChangMsg = true;
                        //支付宝收款码
                        mAlipayCode = rxBusMessage.getMsg();
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
}
