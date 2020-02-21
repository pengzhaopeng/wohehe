package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.ProductInfo;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemMyProductBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.ProductModel;
import com.messoft.gaoqin.wanyiyuan.ui.my.MyShenheProductActivity;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

/**
 * 我的商品列表
 */
public class MyProductListAdapter extends BaseRecyclerViewAdapter<ProductInfo> {

    private BaseActivity mActivity;
    private int mType; //0.待审核 1.审核不通过(审核失败) 2.审核通过(展示中) 3.已下架
    private ProductModel mProductModel;

    public MyProductListAdapter(BaseActivity activity, int type) {
        mActivity = activity;
        mType = type;
        mProductModel = new ProductModel();
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_my_product);
    }


    class ViewHolder extends BaseRecyclerViewHolder<ProductInfo, ItemMyProductBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final ProductInfo data, final int position) {
            if (data != null) {
                /**
                 * 当数据改变时，binding会在下一帧去改变数据，如果我们需要立即改变，就去调用executePendingBindings方法。
                 */
                binding.executePendingBindings();
                // http://mstest-image.mesandbox.com/wyy/img/pimg/22/p1_200_200.jpg   商品列表

                if (mType == 0 || mType == 1) {
                    if (data.getApplyImgs() != null && data.getApplyImgs().size() > 0 && data.getApplyImgs().get(0) != null) {
                        ImgLoadUtil.displayEspImage(data.getApplyImgs().get(0).getUrl(), binding.iv, 1);
                    }

                    binding.tvPriceMarket.setVisibility(View.GONE);
                } else if (mType == 2 || mType == 3) {
                    String img = Constants.MASTER_URL + "wyy/img/pimg/" + data.getId() + "/p1_600_600.jpg";
                    ImgLoadUtil.displayEspImage(img, binding.iv, 1);

                    binding.tvPriceMarket.setText("市场价：¥" + data.getMarketPrice());
                }

                binding.tvName.setText(data.getName());
                binding.tvPrice.setText(data.getPrice());

                switch (mType) {
                    case 0:
                        binding.btnDelete.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        binding.btnDelete.setVisibility(View.VISIBLE);
                        binding.btnEdit.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        binding.rlBottom.setVisibility(View.GONE);
                        binding.tvPriceMarket.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        binding.tvPriceMarket.setVisibility(View.VISIBLE);
                        binding.btnDelete.setVisibility(View.GONE);
                        break;
                }
                //删除商品
                binding.btnDelete.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        DialogWithYesOrNoUtils.getInstance().showDialog(mActivity, "温馨提示", "要删除此商品吗", "删除", "取消", new DialogWithYesOrNoUtils.DialogCallBack() {
                            @Override
                            public void exectEvent(DialogInterface dialog) {
                                mProductModel.delProductForm(mActivity, data.getId(), new RequestImpl() {
                                    @Override
                                    public void loadSuccess(Object object) {
                                        ToastUtil.showShortToast("删除成功");
                                        //外面刷新
                                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_MY_PRODUCT_LIST, 0));
                                    }

                                    @Override
                                    public void loadFailed(int errorCode, String errorMessage) {
                                        ToastUtil.showShortToast(errorMessage);
                                    }
                                });
                            }

                            @Override
                            public void exectCancel(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                    }
                });

                //申请上架
                binding.btnSqsj.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        mProductModel.updateProductForm(mActivity, data.getId(), BusinessUtils.getBindAccountId(),
                                null, null, null, null, null, null, null, null,
                                "0",
                                new RequestImpl() {
                                    @Override
                                    public void loadSuccess(Object object) {
                                        ToastUtil.showShortToast("操作成功");
                                        //外面刷新
                                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_MY_PRODUCT_LIST, 0));
                                    }

                                    @Override
                                    public void loadFailed(int errorCode, String errorMessage) {
                                        ToastUtil.showShortToast(errorMessage);
                                    }
                                });
                    }
                });
                //编辑
                binding.btnEdit.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        MyShenheProductActivity.goPage(mActivity, 1, data.getId());
                    }
                });
            }
        }
    }
}
