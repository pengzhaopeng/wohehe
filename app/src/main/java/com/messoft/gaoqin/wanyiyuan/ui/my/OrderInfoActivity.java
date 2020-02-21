package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.jaeger.library.StatusBarUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.ProductCommentAdapter;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.BaseBean;
import com.messoft.gaoqin.wanyiyuan.bean.EvaluationList;
import com.messoft.gaoqin.wanyiyuan.bean.EvaluationTagList;
import com.messoft.gaoqin.wanyiyuan.bean.OrderInfo;
import com.messoft.gaoqin.wanyiyuan.bean.ProductInfo;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityOrderInfoBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.OrderModel;
import com.messoft.gaoqin.wanyiyuan.model.ProductModel;
import com.messoft.gaoqin.wanyiyuan.ui.home.CommonOrderPayActivity;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.StarBar;
import com.messoft.gaoqin.wanyiyuan.view.flowlayout.FlowLayout;
import com.messoft.gaoqin.wanyiyuan.view.flowlayout.TagAdapter;
import com.messoft.gaoqin.wanyiyuan.view.flowlayout.TagFlowLayout;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.RecycleViewDivider;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 订单详情
 */
public class OrderInfoActivity extends BaseActivity<ActivityOrderInfoBinding> {

    private String mId;
    private int mType; //0-买家  1-卖家 2-售后(不区分买家和卖家)
    //0:等待买家付款 1:等待卖家发货(买家已付款) 2:等待买家确认收货(卖家已发货), 3:买家已签收(货到付款) 4.交易成功 5.交易关闭 100:已取消
//    private int mHandleStatus;

    private OrderModel mOrderModel;
    private ProductModel mProductModel;

    private ProductCommentAdapter mCommentAdapter;
    private int markScore = 5; //默认评分

    @Override
    public void onBackPressed() {
        //返回更新页面信息
        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH__ORDER_LIST, 0));
        super.onBackPressed();
    }

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
    }

    @Override
    protected void initSetting() {
        if (getActivity() != null && getActivity().getWindow() != null) {
            StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.theme_color), 0);
        }
        showContentView();
        mOrderModel = new OrderModel();
        mProductModel = new ProductModel();
        if (getIntent() != null) {
            mId = getIntent().getStringExtra("id");
            mType = getIntent().getIntExtra("type", -1);
//            mHandleStatus = getIntent().getIntExtra("handleStatus", -1);
        }

        //初始化底部评价
        initComment();

        loadData();
    }

    @Override
    protected void initListener() {
        bindingView.ivBack.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 初始化底部评价
     */
    private void initComment() {
        mCommentAdapter = new ProductCommentAdapter(getActivity());
        initOnRefresh(getActivity(), bindingView.rcComment);
//        bindingView.rcAddress.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        bindingView.rcComment.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL,
                DensityUtils.dp2px(getActivity(), (float) 0.5),
                getResources().getColor(R.color.colorPageBg)));
        bindingView.rcComment.setAdapter(mCommentAdapter);
    }

    private void loadData() {
        mOrderModel.getOrderById(getActivity(), mId, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                OrderInfo bean = (OrderInfo) object;
                if (bean != null) {
                    setData(bean);
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setData(OrderInfo bean) {
        //地址
        bindingView.tvName.setText(bean.getReceiveName());
        bindingView.tvPhone.setText(bean.getReceiveMobile());
        @SuppressLint("StringFormatMatches") String addressDesc = String.format(getResources().getString(R.string.address_info),
                bean.getReceiveProvinceText(),
                bean.getReceiveCityText(),
                bean.getReceiveDistrictText(),
                bean.getReceiveAddress());
        bindingView.tvAddress.setText(addressDesc);

        //是否提货 批发区再显示
        if (bean.getOrderItemData().get(0).getClassCode().equals("pfq")) {
            bindingView.rlTh.setVisibility(View.VISIBLE);
            String th = bean.getIsPickUp().endsWith("0") ? "批发区合作" : "自购提货";
            bindingView.tvTh1.setText(th);
        }

        String productId = null;
        String transferState = "0"; //0:不转让(可盛情转让) 1:转让（可取消） 2.完成
        //商品
        if (bean.getOrderItemData() != null && bean.getOrderItemData().size() > 0 && bean.getOrderItemData().get(0) != null) {
            OrderInfo.OrderItemDataBean productBean = bean.getOrderItemData().get(0);
            String img = Constants.MASTER_URL + "wyy/img/pimg/" + productBean.getProductId() + "/p1_200_200.jpg";
            ImgLoadUtil.displayEspImage(img, bindingView.iv, 1);
            bindingView.tvTitle.setText(productBean.getProductName());
            bindingView.tvNumber.setText("x" + productBean.getSkuQty());
//            bindingView.tvPrice.setText("¥ " + productBean.getSkuPrice());
            BusinessUtils.setPriceAndPoints1(bindingView.tvPrice,  productBean.getSkuPoints(), productBean.getSkuPrice());

            transferState = productBean.getTransferState();
            productId = productBean.getProductId();

            //TODO 单商品 总数量
            bindingView.tvTotalNumber.setText("共" + productBean.getSkuQty() + "件");
        }
        //配送方式
        bindingView.tvPeisongType.setText(BusinessUtils.deliveryType(bean.getDeliveryType()));
        //备注留言
        bindingView.etBeizhu.setText(bean.getRemark());
        //商品总价
//        bindingView.tvTotalPrice.setText("¥" + bean.getProductAmount());
        BusinessUtils.setPriceAndPoints1(bindingView.tvTotalPrice,bean.getUsePoint(), bean.getProductAmount());
        //抵扣券
        bindingView.tvDjq.setText("¥" + bean.getUseVoucher());
        //实付款
//        bindingView.tvTotalMoney.setText("¥ " + bean.getOrderAmount());
        BusinessUtils.setPriceAndPoints(bindingView.tvTotalMoney,13,bean.getUsePoint(), bean.getOrderAmount());
        //订单编号 下单时间 付款时间
        bindingView.tvCode.setText(bean.getOrderCode());
        bindingView.tvTime.setText(bean.getCreateTime());
        bindingView.tvPayTime.setText(bean.getPayTime());
        //物流公司
        bindingView.etWuliuCompany.setText(bean.getLogisticsName());
        bindingView.etWuliuCode.setText(bean.getWaybill());

        //变数
        doClick(bean, transferState, productId);
    }

    /**
     * 底部按钮操作
     *
     * @param bean
     * @param productId
     * @param transferState
     */
    private void doClick(OrderInfo bean, String transferState, String productId) {
        //状态 理状态 0:等待买家付款 1:等待卖家发货(买家已付款)
        // 2:等待买家确认收货(卖家已发货), 3:买家已签收(货到付款) 4.交易成功 5.交易关闭 100:已取消
        String status = bean.getHandleStatus();
        String isComment = bean.getIsComment();
        //其他字段 区分买家和卖家
        if (mType == 0) {  //买家
            switch (status) {
                case "0": //待付款
                    bindingView.rlWuliu.setVisibility(View.GONE);
                    bindingView.rlPayTime.setVisibility(View.GONE);
                    bindingView.tvStatus.setText("待付款");
                    bindingView.rlBottom.setVisibility(View.VISIBLE);
                    bindingView.btnDfk.setVisibility(View.VISIBLE);
                    bindingView.btnQxdd.setVisibility(View.VISIBLE);
                    break;
                case "1"://待发货
                    bindingView.tvStatus.setText("待发货");
                    bindingView.rlBottom.setVisibility(View.VISIBLE);
                    bindingView.rlWuliu.setVisibility(View.GONE);
                    if (transferState.equals("0")) {
                        bindingView.btnSszr.setVisibility(View.VISIBLE);
                    } else if (transferState.equals("1")) {
                        bindingView.btnQxzr.setVisibility(View.VISIBLE);
                    } else if (transferState.equals("2")) {
                        bindingView.btnYzr.setVisibility(View.VISIBLE);
                    }
                    break;
                case "2"://待收货
                    bindingView.tvStatus.setText("待收货");
                    bindingView.rlBottom.setVisibility(View.VISIBLE);
                    bindingView.btnDsh.setVisibility(View.VISIBLE);
                    break;
                case "4": //判断是否为待评价
                    bindingView.rlBottom.setVisibility(View.VISIBLE);
                    if (isComment.equals("1")) { //已评价
                        bindingView.tvStatus.setText("已评价");
                        bindingView.btnYpj.setVisibility(View.VISIBLE);
                    } else { //待评价
                        bindingView.tvStatus.setText("待评价");
                        bindingView.btnDpj.setVisibility(View.VISIBLE);
                    }
                    break;
                case "5"://已完成
                    bindingView.tvStatus.setText("已完成");
                    bindingView.rlBottom.setVisibility(View.GONE);
                    //展示评价
                    bindingView.rlComment.setVisibility(View.VISIBLE);
                    showComment(bean.getId(), productId);
                    break;
                case "100"://已取消
                    bindingView.tvStatus.setText("订单已取消");
                    bindingView.rlPayTime.setVisibility(View.GONE);
                    bindingView.rlWuliu.setVisibility(View.GONE);
                    break;
            }
        } else if (mType == 1) { //卖家
            //状态 理状态 0:等待买家付款 1:等待卖家发货(买家已付款)
            // 2:等待买家确认收货(卖家已发货), 3:买家已签收(货到付款) 4.交易成功 5.交易关闭 100:已取消
            switch (status) {
                case "0": //等待买家付款
                    bindingView.tvStatus.setText("等待买家付款");
                    bindingView.rlWuliu.setVisibility(View.GONE);
                    bindingView.rlPayTime.setVisibility(View.GONE);
                    break;
                case "1": //待发货 展示确认发货和买家转让状态
                    bindingView.etWuliuCompany.setEnabled(true);
                    bindingView.etWuliuCode.setEnabled(true);
                    bindingView.tvStatus.setText("待发货");
                    bindingView.rlBottom.setVisibility(View.VISIBLE);
                    bindingView.btnDfh.setVisibility(View.VISIBLE);
                    if (transferState.equals("0")) { //未转让 不显示
                        bindingView.btnZrz.setVisibility(View.VISIBLE);
                        bindingView.btnZrz.setText("未转让");
                    } else if (transferState.equals("1")) { //转让中
                        bindingView.btnZrz.setVisibility(View.VISIBLE);
                    } else if (transferState.equals("2")) {
                        bindingView.btnYzr.setVisibility(View.VISIBLE);
                    }
                    break;
                case "2": //待买家收货
                    bindingView.tvStatus.setText("待买家收货");
                    break;
                case "4": //判断是否为待评价
                    if (isComment.equals("1")) { //买家已评价
                        bindingView.tvStatus.setText("买家已评价");
                    } else { //待买家评价
                        bindingView.tvStatus.setText("待买家评价");
                    }
                    break;
                case "5"://已完成
                    bindingView.tvStatus.setText("已完成");
                    //展示评价
                    bindingView.rlComment.setVisibility(View.VISIBLE);
                    showComment(bean.getId(), productId);
                    break;
                case "100"://已取消
                    bindingView.tvStatus.setText("订单已取消");
                    bindingView.rlPayTime.setVisibility(View.GONE);
                    bindingView.rlWuliu.setVisibility(View.GONE);
                    break;
            }
        } else if (mType == 2) { //售后
            //隐藏付款时间
            bindingView.rlPayTime.setVisibility(View.GONE);
            //subStatus	Long 售后处理状态 0:未申请 1:审核中 2:审核驳回, 3:退款中 4.已退款
            String subStatus = bean.getOrderItemData().get(0).getSubStatus();
            switch (subStatus) {
                case "0":
                    bindingView.btnSqsh.setVisibility(View.VISIBLE);
                    bindingView.btnShz.setVisibility(View.GONE);
                    break;
                case "1":
                    bindingView.btnSqsh.setVisibility(View.GONE);
                    bindingView.btnShz.setVisibility(View.VISIBLE);
                    bindingView.btnShz.setText("审核中");
                    break;
                case "2":
                    bindingView.btnSqsh.setVisibility(View.GONE);
                    bindingView.btnShz.setVisibility(View.VISIBLE);
                    bindingView.btnShz.setText("审核驳回");
                    break;
                case "3":
                    bindingView.btnSqsh.setVisibility(View.GONE);
                    bindingView.btnShz.setVisibility(View.VISIBLE);
                    bindingView.btnShz.setText("退款中");
                    break;
                case "4":
                    bindingView.btnSqsh.setVisibility(View.GONE);
                    bindingView.btnShz.setVisibility(View.VISIBLE);
                    bindingView.btnShz.setText("已退款");
                    break;
            }
            //申请售后
            bindingView.btnSqsh.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    SqshActivity.goPage(getActivity(), bean.getId(), bean.getOrderItemData().get(0).getId(), subStatus, bean.getOrderCode(),
                            bean.getOrderItemData().get(0).getProductId(), bean.getOrderItemData().get(0).getProductName(), bean.getOrderItemData().get(0).getSkuQty(), bean.getOrderItemData().get(0).getSkuPrice()+"", bean.getOrderItemData().get(0).getSkuAmount()+"");
                }
            });
        }
        //点击事件
        //待付款
        bindingView.btnDfk.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                goPay(bean);
            }
        });
        //取消订单
        bindingView.btnQxdd.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(),
                        "", "确认取消此订单？",
                        "确认取消", "我再想想", new DialogWithYesOrNoUtils.DialogCallBack() {
                            @Override
                            public void exectEvent(DialogInterface dialog) {
                                updateOrderStatus(bean, 100, null, null, null);
                            }

                            @Override
                            public void exectCancel(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });

            }
        });
        //确认收货
        bindingView.btnDsh.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                updateOrderStatus(bean, 4, null, null, null);
            }
        });
        //确认发货
        bindingView.btnDfh.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                String name = bindingView.etWuliuCompany.getText().toString().trim();
                String code = bindingView.etWuliuCode.getText().toString().trim();
                if (!StringUtils.isNoEmpty(name)) {
                    ToastUtil.showShortToast("请填写物流公司");
                    return;
                }
                if (!StringUtils.isNoEmpty(code)) {
                    ToastUtil.showShortToast("请填写快递单号");
                    return;
                }
                updateOrderStatus(bean, 2, "0", name, code);
            }
        });
        //转让和取消转让
//        String finalTransferState = transferState;
        bindingView.btnSszr.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                updateOrderItemForm(bean);
            }
        });
        bindingView.btnQxzr.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                updateOrderItemForm(bean);
            }
        });
        //评价
        bindingView.btnDpj.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                doComment(bean);
            }
        });
    }

    /**
     * 去付款
     */
    private void goPay(OrderInfo bean) {
        //查询该商品支持的支付方式
        mProductModel.getProductById(getActivity(), bean.getOrderItemData().get(0).getProductId(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                ProductInfo data = (ProductInfo) object;
                if (data != null) {
                    String payType = data.getPayType();
                    CommonOrderPayActivity.goPage(getActivity(),
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
     * 评价
     *
     * @param bean
     */
    private void doComment(OrderInfo bean) {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_comment)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, BaseNiceDialog dialog) {
                        ImageView ivBack = holder.getConvertView().findViewById(R.id.iv_back);
                        ivBack.setOnClickListener(new PerfectClickListener() {
                            @Override
                            protected void onNoDoubleClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        @SuppressLint("CutPasteId") StarBar starBar = holder.getConvertView().findViewById(R.id.starBar);
                        @SuppressLint("CutPasteId") TextView tvScore = holder.getConvertView().findViewById(R.id.tv_score);
                        @SuppressLint("CutPasteId") TagFlowLayout flowlayout = holder.getConvertView().findViewById(R.id.flowlayout);
                        @SuppressLint("CutPasteId") EditText etContent = holder.getConvertView().findViewById(R.id.et_content);
                        @SuppressLint("CutPasteId") RoundTextView tvNext = holder.getConvertView().findViewById(R.id.tv_next);
                        starBar.setIntegerMark(true);
                        starBar.setStarMark(5f);
                        starBar.setOnStarChangeListener(mark -> {
                            markScore = (int) mark;
                            switch (markScore) {
                                case 0:
                                    tvScore.setText("1.0分");
                                    break;
                                case 1:
                                    tvScore.setText("1.0分");
                                    break;
                                case 2:
                                    tvScore.setText("2.0分");
                                    break;
                                case 3:
                                    tvScore.setText("3.0分");
                                    break;
                                case 4:
                                    tvScore.setText("4.0分");
                                    break;
                                case 5:
                                    tvScore.setText("5.0分");
                                    break;
                                default:
                                    tvScore.setText("5.0分");
                                    break;
                            }
                        });

                        //获取标签数据
                        String classId = bean.getOrderItemData().get(0).getClassId();
                        ArrayList<String> tagIds = new ArrayList<>();
                        mOrderModel.getEvaluationTagList(getActivity(), classId, new RequestImpl() {
                            @Override
                            public void loadSuccess(Object object) {
                                List<EvaluationTagList> data = (List<EvaluationTagList>) object;
                                ArrayList<String> listNameTag = new ArrayList<>();
                                if (data != null && data.size() > 0) {
                                    for (EvaluationTagList datum : data) {
                                        listNameTag.add(datum.getName());
                                        tagIds.add(datum.getId());
                                    }
                                    //设置标签数据
                                    flowlayout.setAdapter(new TagAdapter<String>(listNameTag) {
                                        @Override
                                        public View getView(FlowLayout parent, int position, String s) {
                                            TextView tv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.flow_item,
                                                    flowlayout, false);
                                            tv.setText(s);
                                            return tv;
                                        }
                                    });
                                }
                            }

                            @Override
                            public void loadFailed(int errorCode, String errorMessage) {
                                ToastUtil.showShortToast(errorMessage);
                            }
                        });
                        //提交评价
                        tvNext.setOnClickListener(new PerfectClickListener() {
                            @Override
                            protected void onNoDoubleClick(View v) {
                                StringBuilder builder = new StringBuilder();
                                Set<Integer> list = flowlayout.getSelectedList();
                                if (list.size() < 1) {
                                    ToastUtil.showShortToast("请选择评价标签");
                                    return;
                                }
                                List<Integer> setList = new ArrayList<>(list);
                                for (int i = 0; i < setList.size(); i++) {
                                    builder.append(tagIds.get(i));
                                    builder.append(",");
                                }
                                //去掉最后一个逗号
                                String evaluationTagIds = builder.toString();
                                evaluationTagIds = evaluationTagIds.substring(0, evaluationTagIds.length() - 1);
                                if (!StringUtils.isNoEmpty(evaluationTagIds)) {
                                    ToastUtil.showShortToast("请选择评价标签");
                                    return;
                                }
                                String content = etContent.getText().toString().trim();
                                if (!StringUtils.isNoEmpty(content)) {
                                    ToastUtil.showShortToast("请输入评价内容");
                                    return;
                                }
                                mOrderModel.addEvaluationForm(getActivity(),
                                        "1",
                                        BusinessUtils.getBindAccountId(),
                                        evaluationTagIds,
                                        bean.getId(),
                                        bean.getOrderItemData().get(0).getProductId(),
                                        "0",
                                        bean.getOrderItemData().get(0).getSkuId(),
                                        markScore,
                                        content, new RequestImpl() {
                                            @Override
                                            public void loadSuccess(Object object) {
                                                ToastUtil.showShortToast("评价成功");
                                                dialog.dismiss();
                                                //TODO 这里直接重新请求数据 方便显示已完成数据
                                                loadData();
                                                bindingView.btnDpj.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void loadFailed(int errorCode, String errorMessage) {
                                                ToastUtil.showShortToast(errorMessage);
                                            }
                                        });
                            }
                        });
                    }
                })
                .setDimAmount(0.3f)
                .setShowBottom(true)
                .setOutCancel(true)
                .show(getActivity().getSupportFragmentManager());
    }

    /**
     * 订单取消 100
     * 订单确认收货 4
     */
    private void updateOrderStatus(OrderInfo data,
                                   int type,
                                   String logisticsId,
                                   String logisticsName,
                                   String waybill) {
        mOrderModel.updateOrderStatus(getActivity(), data.getId(), type, logisticsId, logisticsName, waybill, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                switch (type) {
                    case 100:
                        ToastUtil.showShortToast("订单取消成功");

                        onBackPressed();
                        break;
                    case 4:
                        ToastUtil.showShortToast("确认收货成功");
                        //隐藏按钮
                        bindingView.btnDsh.setVisibility(View.GONE);
                        onBackPressed();
                        break;
                    case 2:
                        ToastUtil.showShortToast("发货成功");
                        onBackPressed();
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
    private void updateOrderItemForm(OrderInfo data) {
        //卖家别动
        if (mType == 1) {
            return;
        }
        String classCode = data.getOrderItemData().get(0).getClassCode();
        String transferState = data.getOrderItemData().get(0).getTransferState();
//        if (classCode.equals("pfq") || classCode.equals("bpq")) {
//            ToastUtil.showShortToast("批发区和爆品区商品不能转让");
//            return;
//        }
        if (transferState.equals("-1")) {
            ToastUtil.showShortToast("该商品不能转让");
            return;
        }
        mProductModel.getProductById(getActivity(), data.getOrderItemData().get(0).getProductId(), new RequestImpl() {
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
                    mOrderModel.updateOrderItemForm(getActivity(), data.getOrderItemData().get(0).getId(), type, new RequestImpl() {
                        @Override
                        public void loadSuccess(Object object) {
                            switch (transferState) {
                                case "0": //点完转让 变成取消转让（转让中...）
                                    ToastUtil.showShortToast("提交转让申请...");
                                    bindingView.btnSszr.setVisibility(View.GONE);
                                    bindingView.btnQxzr.setVisibility(View.VISIBLE);
                                    break;
                                case "1": //点完取消转让 变成可以转让
                                    ToastUtil.showShortToast("已取消转让");
                                    bindingView.btnSszr.setVisibility(View.VISIBLE);
                                    bindingView.btnQxzr.setVisibility(View.GONE);
                                    break;
                            }
                            // 转让完修改 transferState 值
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

    /**
     * 展示评价
     *
     * @param id
     * @param productId
     */
    private void showComment(String id, String productId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "1"); //评价类型(1:商品评价类别,2:店铺评价类别)
            jsonObject.put("orderId", id);
            jsonObject.put("memberId", BusinessUtils.getBindAccountId());
            jsonObject.put("productId", productId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getEvaluationList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), 0, 10)
                .compose(RxSchedulers.compose())
                .compose(this.bindToLifecycle())
                .subscribe(new Observer<BaseBean<List<EvaluationList>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseBean<List<EvaluationList>> listBaseBean) {
                        if (listBaseBean == null) return;
//                        int total = listBaseBean.getTotal();
//                        if (total <= 0) {
//                            bindingView.rlMoreComment.setVisibility(View.GONE);
//                        } else {
//                            bindingView.rlMoreComment.setVisibility(View.VISIBLE);
//                            bindingView.tvCommentTitle.setText("用户评价" + "(" + total + ")");
//                        }
                        List<EvaluationList> data = listBaseBean.getData();
                        mCommentAdapter.clear();
                        mCommentAdapter.addAll(data);
                        mCommentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static void goPage(Context context, String id, int type) {
        Intent intent = new Intent(context, OrderInfoActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
//        intent.putExtra("handleStatus", handleStatus);
        context.startActivity(intent);
    }
}
