package com.messoft.gaoqin.wanyiyuan.ui.market;

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
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityWoYaoShenMaiBinding;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.HttpUiTips;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.GoldModel;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.RegexUtil;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.bitmap.CompressHelper;
import com.messoft.gaoqin.wanyiyuan.utils.bitmap.FileUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

/**
 * 我要申述
 */
public class WoYaoShenShuActivity extends BaseActivity<ActivityWoYaoShenMaiBinding> implements BGASortableNinePhotoLayout.Delegate {

    private String mOrderId;
    private String mOrderNo;
    private int mType; //0-商品申述  1-寄售待放行订单申述 2-抢购已付款申述
    private GoldModel mGoldModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wo_yao_shen_mai);
    }

    @Override
    protected void initSetting() {
        setTitle("我要申述");
        showContentView();
        mGoldModel = new GoldModel();
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
            mOrderId = getIntent().getStringExtra("orderId");
            mOrderNo = getIntent().getStringExtra("orderNo");
            bindingView.etName.setText(mOrderNo);
        }

        //图片选择
        initPickView();
    }

    @Override
    protected void initListener() {
        bindingView.btnSure.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                doNext();
            }
        });
    }

    private void doNext() {
        String etContent = bindingView.etSendWorkRemarks.getText().toString().trim();
        String phone = bindingView.etMoney.getText().toString().trim();
        if (!StringUtils.isNoEmpty(etContent)) {
            ToastUtil.showShortToast("请输入申述内容");
            return;
        }
        if (!StringUtils.isNoEmpty(phone)) {
            ToastUtil.showShortToast("请输入您的电话号码");
            return;
        }
        if (!RegexUtil.checkMobile(phone)) {
            ToastUtil.showShortToast("请输入正确的电话号码");
            return;
        }
        final ArrayList<String> data = bindingView.bagPhoto.getData();
        JSONArray jsonArray = new JSONArray();//存放上传后的集合
        final int size = data.size();
        if (size < 1) {
            ToastUtil.showShortToast("请上传商品图片");
            return;
        }
        //循环上传图片
        for (int i = 0; i < size; i++) {
            File file = new File(data.get(i));
            //后缀名
            String ivSuffix = FileUtil.getExtensionName(file.getName());
            //压缩图片
            File newFile = CompressHelper.getDefault(this).compressToFile(file);
            //Base64
            String str64 = FileUtil.fileToBase64(newFile);
            final int finalI = i;
            HttpUiTips.showDialog(getActivity(), false, "上传中...");
            HttpClient.Builder.getJavaCommonServer().uploadFile("headline", ivSuffix, str64)
                    .compose(RxSchedulers.compose())
                    .compose(this.bindToLifecycle())
                    .subscribe(new BaseObserver<UploadFile>(getActivity(), false, "上传图片中...") {
                        @Override
                        protected void onSuccess(UploadFile login) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("url", login.getUrl());
                                jsonObject.put("dbFileName", login.getDbFileName());
                                jsonObject.put("fileName", login.getFileName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            jsonArray.put(jsonObject);
                            if (jsonArray.length() == size) {
                                HttpUiTips.dismissDialog(getActivity());
                                ToastUtil.showShortToast("上传文件完毕");
                                //提交
                                doPost(jsonArray, etContent, phone);
                            }
                        }

                        @Override
                        protected void onFailure(int errorCode, String msg) {
                            ToastUtil.showShortToast("上传第" + (finalI + 1) + "份文件失败（" + msg + ")");
                            if (finalI == data.size() - 1) {
                                HttpUiTips.dismissDialog(getActivity());
                            }
                        }
                    });
        }

    }

    private void doPost(JSONArray jsonArray, String etContent, String phone) {
        String ext = (mType == 1 || mType == 2) ? "wyyqg" : null;
        mGoldModel.addBeanOrderAppealForm(getActivity(), mType, mOrderId, mOrderNo, phone, etContent, jsonArray.toString(), ext, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                ToastUtil.showShortToast("提交申诉成功");
                RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_MY_CG_ORDER, 0));
                finish();
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    private void initPickView() {
        //设置九宫格图片的属性
        //不设置单选
        bindingView.bagPhoto.setMaxItemCount(3);
        //是否编辑
        bindingView.bagPhoto.setEditable(true);
        //加号
        bindingView.bagPhoto.setPlusEnable(true);
        //开启拖拽排序
        bindingView.bagPhoto.setSortable(true);
        // 设置拖拽排序控件的代理
        bindingView.bagPhoto.setDelegate(this);
    }

    private void selectPhoto() {
        //选择照片
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "pictures");
        startActivityForResult(BGAPhotoPickerActivity.newIntent(WoYaoShenShuActivity.this, takePhotoDir,
                bindingView.bagPhoto.getMaxItemCount() - bindingView.bagPhoto.getItemCount(),
                null, false), Constants.REQUEST_CODE_CHOOSE_PHOTO);
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        selectPhoto();
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        bindingView.bagPhoto.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, bindingView.bagPhoto.getMaxItemCount(), models, models, position, false), Constants.REQUEST_CODE_PHOTO_PREVIEW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || null == BGAPhotoPickerActivity.getSelectedImages(data)) {
            return;
        }
        if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_CODE_CHOOSE_PHOTO) {
            bindingView.bagPhoto.addMoreData(BGAPhotoPickerActivity.getSelectedImages(data));
        } else if (requestCode == Constants.REQUEST_CODE_PHOTO_PREVIEW) {
            bindingView.bagPhoto.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
        }
    }

    public static void goPage(Context context, int type, String orderId, String orderNo) {
        Intent intent = new Intent(context, WoYaoShenShuActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("orderId", orderId);
        context.startActivity(intent);
    }
}
