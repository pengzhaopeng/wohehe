package com.messoft.gaoqin.wanyiyuan.adapter;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.roundview.RoundTextView;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewAdapter;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.BaseRecyclerViewHolder;
import com.messoft.gaoqin.wanyiyuan.bean.GetRushGoodsTypeList;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ItemQgListBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.model.QgModel;
import com.messoft.gaoqin.wanyiyuan.ui.my.MyQgListActivity;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

import java.util.List;

/**
 * 抢购列表
 */
public class QgListAdapter extends BaseRecyclerViewAdapter<GetRushGoodsTypeList> {

    private BaseActivity mActivity;
    private QgModel mQgModel;
    private LoginModel mLoginModel;

    public QgListAdapter(BaseActivity activity) {
        mActivity = activity;
        mQgModel = new QgModel();
        mLoginModel = new LoginModel();
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ViewHolder(parent, R.layout.item_qg_list);
    }


    class ViewHolder extends BaseRecyclerViewHolder<GetRushGoodsTypeList, ItemQgListBinding> {

        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final GetRushGoodsTypeList data, final int position) {
            if (data != null) {
                binding.btn.setEnabled(true);
                if (data.getImgsList() != null && data.getImgsList().size() > 0) {
                    ImgLoadUtil.displayEspImage(SysUtils.getImgURL(data.getImgsList().get(0)), binding.iv, 1, TimeUtils.date2TimeStamp(data.getUpdateTime()));
                }
                //抢购时间
                binding.tvTime.setText(TimeUtils.timeFormat(data.getBeginPanicTime(), "HH:mm:ss") + " - " +
                        TimeUtils.timeFormat(data.getEndPanicTime(), "HH:mm:ss"));
                binding.tvTitle.setText(data.getTypeName());
                binding.tvStf.setText("预约扣取权益分：" + data.getEcoFee());
                binding.tvMoney.setText("(" + data.getPriceSection() + ")");
                //状态 orderList
                //+ beginPanicTime
                //+ endPanicTime
                //[]可以申请，nowTime>beginPanicTime失效
                //[0]立即抢购，<time>
                //[2]已申请
                Long aLongStart = TimeUtils.longTimeToDay1(data.getBeginPanicTime());
                Long aLongEnd = TimeUtils.longTimeToDay1(data.getEndPanicTime());

                List<GetRushGoodsTypeList.OrderListBean> orderList = data.getOrderList();
                if (orderList != null && orderList.size() > 0) {
                    String status = orderList.get(0).getStatus();
                    if (aLongStart >= 0 && aLongEnd <= 0) {
                        if (!status.equals("2")) {
                            binding.btn.getDelegate().setBackgroundColor(Color.parseColor("#EF3A64"));
                            binding.btn.setText("立即抢购");
                        } else {
                            binding.btn.getDelegate().setBackgroundColor(Color.parseColor("#73FF9800"));
                            binding.btn.setText("已抢购");
                        }
                    } else {
                        switch (status) {
                            case "0":
                                binding.btn.getDelegate().setBackgroundColor(Color.parseColor("#EF3A64"));
                                binding.btn.setText("已预约");
                                break;
                            case "2":
                                binding.btn.getDelegate().setBackgroundColor(Color.parseColor("#73FF9800"));
                                binding.btn.setText("已申请");
                                break;
                        }
                    }

                } else { //可申请
                    //现在的时间 < 抢购时间才能点，否则显示抢购
//                    Long aLong = TimeUtils.longTimeToDay1(data.getBeginPanicTime());
                    if (aLongStart < 0) {
                        binding.btn.getDelegate().setBackgroundColor(Color.parseColor("#FF9800"));
                        binding.btn.setText("申请预约");
                    } else if (aLongStart >= 0 && aLongEnd <= 0) {
                        binding.btn.getDelegate().setBackgroundColor(Color.parseColor("#EF3A64"));
                        binding.btn.setText("立即抢购");
                    } else {
                        binding.btn.getDelegate().setBackgroundColor(Color.parseColor("#73FF9800"));
                        binding.btn.setText("已结束");
                    }

                }
                binding.btn.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        if (orderList != null && orderList.size() > 0) {
                            String status = orderList.get(0).getStatus();
                            switch (status) {
                                case "0": //已经预约了 并且 可以立即抢购
//                                    ToastUtil.showShortToast("抢购未开始");
                                    Long aLongStart = TimeUtils.longTimeToDay1(data.getBeginPanicTime());
                                    Long aLongEnd = TimeUtils.longTimeToDay1(data.getEndPanicTime());
                                    if (aLongStart >= 0 && aLongEnd <= 0) {
                                        //立即抢购
                                        goQg(data, binding.btn);

                                    } else if (aLongStart < 0) {
                                        showFailDialog("温馨提示", "抢购未开始");
                                        return;
                                    } else if (aLongEnd > 0) {
                                        showFailDialog("温馨提示", "抢购已结束");
                                        return;
                                    }
                                    break;
                                case "2"://已申请
                                    showFailDialog("温馨提示", "您已申请过了，无需重复申请");
                                    break;
                            }
                        } else {
                            //可申请
                            //现在的时间 < 抢购时间才能点，否则失效
                            //2020/1/3改动 现在的时间 < 抢购时间才能申请 否则可以抢购
//                            Long aLong = TimeUtils.longTimeToDay1(data.getBeginPanicTime());
                            Long aLongStart = TimeUtils.longTimeToDay1(data.getBeginPanicTime());
                            Long aLongEnd = TimeUtils.longTimeToDay1(data.getEndPanicTime());
                            if (aLongStart < 0) {
                                mLoginModel.getLoginMemberInfo(mActivity, new RequestImpl() {
                                    @Override
                                    public void loadSuccess(Object object) {
                                        LoginMemberInfo bean = (LoginMemberInfo) object;
                                        if (bean != null) {
                                            String msg = "预约需要扣取 " + data.getEcoFee() + "分 权益值，购买时不再扣取，预约不成功将返回权益值，是否继续？";
                                            DialogWithYesOrNoUtils.getInstance().showDialog(mActivity, "温馨提示", msg, "立即预约", "取消", new DialogWithYesOrNoUtils.DialogCallBack() {
                                                @Override
                                                public void exectEvent(DialogInterface dialog) {
                                                    dialog.dismiss();
                                                    //申请预约
                                                    mQgModel.addRushOrderForm(mActivity, BusinessUtils.getBindAccountId(),
                                                            data.getGoodsTypeId(), BusinessUtils.getMobile(),
                                                            bean.getEcologyPoints() + "",
                                                            bean.getCreditScore(),
                                                            new RequestImpl() {
                                                                @Override
                                                                public void loadSuccess(Object object) {
                                                                    ToastUtil.showLongToast("预约成功");
                                                                    binding.btn.setText("已预约");
                                                                    binding.btn.setEnabled(false);
                                                                    //删除这条数据
//                                                            remove(position);
//                                                            notifyDataSetChanged();
                                                                    RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_QG_LIST, 0));
                                                                }

                                                                @Override
                                                                public void loadFailed(int errorCode, String errorMessage) {
                                                                    showFailDialog("温馨提示", errorMessage);
                                                                }
                                                            });
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

                            } else if (aLongStart >= 0 && aLongEnd <= 0) {  //可抢购
                                //立即抢购
                                goQg(data, binding.btn);
                            } else {
                                showFailDialog("温馨提示", "抱歉，抢购活动已结束!");
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * 立即抢购
     *
     * @param data
     * @param btn
     */
    private void goQg(GetRushGoodsTypeList data, RoundTextView btn) {
        mLoginModel.getLoginMemberInfo(mActivity, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                LoginMemberInfo bean = (LoginMemberInfo) object;
                if (bean != null) {
                    mQgModel.autoAllotGoodsOrder(mActivity, BusinessUtils.getBindAccountId(),
                            data.getGoodsTypeId(),
                            BusinessUtils.getMobile(),
                            bean.getEcologyPoints() + "",
                            bean.getCreditScore(),
                            new RequestImpl() {
                                @Override
                                public void loadSuccess(Object object) {
                                    btn.setText("已抢购");
                                    btn.setEnabled(false);

                                    DialogWithYesOrNoUtils.getInstance().showDialog(mActivity, "抢购成功", "恭喜你！[" + data.getTypeName() + "]抢购成功，请前往查看详情", "立即前往", "取消", new DialogWithYesOrNoUtils.DialogCallBack() {
                                        @Override
                                        public void exectEvent(DialogInterface dialog) {
                                            dialog.dismiss();
                                            SysUtils.startActivity(mActivity, MyQgListActivity.class);
                                        }

                                        @Override
                                        public void exectCancel(DialogInterface dialog) {
                                            dialog.dismiss();
                                        }
                                    });
                                    //刷新列表
                                    RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_QG_LIST, 0));
//                                                            ToastUtil.showLongToast("恭喜您，抢购到了！");
                                }

                                @Override
                                public void loadFailed(int errorCode, String errorMessage) {
//                                                            ToastUtil.showShortToast(errorMessage);
                                    if (errorCode == 1) {
                                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_QG_LIST, 0));
                                    }
                                    showFailDialog("抢购失败", "[" + data.getTypeName() + "]" + errorMessage);
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

    private void showFailDialog(String title, String msg) {
        DialogWithYesOrNoUtils.getInstance().showDialog(mActivity, title, msg, "知道了", new DialogWithYesOrNoUtils.DialogCallBack() {
            @Override
            public void exectEvent(DialogInterface dialog) {
                dialog.dismiss();
            }

            @Override
            public void exectCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }
}
