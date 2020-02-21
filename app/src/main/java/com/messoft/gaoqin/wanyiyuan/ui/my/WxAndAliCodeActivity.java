package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.bean.UploadFile;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityWxAndAliCodeBinding;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.UIUtils;
import com.messoft.gaoqin.wanyiyuan.utils.bitmap.CompressHelper;
import com.messoft.gaoqin.wanyiyuan.utils.bitmap.FileUtil;

import java.io.File;
import java.util.ArrayList;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;

/**
 * 微信或者支付宝收款码界面
 */
public class WxAndAliCodeActivity extends BaseActivity<ActivityWxAndAliCodeBinding> {

    private int CHOOSE_PHOTO = 0x01;
    private LoginModel mLoginModel;

    private int mType; //0-微信 1-支付宝
    private String mCodeUrl;

    private String mImg;

    @Override
    public void onBackPressed() {
        if (mType == 0) {
            RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_PERSON_INFO, 2, mCodeUrl));
        } else {
            RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_PERSON_INFO, 3, mCodeUrl));
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_and_ali_code);
    }

    @Override
    protected void initSetting() {
        showContentView();
        mLoginModel = new LoginModel();
        setTitle("设置收款码");
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
            mCodeUrl = getIntent().getStringExtra("codeUrl");
            if (mType == 0) {
                bindingView.tvTitle1.setText("微信收款码");
                bindingView.tvTitle2.setText("用于微信扫码收款");
            } else {
                bindingView.tvTitle1.setText("支付宝收款码");
                bindingView.tvTitle2.setText("用于支付宝扫码收款");
            }
            if (!StringUtils.isNoEmpty(mCodeUrl)) {
                bindingView.tvTitle3.setVisibility(View.VISIBLE);
            } else {
                bindingView.tvTitle3.setVisibility(View.GONE);
                bindingView.tvTitle4.setVisibility(View.VISIBLE);
                ImgLoadUtil.displayEspImage(SysUtils.getImgURL(mCodeUrl), bindingView.iv);
            }
        }
    }

    @Override
    protected void initListener() {
        bindingView.iv.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                selectPhoto();
            }
        });
    }

    private void selectPhoto() {
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "pictures");
        startActivityForResult(BGAPhotoPickerActivity.newIntent(WxAndAliCodeActivity.this, takePhotoDir,
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
                ImgLoadUtil.displayEspImage(mImg, bindingView.iv, 3);
            }
        } else if (requestCode == Constants.REQUEST_CODE_PHOTO_PREVIEW) {
            ArrayList<String> selectedImages = BGAPhotoPickerActivity.getSelectedImages(data);
            if (selectedImages.size() > 0) {
                mImg = selectedImages.get(0);
                ImgLoadUtil.displayEspImage(mImg, bindingView.iv, 3);
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
                        mCodeUrl = login.getDbFileName();
                        uploadPersonInfo();
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        ToastUtil.showShortToast(msg);
                    }
                });
    }

    String wxpayCode = null;
    String alipayCode = null;

    private void uploadPersonInfo() {
        if (mType == 0) {
            wxpayCode = mCodeUrl;
        } else if (mType == 1) {
            alipayCode = mCodeUrl;
        }
        mLoginModel.updateLoginMemberInfo(getActivity(),
                null,
                null,
                null,
                wxpayCode,
                alipayCode, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        int type = (mType == 0) ? 2 : 3;
                        String msg = (mType == 0) ? wxpayCode : alipayCode;
                        //通知外面刷新
                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_PERSON_INFO, type, msg));
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

    public static void goPage(Context context, int type, String codeUrl) {
        Intent intent = new Intent(context, WxAndAliCodeActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("codeUrl", codeUrl);
        context.startActivity(intent);
    }
}
