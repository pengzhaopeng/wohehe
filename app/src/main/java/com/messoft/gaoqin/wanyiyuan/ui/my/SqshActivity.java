package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.bean.UploadFile;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivitySqshBinding;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.HttpUiTips;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.OrderModel;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
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
 * 申请售后
 */
public class SqshActivity extends BaseActivity<ActivitySqshBinding> implements BGASortableNinePhotoLayout.Delegate {

    private String orderId;
    private String orderItemId;
    private String subStatus;

    private int typeFlag = 0; //0-退货  1-换货
    private String mOrderNo;
    private OrderModel mOrderModel;
    private String title;
    private String number;
    private String price;
    private String totalPric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqsh);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initSetting() {
        setTitle("退款/售后");
        showContentView();
        mOrderModel = new OrderModel();
        if (getIntent() != null) {
            orderId = getIntent().getStringExtra("orderId");
            orderItemId = getIntent().getStringExtra("orderItemId");
            subStatus = getIntent().getStringExtra("subStatus");
            mOrderNo = getIntent().getStringExtra("orderNo");
            mOrderNo = getIntent().getStringExtra("orderNo");
            title = getIntent().getStringExtra("title");
            number = getIntent().getStringExtra("number");
            price = getIntent().getStringExtra("price");
            totalPric = getIntent().getStringExtra("totalPrice");
            String productId = getIntent().getStringExtra("productId");

            String img = Constants.MASTER_URL + "wyy/img/pimg/" + productId + "/p1_200_200.jpg";
            ImgLoadUtil.displayEspImage(img, bindingView.iv, 1);
            bindingView.tvTitle.setText(title);
            bindingView.tvNumber.setText("x" + number);
            bindingView.tvPrice.setText(price);
            bindingView.etOrderNo.setText(mOrderNo);

            bindingView.etMoney.setText("¥ " + totalPric);
        }

        //图片选择
        initPickView();
    }

    @Override
    protected void initListener() {
        //退货
        bindingView.tvTh.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                typeFlag = 0;
                bindingView.tvTh.setTextColor(Color.parseColor("#FF9800"));
                bindingView.tvTh.setBackgroundResource(R.drawable.shape_tuihuo_select);
                bindingView.tvHh.setTextColor(Color.parseColor("#333333"));
                bindingView.tvHh.setBackgroundResource(R.drawable.shape_tuihuo_normal);

                bindingView.tvReason.setText("退款原因");
                bindingView.tvMsg.setText("退款说明");
                bindingView.etReason.setHint("请描述退款原因");
                bindingView.etMsg.setHint("请描述退款说明");
                bindingView.rlTkMoney.setVisibility(View.VISIBLE);
            }
        });
        //换货
        bindingView.tvHh.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                typeFlag = 1;
                bindingView.tvHh.setTextColor(Color.parseColor("#FF9800"));
                bindingView.tvHh.setBackgroundResource(R.drawable.shape_tuihuo_select);
                bindingView.tvTh.setTextColor(Color.parseColor("#333333"));
                bindingView.tvTh.setBackgroundResource(R.drawable.shape_tuihuo_normal);

                bindingView.tvReason.setText("换货原因");
                bindingView.tvMsg.setText("换货说明");
                bindingView.etReason.setHint("请描述换货原因");
                bindingView.etMsg.setHint("请描述换货说明");
                bindingView.rlTkMoney.setVisibility(View.GONE);

            }
        });

        bindingView.btnSure.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                doNext();
            }
        });
    }

    private void doNext() {
        String reason = bindingView.etReason.getText().toString().trim();
        String msg = bindingView.etMsg.getText().toString().trim();
        String t1 = (typeFlag == 0) ? "退货" : "换货";
        if (!StringUtils.isNoEmpty(reason)) {
            ToastUtil.showShortToast("请描述" + t1 + "原因");
            return;
        }
        if (!StringUtils.isNoEmpty(msg)) {
            ToastUtil.showShortToast("请描述" + t1 + "说明");
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
                                doPost(jsonArray, reason, msg);
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

    private void doPost(JSONArray jsonArray, String reason, String msg) {
        mOrderModel.addReturnApplyForm(getActivity(),
                typeFlag, orderId, orderItemId, reason, totalPric, msg, jsonArray.toString(),
                new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        ToastUtil.showShortToast("操作成功");
                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_MY_SH_ORDER, 0));
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
        startActivityForResult(BGAPhotoPickerActivity.newIntent(SqshActivity.this, takePhotoDir,
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

    public static void goPage(Context context, String orderId, String orderItemId, String subStatus, String orderNo,
                              String productId,String title, String number, String price, String totalPrice) {
        Intent intent = new Intent(context, SqshActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("orderItemId", orderItemId);
        intent.putExtra("subStatus", subStatus);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("title", title);
        intent.putExtra("number", number);
        intent.putExtra("price", price);
        intent.putExtra("totalPrice", totalPrice);
        intent.putExtra("productId", productId);
        context.startActivity(intent);
    }
}
