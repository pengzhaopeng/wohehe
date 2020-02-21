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
import com.messoft.gaoqin.wanyiyuan.bean.OrderInfo;
import com.messoft.gaoqin.wanyiyuan.bean.ProductInfo;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemOrderBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.OrderModel;
import com.messoft.gaoqin.wanyiyuan.model.ProductModel;
import com.messoft.gaoqin.wanyiyuan.ui.home.CommonOrderPayActivity;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;


/**
 * 包括卖家和买家
 */
public class OrderAdapter extends BaseRecyclerViewAdapter<OrderInfo> {

    private BaseActivity mActivity;
    private OrderModel mModel;
    private ProductModel mProductModel;
    private int mType;//0-买家  1-卖家 2-售后(不区分买家和卖家)

    public OrderAdapter(BaseActivity activity, int type) {
        mType = type;
        mActivity = activity;
        mModel = new OrderModel();
        mProductModel = new ProductModel();
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_order);
    }

    class ViewHolder extends BaseRecyclerViewHolder<OrderInfo, ItemOrderBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final OrderInfo data, final int position) {

            if (data != null) {
                //公共字段
                binding.tvTime.setText(data.getPostTime());
//                //订单号
//                binding.tvOrderNumber.setText("订单号：" + data.getOrderCode());
                //TODO 单个订单单个商品
                String transferState = "0"; //0:不转让(可盛情转让) 1:转让（可取消） 2.完成
                if (data.getOrderItemData() != null && data.getOrderItemData().size() > 0 &&
                        data.getOrderItemData().get(0) != null) {
                    OrderInfo.OrderItemDataBean productBean = data.getOrderItemData().get(0);
                    String img = Constants.MASTER_URL + "wyy/img/pimg/" + productBean.getProductId() + "/p1_200_200.jpg";
                    ImgLoadUtil.displayEspImage(img, binding.iv, 1);
                    binding.tvName.setText(productBean.getProductName());
                    binding.tvNumber.setText("x" + productBean.getSkuQty());
//                    binding.tvPrice.setText(productBean.getSkuPrice());
                    //转让状态
                    transferState = productBean.getTransferState();
                }
                BusinessUtils.setPriceAndPoints(binding.tvPrice,13,  data.getUsePoint(), data.getProductAmount());
                //状态 理状态 0:等待买家付款 1:等待卖家发货(买家已付款)
                // 2:等待买家确认收货(卖家已发货), 3:买家已签收(货到付款) 4.交易成功 5.交易关闭 100:已取消
                String status = data.getHandleStatus();
                String isComment = data.getIsComment();
                //其他字段 区分买家和卖家
//                if (data.getMemberId().equals(BusinessUtils.getBindAccountId())) {  //买家
                if (mType == 0) {  //买家
                    //订单号
                    binding.tvOrderNumber.setText(data.getOrderItemData().get(0).getClassName());
                    switch (status) {
                        case "0":
                            binding.tvStatus.setText("待付款");
                            buyDfk(binding);
                            break;
                        case "1":
                            binding.tvStatus.setText("待发货");
                            //0 提货权转让 1取消转让  2转让申请已提交
                            if (transferState.equals("0")) {
                                buyDfh0(binding);
                            } else if (transferState.equals("1")) {
                                buyDfh1(binding);
                            } else if (transferState.equals("2")) {
                                buyDfh2(binding);
                            } else if (transferState.equals("-1")) {
                                buyDfh3(binding);
                            }
                            break;
                        case "2":
                            binding.tvStatus.setText("待收货");
                            buyDsh(binding);
                            break;
                        case "4": //判断是否为待评价
                            if (isComment.equals("1")) {
                                binding.tvStatus.setText("已评价");
                                buyDpj1(binding);
                            } else {
                                binding.tvStatus.setText("待评价");
                                buyDpj2(binding);
                            }
                            break;
                        case "5":
                            binding.tvStatus.setText("已完成");
                            buyYwc(binding);
                            break;
                        case "100":
                            binding.tvStatus.setText("已取消");
                            buyYqx(binding);
                            break;
                    }
//                } else if (data.getCompanyId().equals(BusinessUtils.getBindAccountId())) { //卖家
                } else if (mType == 1) { //卖家
                    //订单号
                    binding.tvOrderNumber.setText("订单号：" + data.getOrderCode());
                    //状态 理状态 0:等待买家付款 1:等待卖家发货(买家已付款)
                    // 2:等待买家确认收货(卖家已发货), 3:买家已签收(货到付款) 4.交易成功 5.交易关闭 100:已取消
                    switch (status) {
                        case "0":
                            binding.tvStatus.setText("等待买家付款");
                            sellDfk(binding);
                            break;
                        case "1":
                            binding.tvStatus.setText("待发货");
                            //卖家：0、不转让  1、转让中  2、申请转让
                            if (transferState.equals("0")) {
                                sellDfh0(binding);
                            } else if (transferState.equals("1")) {
                                sellDfh1(binding);
                            } else if (transferState.equals("2")) {
                                sellDfh2(binding);
                            } else if (transferState.equals("-1")) {
                                sellDfh3(binding);
                            }
                            break;
                        case "2":
                            sellDfk(binding);
                            binding.tvStatus.setText("待买家收货");
                            break;
                        case "4": //判断是否为待评价
                            sellDfk(binding);
                            if (isComment.equals("1")) {
                                binding.tvStatus.setText("买家已评价");
                            } else {
                                binding.tvStatus.setText("待买家评价");
                            }
                            break;
                        case "5":
                            sellDfk(binding);
                            binding.tvStatus.setText("已完成");
                            break;
                        case "100":
                            binding.tvStatus.setText("已取消");
                            buyYqx(binding);
                            break;
                    }
                }
                //点击事件
                //待付款
                binding.btnDfk.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        goPay(data);
                    }
                });
                //取消订单
                binding.btnQxdd.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        DialogWithYesOrNoUtils.getInstance().showDialog(mActivity,
                                "", "确认取消此订单？",
                                "确认取消", "我再想想", new DialogWithYesOrNoUtils.DialogCallBack() {
                                    @Override
                                    public void exectEvent(DialogInterface dialog) {
                                        updateOrderStatus(data, 100, binding, position);
                                    }

                                    @Override
                                    public void exectCancel(DialogInterface dialog) {
                                        dialog.dismiss();
                                    }
                                });

                    }
                });
                //确认收货
                binding.btnDsh.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        updateOrderStatus(data, 4, binding, position);
                    }
                });
                //确认发货
//                binding.btnDfh.setOnClickListener(new PerfectClickListener() {
//                    @Override
//                    protected void onNoDoubleClick(View v) {
////                        OrderInfoActivity.goPage(mActivity, data.getId(),);
//                    }
//                });
                //转让和取消转让
                binding.btnSszr.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        //卖家别动
                        if (data.getCompanyId().equals(BusinessUtils.getBindAccountId())) {
                            return;
                        }
                        updateOrderItemForm(data, binding);
                    }
                });
                binding.btnQxzr.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        //卖家别动
                        if (data.getCompanyId().equals(BusinessUtils.getBindAccountId())) {
                            return;
                        }
                        updateOrderItemForm(data, binding);
                    }
                });
            }
        }
    }


    /**
     * 去付款
     */
    private void goPay(OrderInfo bean) {
        //查询该商品支持的支付方式
        mProductModel.getProductById(mActivity, bean.getOrderItemData().get(0).getProductId(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                ProductInfo data = (ProductInfo) object;
                if (data != null) {
                    String payType = data.getPayType();
                    CommonOrderPayActivity.goPage(mActivity,
                            bean.getId(),
                            bean.getOrderItemData().get(0).getClassCode(),
                            bean.getOrderAmount(),
                            payType
                    );
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });


    }

    /**
     * 订单取消 100
     * 订单确认收货 4
     */
    private void updateOrderStatus(OrderInfo data, int type, ItemOrderBinding binding, int position) {
        mModel.updateOrderStatus(mActivity, data.getId(), type, null, null, null, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                switch (type) {
                    case 100:
                        ToastUtil.showShortToast("订单取消成功");
                        if (position >= 0) {
                            remove(position);
                            notifyDataSetChanged();
                        }
                        buyYqx(binding);
                        break;
                    case 4:
                        ToastUtil.showShortToast("确认收货成功");
                        //隐藏按钮
                        binding.btnDsh.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    /**
     * 转让订单
     */
    private void updateOrderItemForm(OrderInfo data, ItemOrderBinding binding) {
//        String classCode = data.getOrderItemData().get(0).getClassCode();
//        if (classCode.equals("pfq") || classCode.equals("bpq")) {
//            ToastUtil.showShortToast("批发区和爆品区商品不能转让");
//            return;
//        }
//        String classCode = data.getOrderItemData().get(0).getClassCode();
        String transferState = data.getOrderItemData().get(0).getTransferState();
//        if (classCode.equals("pfq") || classCode.equals("bpq")) {
//            ToastUtil.showShortToast("批发区和爆品区商品不能转让");
//            return;
//        }
        if (transferState.equals("-1")) {
            ToastUtil.showShortToast("该商品不能转让");
            return;
        }
        //下架了不让点
        mProductModel.getProductById(mActivity, data.getOrderItemData().get(0).getProductId(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                ProductInfo productInfo = (ProductInfo) object;
                String isSale = productInfo.getIsSale();
                if (isSale.equals("0")) {
                    ToastUtil.showShortToast("下架商品不能转让");

                } else {
                    String transferState = data.getOrderItemData().get(0).getTransferState();
                    int type = 0;
                    if (transferState.equals("0")) {
                        type = 1;
                    } else if (transferState.equals("1")) {
                        type = 0;
                    }
                    int finalType = type;
                    mModel.updateOrderItemForm(mActivity, data.getOrderItemData().get(0).getId(), type, new RequestImpl() {
                        @Override
                        public void loadSuccess(Object object) {
                            switch (transferState) {
                                case "0": //点完转让 变成取消转让（转让中...）
                                    ToastUtil.showShortToast("提交转让申请...");
                                    binding.btnSszr.setVisibility(View.GONE);
                                    binding.btnQxzr.setVisibility(View.VISIBLE);
                                    break;
                                case "1": //点完取消转让 变成可以转让
                                    ToastUtil.showShortToast("已取消转让");
                                    binding.btnSszr.setVisibility(View.VISIBLE);
                                    binding.btnQxzr.setVisibility(View.GONE);
                                    break;
                            }
                            //TODO 转让完修改 transferState 值
                            data.getOrderItemData().get(0).setTransferState(String.valueOf(finalType));
                        }

                        @Override
                        public void loadFailed(int errorCode, String errorMessage) {
                            ToastUtil.showShortToast(errorMessage);
                        }
                    });
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showLongToast(errorMessage);
            }
        });

    }

    //买家待付款
    private void buyDfk(ItemOrderBinding binding) {
        binding.btnDfk.setVisibility(View.VISIBLE);
        binding.btnQxdd.setVisibility(View.VISIBLE);

        binding.btnSszr.setVisibility(View.GONE);
        binding.btnQxzr.setVisibility(View.GONE);
        binding.btnYzr.setVisibility(View.GONE);

        binding.btnDsh.setVisibility(View.GONE);

        binding.btnYpj.setVisibility(View.GONE);
        binding.btnDpj.setVisibility(View.GONE);

        binding.btnYqx.setVisibility(View.GONE);
    }

    //买家待发货 0 提货权转让 1取消转让  2转让申请已提交
    private void buyDfh0(ItemOrderBinding binding) {
        binding.btnDfk.setVisibility(View.GONE);
        binding.btnQxdd.setVisibility(View.GONE);

        binding.btnSszr.setVisibility(View.VISIBLE);
        binding.btnQxzr.setVisibility(View.GONE);
        binding.btnYzr.setVisibility(View.GONE);

        binding.btnDsh.setVisibility(View.GONE);

        binding.btnYpj.setVisibility(View.GONE);
        binding.btnDpj.setVisibility(View.GONE);

        binding.btnYqx.setVisibility(View.GONE);
    }

    //买家待发货 0 提货权转让 1取消转让  2转让申请已提交
    private void buyDfh1(ItemOrderBinding binding) {
        binding.btnDfk.setVisibility(View.GONE);
        binding.btnQxdd.setVisibility(View.GONE);

        binding.btnSszr.setVisibility(View.GONE);
        binding.btnQxzr.setVisibility(View.VISIBLE);
        binding.btnYzr.setVisibility(View.GONE);

        binding.btnDsh.setVisibility(View.GONE);

        binding.btnYpj.setVisibility(View.GONE);
        binding.btnDpj.setVisibility(View.GONE);

        binding.btnYqx.setVisibility(View.GONE);
    }

    //买家待发货 0 提货权转让 1取消转让  2转让申请已提交
    private void buyDfh2(ItemOrderBinding binding) {
        binding.btnDfk.setVisibility(View.GONE);
        binding.btnQxdd.setVisibility(View.GONE);

        binding.btnSszr.setVisibility(View.GONE);
        binding.btnQxzr.setVisibility(View.GONE);
        binding.btnYzr.setVisibility(View.VISIBLE);

        binding.btnDsh.setVisibility(View.GONE);

        binding.btnYpj.setVisibility(View.GONE);
        binding.btnDpj.setVisibility(View.GONE);

        binding.btnYqx.setVisibility(View.GONE);
    }

    //买家待发货 0 提货权转让 1取消转让  2转让申请已提交
    private void buyDfh3(ItemOrderBinding binding) {
        binding.btnDfk.setVisibility(View.GONE);
        binding.btnQxdd.setVisibility(View.GONE);

        binding.btnSszr.setVisibility(View.GONE);
        binding.btnQxzr.setVisibility(View.GONE);
        binding.btnYzr.setVisibility(View.GONE);

        binding.btnDsh.setVisibility(View.GONE);

        binding.btnYpj.setVisibility(View.GONE);
        binding.btnDpj.setVisibility(View.GONE);

        binding.btnYqx.setVisibility(View.GONE);
    }

    //买家待收货
    private void buyDsh(ItemOrderBinding binding) {
        binding.btnDfk.setVisibility(View.GONE);
        binding.btnQxdd.setVisibility(View.GONE);

        binding.btnSszr.setVisibility(View.GONE);
        binding.btnQxzr.setVisibility(View.GONE);
        binding.btnYzr.setVisibility(View.GONE);

        binding.btnDsh.setVisibility(View.VISIBLE);

        binding.btnYpj.setVisibility(View.GONE);
        binding.btnDpj.setVisibility(View.GONE);

        binding.btnYqx.setVisibility(View.GONE);
    }

    //买家待评价
    private void buyDpj1(ItemOrderBinding binding) {
        binding.btnDfk.setVisibility(View.GONE);
        binding.btnQxdd.setVisibility(View.GONE);

        binding.btnSszr.setVisibility(View.GONE);
        binding.btnQxzr.setVisibility(View.GONE);
        binding.btnYzr.setVisibility(View.GONE);

        binding.btnDsh.setVisibility(View.GONE);

        binding.btnYpj.setVisibility(View.VISIBLE);
        binding.btnDpj.setVisibility(View.GONE);

        binding.btnYqx.setVisibility(View.GONE);
    }

    //买家待评价
    private void buyDpj2(ItemOrderBinding binding) {
        binding.btnDfk.setVisibility(View.GONE);
        binding.btnQxdd.setVisibility(View.GONE);

        binding.btnSszr.setVisibility(View.GONE);
        binding.btnQxzr.setVisibility(View.GONE);
        binding.btnYzr.setVisibility(View.GONE);

        binding.btnDsh.setVisibility(View.GONE);

        binding.btnYpj.setVisibility(View.GONE);
        binding.btnDpj.setVisibility(View.VISIBLE);

        binding.btnYqx.setVisibility(View.GONE);
    }

    //买家已完成
    private void buyYwc(ItemOrderBinding binding) {
        binding.btnDfk.setVisibility(View.GONE);
        binding.btnQxdd.setVisibility(View.GONE);

        binding.btnSszr.setVisibility(View.GONE);
        binding.btnQxzr.setVisibility(View.GONE);
        binding.btnYzr.setVisibility(View.GONE);

        binding.btnDsh.setVisibility(View.GONE);

        binding.btnYpj.setVisibility(View.GONE);
        binding.btnDpj.setVisibility(View.GONE);

        binding.btnYqx.setVisibility(View.GONE);

        //转让状态
        binding.btnBrz.setVisibility(View.GONE);
        binding.btnZrz.setVisibility(View.GONE);
        binding.btnSqzr.setVisibility(View.GONE);
    }

    //买家已取消
    private void buyYqx(ItemOrderBinding binding) {
        binding.btnDfk.setVisibility(View.GONE);
        binding.btnQxdd.setVisibility(View.GONE);

        binding.btnSszr.setVisibility(View.GONE);
        binding.btnQxzr.setVisibility(View.GONE);
        binding.btnYzr.setVisibility(View.GONE);

        binding.btnDsh.setVisibility(View.GONE);
        binding.btnDfh.setVisibility(View.GONE);

        binding.btnYpj.setVisibility(View.GONE);
        binding.btnDpj.setVisibility(View.GONE);

        binding.btnYzr.setVisibility(View.GONE);

        binding.btnYqx.setVisibility(View.VISIBLE);

        //转让状态
        binding.btnBrz.setVisibility(View.GONE);
        binding.btnZrz.setVisibility(View.GONE);
        binding.btnSqzr.setVisibility(View.GONE);
    }

    //等待买家付款
    private void sellDfk(ItemOrderBinding binding) {
        binding.btnDfh.setVisibility(View.GONE);
        binding.btnSszr.setVisibility(View.GONE);
        binding.btnQxzr.setVisibility(View.GONE);
        binding.btnYzr.setVisibility(View.GONE);

        binding.btnYqx.setVisibility(View.GONE);

        //转让状态
        binding.btnBrz.setVisibility(View.GONE);
        binding.btnZrz.setVisibility(View.GONE);
        binding.btnSqzr.setVisibility(View.GONE);
    }

    //卖家待发货 卖家：0、不转让  1、转让中  2、申请转让
    private void sellDfh0(ItemOrderBinding binding) {
        binding.btnDfh.setVisibility(View.VISIBLE);

        binding.btnSszr.setVisibility(View.GONE);
        binding.btnQxzr.setVisibility(View.GONE);
        binding.btnYzr.setVisibility(View.GONE);

        binding.btnBrz.setVisibility(View.VISIBLE);
        binding.btnZrz.setVisibility(View.GONE);
        binding.btnSqzr.setVisibility(View.GONE);

        binding.btnYqx.setVisibility(View.GONE);

    }

    ///卖家待发货 卖家：0、不转让  1、转让中  2、申请转让
    private void sellDfh1(ItemOrderBinding binding) {
        binding.btnDfh.setVisibility(View.VISIBLE);

        binding.btnBrz.setVisibility(View.GONE);
        binding.btnZrz.setVisibility(View.VISIBLE);
        binding.btnSqzr.setVisibility(View.GONE);

        binding.btnSszr.setVisibility(View.GONE);
        binding.btnQxzr.setVisibility(View.GONE);
        binding.btnYzr.setVisibility(View.GONE);

        binding.btnYqx.setVisibility(View.GONE);

    }

    ///卖家待发货 卖家：0、不转让  1、转让中  2、申请转让
    private void sellDfh2(ItemOrderBinding binding) {
        binding.btnDfh.setVisibility(View.VISIBLE);

        binding.btnBrz.setVisibility(View.GONE);
        binding.btnZrz.setVisibility(View.GONE);
        binding.btnSqzr.setVisibility(View.VISIBLE);

        binding.btnSszr.setVisibility(View.GONE);
        binding.btnQxzr.setVisibility(View.GONE);
        binding.btnYzr.setVisibility(View.GONE);

        binding.btnYqx.setVisibility(View.GONE);
    }

    ///卖家待发货 卖家：0、不转让  1、转让中  2、申请转让 -1不显示按钮
    private void sellDfh3(ItemOrderBinding binding) {
        binding.btnDfh.setVisibility(View.VISIBLE);

        binding.btnBrz.setVisibility(View.GONE);
        binding.btnZrz.setVisibility(View.GONE);
        binding.btnSqzr.setVisibility(View.GONE);

        binding.btnSszr.setVisibility(View.GONE);
        binding.btnQxzr.setVisibility(View.GONE);
        binding.btnYzr.setVisibility(View.GONE);

        binding.btnYqx.setVisibility(View.GONE);
    }

}
