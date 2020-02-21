package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.ProductInfo;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.bean.UploadFile;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityMyShenheProductBinding;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.HttpUiTips;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.ProductModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.bitmap.CompressHelper;
import com.messoft.gaoqin.wanyiyuan.utils.bitmap.FileUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

/**
 * 我的审核商品详情
 */
public class MyShenheProductActivity extends BaseActivity<ActivityMyShenheProductBinding> implements BGASortableNinePhotoLayout.Delegate {

    private ProductModel mModel;
    private int mType;//0-查看 1-修改 2-提交审核(新增)
    private String mId;
    private String mName;
    private String mPrice;
    private String mShortDesc;
    private List<ProductInfo.ApplyImgsBean> mApplyImgs;
    private ArrayList<ProductInfo.ApplyImgsBean> mListDbs; //记录已经存在得DBFileNname
    //    private JSONArray mJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shenhe_product);
    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("详情");
        mModel = new ProductModel();
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
            mId = getIntent().getStringExtra("id");
        }
        mListDbs = new ArrayList<>();
        //存放上传后的集合
//        mJsonArray = new JSONArray();
        //图片选择
        initPickView();

        switch (mType) {
            case 0: //查看
                loadData();
                break;
            case 1: //编辑
                bindingView.btnSure.setVisibility(View.VISIBLE);
                loadData();
                break;
            case 2: //新增
                bindingView.btnSure.setVisibility(View.VISIBLE);
                break;
        }

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
        mName = bindingView.etName.getText().toString().trim();
        mPrice = bindingView.etPrice.getText().toString().trim();
        mShortDesc = bindingView.etContent.getText().toString().trim();
        if (!StringUtils.isNoEmpty(mName)) {
            ToastUtil.showShortToast("请输入名称~");
            return;
        }
        if (!StringUtils.isNoEmpty(mPrice)) {
            ToastUtil.showShortToast("请输入价格~");
            return;
        }
        if (!StringUtils.isNoEmpty(mShortDesc)) {
            ToastUtil.showShortToast("请输入商品简介~");
            return;
        }
        final ArrayList<String> data = bindingView.bagPhoto.getData();
        JSONArray jsonArray = new JSONArray();//存放上传后的集合

        if (data.size() < 1) {
            ToastUtil.showShortToast("请上传商品图片");
            return;
        }
        //如果商品图片没有修改就直接用 不用去上传
        //先过滤 http开头的图片 这些图片没有变动 ,不是http开头的图片需要拿去上传
        ArrayList<String> dataNeedUp = new ArrayList<>();//记录修改后需要上传的图片 DbFile
        ArrayList<Integer> dataNoNeedUpIndex = new ArrayList<>(); //记录修改后不需要上传的图片 DbFile index，比如三张修改了最后一张，那么值就是 1 和2
        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).startsWith("http")) {
                dataNeedUp.add(data.get(i));
            } else {
                dataNoNeedUpIndex.add(i);
            }
        }
        if (dataNeedUp.size() > 0) { //有需要上传的本地图片
            data.clear();
            data.addAll(dataNeedUp);
        }
        final int size = data.size();
        //如果 dataNeedUp 为空，mListDbs不为空，意味着编辑进来没有更新图片，这个时候就不用上传文件，直接拿mListDbs去保存就行
        if (dataNeedUp.size() < 1 && mListDbs.size() > 0) {
            for (ProductInfo.ApplyImgsBean listDb : mListDbs) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("url", listDb.getUrl());
                    jsonObject.put("dbFileName", listDb.getDbFileName());
                    jsonObject.put("fileName", listDb.getFileName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
            doPost(jsonArray);

        } else { //编辑或者新增
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

                                    //上传前做合并操作 将jsonArray 和 mListDbs 进行合并，通过dataNoNeedUpIndex进行过滤，拿到最终的 List<DBFileNname> 去上传
                                    if (mListDbs.size() > 0 && dataNoNeedUpIndex.size() > 0) { //大于0 才属于编辑(并且更新了图片)操作，否则算新增操作，新增操作就不进这个判断
                                        //反序一下  (1,2----2,1) 为了添加jsonArray 顺序保持一致
                                        Collections.reverse(dataNoNeedUpIndex);
                                        for (int i1 = 0; i1 < dataNoNeedUpIndex.size(); i1++) {
                                            ProductInfo.ApplyImgsBean applyImgsBean = mListDbs.get(i1);
                                            JSONObject jsonObject1 = new JSONObject();
                                            try {
                                                jsonObject1.put("url", applyImgsBean.getUrl());
                                                jsonObject1.put("dbFileName", applyImgsBean.getDbFileName());
                                                jsonObject1.put("fileName", applyImgsBean.getFileName());
//                                                jsonArray.put(0, jsonObject1);
                                                jsonArray.put(jsonObject1);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    //提交
                                    doPost(jsonArray);
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
    }


    private void doPost(JSONArray imags) {
        if (mType == 1) { //修改
            mModel.updateProductForm(getActivity(),
                    mId,
                    BusinessUtils.getBindAccountId(),
                    mName,
                    mShortDesc,
                    mPrice,
                    "0",
                    imags.toString(),
                    "2",
                    "1",
                    "1",
                    "0", new RequestImpl() {
                        @Override
                        public void loadSuccess(Object object) {
                            ToastUtil.showShortToast("操作成功");
                            RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_MY_PRODUCT_LIST, 0));
                            finish();
                        }

                        @Override
                        public void loadFailed(int errorCode, String errorMessage) {
                            ToastUtil.showShortToast(errorMessage);
                        }
                    });
        } else if (mType == 2) {//新增
            mModel.addProductForm(getActivity(),
                    BusinessUtils.getBindAccountId(),
                    mName,
                    mShortDesc,
                    mPrice,
                    "0",
                    imags.toString(),
                    "2",
                    "1",
                    "1",
                    "0", new RequestImpl() {
                        @Override
                        public void loadSuccess(Object object) {
                            ToastUtil.showShortToast("操作成功");
                            RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_MY_PRODUCT_LIST, 0));
                            finish();
                        }

                        @Override
                        public void loadFailed(int errorCode, String errorMessage) {
                            ToastUtil.showShortToast(errorMessage);
                        }
                    });
        }
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
        startActivityForResult(BGAPhotoPickerActivity.newIntent(MyShenheProductActivity.this, takePhotoDir,
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

    private void loadData() {
        mModel.getProductById(getActivity(), mId, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                ProductInfo data = (ProductInfo) object;
                if (data != null) {
                    setData(data);
                    switch (mType) {
                        case 0://只查看
                            bindingView.etName.setEnabled(false);
                            bindingView.etPrice.setEnabled(false);
                            bindingView.etContent.setEnabled(false);
                            bindingView.bagPhoto.setEditable(false);
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

    public void setData(ProductInfo data) {
        mName = data.getName();
        bindingView.etName.setText(mName);
        mPrice = data.getPrice();
        bindingView.etPrice.setText(mPrice);
        mShortDesc = data.getShortDesc();
        bindingView.etContent.setText(mShortDesc);
        mApplyImgs = data.getApplyImgs();
        ArrayList<String> listImgs = new ArrayList<>();
        mListDbs.clear();
        for (ProductInfo.ApplyImgsBean applyImg : mApplyImgs) {
            listImgs.add(applyImg.getUrl());
            mListDbs.add(applyImg);
        }
        bindingView.bagPhoto.setData(listImgs);

        //查看审核是否成功
        String auditState = data.getAuditState();
        if (auditState.equals("0")) {
            bindingView.rlShz.setVisibility(View.VISIBLE);
        } else if (auditState.equals("1")) {
            bindingView.rlShsb.setVisibility(View.VISIBLE);
        }


//        for (ProductInfo.ApplyImgsBean applyImg : mApplyImgs) {
//            //设置上传的图片数组
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("url", applyImg.getUrl());
//                jsonObject.put("dbFileName", applyImg.getDbFileName());
//                jsonObject.put("fileName", applyImg.getFileName());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            mJsonArray.put(jsonObject);
//        }

    }

    public static void goPage(Context context, int type, String id) {
        Intent intent = new Intent(context, MyShenheProductActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
}
