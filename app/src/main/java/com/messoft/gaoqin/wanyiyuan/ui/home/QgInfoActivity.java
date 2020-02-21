package com.messoft.gaoqin.wanyiyuan.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.flyco.roundview.RoundTextView;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.GetRushGoodsTypeList;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityQgInfoBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.model.QgModel;
import com.messoft.gaoqin.wanyiyuan.ui.my.MyQgListActivity;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.GlideImageLoader;
import com.messoft.gaoqin.wanyiyuan.utils.HtmlFormat;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.WebViewUtils;
import com.messoft.gaoqin.wanyiyuan.view.viewbigimage.ViewBigImageActivity;
import com.messoft.gaoqin.wanyiyuan.view.webview.MyWebViewClient;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 抢购详情
 */
public class QgInfoActivity extends BaseActivity<ActivityQgInfoBinding> {

    private QgModel mQgModel;
    private LoginModel mLoginModel;
    private String goodsTypeId;
    private ArrayList<String> bannerList; //轮播图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qg_info);
    }

    @Override
    protected void initSetting() {
        showContentView();
        setTitle("产品详情");
        mQgModel = new QgModel();
        mLoginModel = new LoginModel();
        if (getIntent() != null) {
            goodsTypeId = getIntent().getStringExtra("goodsTypeId");
        }
        initBanner();
        loadData();
    }

    private void initBanner() {
        bannerList = new ArrayList<>();
        //动态设置banner宽高
        LinearLayout.LayoutParams bannerParams = (LinearLayout.LayoutParams) bindingView.banner.getLayoutParams();
        bannerParams.width = DensityUtils.getScreenWidth(getActivity());
        bannerParams.height = DensityUtils.getScreenWidth(getActivity());
        bindingView.banner.setLayoutParams(bannerParams);
        // 显示时轮播图滚动
        if (bindingView != null && bindingView.banner != null) {
            bindingView.banner.startAutoPlay();
            bindingView.banner.setDelayTime(4000);
        }
    }

    @Override
    protected void initListener() {
        bindingView.wvContent.setWebViewClient(new MyWebViewClient() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onPageFinished(WebView view, String url) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageFinished(view, url);
                addImageClickListener(view);//待网页加载完全后设置图片点击的监听方法
                view.loadUrl(String.format(Locale.CHINA, "javascript:document.body.style.paddingTop='%fpx'; void 0",
                        DensityUtil.px2dp(bindingView.wvContent.getPaddingTop())));

            }

            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            private void addImageClickListener(WebView webView) {
                webView.loadUrl("javascript:(function(){" +
                        "var objs = document.getElementsByTagName(\"img\"); " +
                        "for(var i=0;i<objs.length;i++)  " +
                        "{"
                        + "    objs[i].onclick=function()  " +
                        "    {  "
                        + "        window.imagelistener.openImage(this.src);  " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                        "    }  " +
                        "}" +
                        "})()");
            }
        });
    }

    private void loadData() {
        mQgModel.getRushGoodsTypeById(getActivity(), goodsTypeId, BusinessUtils.getBindAccountId(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                GetRushGoodsTypeList bean = (GetRushGoodsTypeList) object;
                if (bean != null) {
                    setBanner(bean.getImgsList(),bean.getUpdateTime());
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
    private void setData(GetRushGoodsTypeList data) {
        //详情
        WebViewUtils.initWeb(getActivity(), bindingView.wvContent);
        //网页内容
        if (StringUtils.isNoEmpty(data.getContent())) {
            String msg = data.getContent();
            if (!data.getContent().startsWith("<p>")) { //加 <p> 标签
                msg = "<p>"+data.getContent()+"</p>";
            }
            bindingView.wvContent.loadData(HtmlFormat.getNewContent(msg), "text/html; charset=UTF-8", null); // 加载定义的代码，并设定编码格式和字符集。
        }

        bindingView.tvName.setText(data.getTypeName());
        bindingView.tvPrice.setText("(" + data.getPriceSection() + ")");
        //抢购时间
        bindingView.tvTime.setText("——抢购时间："+TimeUtils.timeFormat(data.getBeginPanicTime(), "HH:mm:ss") + " - " +
                TimeUtils.timeFormat(data.getEndPanicTime(), "HH:mm:ss"));
        bindingView.tvPoints.setText("——预约扣取权益分：" + data.getEcoFee());
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
                    bindingView.btn.getDelegate().setBackgroundColor(Color.parseColor("#EF3A64"));
                    bindingView.btn.setText("立即抢购");
                }else{
                    bindingView.btn.getDelegate().setBackgroundColor(Color.parseColor("#73FF9800"));
                    bindingView.btn.setText("已抢购");
                }
            } else {
                switch (status) {
                    case "0":
                        bindingView.btn.getDelegate().setBackgroundColor(Color.parseColor("#EF3A64"));
                        bindingView.btn.setText("已预购");
                        break;
                    case "2":
                        bindingView.btn.getDelegate().setBackgroundColor(Color.parseColor("#73FF9800"));
                        bindingView.btn.setText("已申请");
                        break;
                }
            }

        }  else { //可申请
            //现在的时间 < 抢购时间才能点，否则显示抢购
//                    Long aLong = TimeUtils.longTimeToDay1(data.getBeginPanicTime());

            if (aLongStart < 0) {
                bindingView.btn.getDelegate().setBackgroundColor(Color.parseColor("#FF9800"));
                bindingView.btn.setText("申请预约");
            } else if (aLongStart >= 0 && aLongEnd <= 0) {
                bindingView.btn.getDelegate().setBackgroundColor(Color.parseColor("#EF3A64"));
                bindingView.btn.setText("立即抢购");
            } else {
                bindingView.btn.getDelegate().setBackgroundColor(Color.parseColor("#73FF9800"));
                bindingView.btn.setText("已结束");
            }

        }

        bindingView.btn.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (orderList != null && orderList.size() > 0) {
                    String status = orderList.get(0).getStatus();
                    switch (status) {
                        case "0"://已经预约了 并且 可以立即抢购
                            Long aLongStart = TimeUtils.longTimeToDay1(data.getBeginPanicTime());
                            Long aLongEnd = TimeUtils.longTimeToDay1(data.getEndPanicTime());
                            if (aLongStart >= 0 && aLongEnd <= 0) {
                                //立即抢购
                                goQg(data,bindingView.btn);

                            } else if (aLongStart < 0) {
                                showFailDialog("温馨提示","抢购未开始");
                                return;
                            } else if (aLongEnd > 0) {
                                showFailDialog("温馨提示","抢购已结束");

                                return;
                            }
                            break;
                        case "2"://已申请
                            showFailDialog("温馨提示","您已申请过了，无需重复申请");
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
                        mLoginModel.getLoginMemberInfo(getActivity(), new RequestImpl() {
                            @Override
                            public void loadSuccess(Object object) {
                                LoginMemberInfo bean = (LoginMemberInfo) object;
                                if (bean != null) {
                                    //申请预约
                                    String msg = "预约需要扣取 " + data.getEcoFee() + "分 权益分，购买时不再扣取，预约不成功将返回预约分，是否继续？";
                                    DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(), "温馨提示", msg, "立即预约", "取消", new DialogWithYesOrNoUtils.DialogCallBack() {
                                        @Override
                                        public void exectEvent(DialogInterface dialog) {
                                            dialog.dismiss();
                                            //申请预约
                                            mQgModel.addRushOrderForm(getActivity(), BusinessUtils.getBindAccountId(),
                                                    data.getGoodsTypeId(), BusinessUtils.getMobile(),
                                                    bean.getEcologyPoints() + "",
                                                    bean.getCreditScore(),
                                                    new RequestImpl() {
                                                        @Override
                                                        public void loadSuccess(Object object) {
                                                            ToastUtil.showLongToast("预约成功");
                                                            bindingView.btn.setText("已预约");
//                                                            data.getOrderList().get(0).setStatus("0");
                                                            bindingView.btn.setEnabled(false);
                                                            RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_QG_LIST, 0));

                                                            loadData();
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

                            }

                            @Override
                            public void loadFailed(int errorCode, String errorMessage) {
                                ToastUtil.showShortToast(errorMessage);
                            }
                        });

                    } else if (aLongStart >= 0 && aLongEnd <= 0) {  //可抢购
                        //立即抢购
                        goQg(data, bindingView.btn);
                    } else {
                        showFailDialog("温馨提示","抱歉，抢购活动已结束!");
                    }
                }
            }
        });
    }

    private void goQg(GetRushGoodsTypeList data, RoundTextView btn) {
        mLoginModel.getLoginMemberInfo(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                LoginMemberInfo bean = (LoginMemberInfo) object;
                if (bean != null) {
                    mQgModel.autoAllotGoodsOrder(getActivity(), BusinessUtils.getBindAccountId(),
                            data.getGoodsTypeId(),
                            BusinessUtils.getMobile(),
                            bean.getEcologyPoints() + "",
                            bean.getCreditScore(),
                            new RequestImpl() {
                                @Override
                                public void loadSuccess(Object object) {
                                    btn.setText("已抢购");
                                    btn.setEnabled(false);

                                    DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(), "抢购成功", "恭喜你！[" + data.getTypeName() + "]抢购成功，请前往查看详情", "立即前往", "取消", new DialogWithYesOrNoUtils.DialogCallBack() {
                                        @Override
                                        public void exectEvent(DialogInterface dialog) {
                                            dialog.dismiss();
                                            SysUtils.startActivity(getActivity(), MyQgListActivity.class);
                                        }

                                        @Override
                                        public void exectCancel(DialogInterface dialog) {
                                            dialog.dismiss();
                                        }
                                    });
                                    RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_QG_LIST, 0));
                                }

                                @Override
                                public void loadFailed(int errorCode, String errorMessage) {
//                                    ToastUtil.showShortToast(errorMessage);
                                    if (errorCode == 1) {
                                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_QG_LIST, 0));
                                    }
                                    showFailDialog("抢购失败","[" + data.getTypeName() + "]" + errorMessage);
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

    private void showFailDialog(String title,String msg){
        DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(), title, msg, "知道了", new DialogWithYesOrNoUtils.DialogCallBack() {
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

    /**
     * 设置banner
     *
     * @param imgsList
     * @param updateTime
     */
    private void setBanner(List<String> imgsList, String updateTime) {
        if (imgsList == null || imgsList.size() < 1) return;
        bannerList.clear();
        for (String s : imgsList) {
            bannerList.add(SysUtils.getImgURL(s));
        }
        bindingView.banner.setBannerAnimation(Transformer.Default);
        bindingView.banner.setOnBannerClickListener(position -> {
            // 链接没有做缓存，如果轮播图使用的缓存则点击图片无效
            position = position - 1;
            //点击放大
            Bundle bundle = new Bundle();
            bundle.putInt("selet", 2);// 2,大图显示当前页数，1,头像，不显示页数
            bundle.putInt("code", position);//第几张
            bundle.putStringArrayList("imageuri", bannerList);
            Intent intent = new Intent(getActivity(), ViewBigImageActivity.class);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        });
        //放最后
        bindingView.banner.setImages(bannerList).setImageLoader(new GlideImageLoader(TimeUtils.date2TimeStamp(updateTime))).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 开始图片请求
        Glide.with(getActivity()).resumeRequests();
    }

    public static void goPage(Context context, String goodsTypeId) {
        Intent intent = new Intent(context, QgInfoActivity.class);
        intent.putExtra("goodsTypeId", goodsTypeId);
        context.startActivity(intent);
    }
}
