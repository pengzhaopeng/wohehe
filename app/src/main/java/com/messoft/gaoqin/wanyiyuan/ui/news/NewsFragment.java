package com.messoft.gaoqin.wanyiyuan.ui.news;

import android.view.Gravity;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseFragment;
import com.messoft.gaoqin.wanyiyuan.bean.BaseBean;
import com.messoft.gaoqin.wanyiyuan.bean.GetMemberNotifyList;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.FragmentNewsBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import q.rorbin.badgeview.QBadgeView;

public class NewsFragment extends BaseFragment<FragmentNewsBinding> {

    private QBadgeView badge1, badge2, badge3, badge4, badge5;
    private RxBus mRxBus;
    // 初始化完成后加载数据
    private boolean mIsPrepared = false;
    // 第一次显示时加载数据，第二次不显示
    private boolean mIsFirst = true;

    @Override
    protected int setContent() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initSetting() {
        initRxBus();
        showContentView();

        //初始化消息红点
        initBadge();
        mIsPrepared = true;
        loadData();
    }

    @Override
    protected void initListener() {
        bindingView.cwClgl.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                MyNewsActivity.goPage(getActivity(),1,"车辆管理");
            }
        });

        bindingView.cwSytz.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                MyNewsActivity.goPage(getActivity(),2,"收益通知");
            }
        });

        bindingView.cwDdtz.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
//                ToastUtil.showShortToast("暂未开放...");
                MyNewsActivity.goPage(getActivity(),3,"订单通知");
            }
        });

        bindingView.cwXtxx.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                MyNewsActivity.goPage(getActivity(),4,"系统消息");
            }
        });

        bindingView.cwHdgg.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                MyNewsActivity.goPage(getActivity(),5,"活动公告");
            }
        });
    }

    @Override
    protected void loadData() {
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }
        synchronized (this) {
            requestData();
        }
    }

    /**
     * 消息红点init
     */
    private void initBadge() {
        //车辆管理
        badge1 = new QBadgeView(getActivity());
        bindingView.cwClgl.getTvRight().setVisibility(View.VISIBLE);
        badge1.bindTarget(bindingView.cwClgl.getTvRight())
                .stroke(getResources().getColor(R.color.red), 1, true)
                .setBadgeGravity(Gravity.END | Gravity.TOP)
                .setGravityOffset(0, 16, true);
        badge1.setVisibility(View.GONE);

        //收益通知
        bindingView.cwSytz.getTvRight().setVisibility(View.VISIBLE);
        badge2 = new QBadgeView(getActivity());
        badge2.bindTarget(bindingView.cwSytz.getTvRight())
                .stroke(getResources().getColor(R.color.red), 1, true)
                .setBadgeGravity(Gravity.END | Gravity.TOP)
                .setGravityOffset(0, 16, true);
        badge2.setVisibility(View.GONE);

        //订单通知
        bindingView.cwDdtz.getTvRight().setVisibility(View.VISIBLE);
        badge3 = new QBadgeView(getActivity());
        badge3.bindTarget(bindingView.cwDdtz.getTvRight())
                .stroke(getResources().getColor(R.color.red), 1, true)
                .setBadgeGravity(Gravity.END | Gravity.TOP)
                .setGravityOffset(0, 16, true);
        badge3.setVisibility(View.GONE);

        //系统消息
        bindingView.cwXtxx.getTvRight().setVisibility(View.VISIBLE);
        badge4 = new QBadgeView(getActivity());
        badge4.bindTarget(bindingView.cwXtxx.getTvRight())
                .stroke(getResources().getColor(R.color.red), 1, true)
                .setBadgeGravity(Gravity.END | Gravity.TOP)
                .setGravityOffset(0, 16, true);
        badge4.setVisibility(View.GONE);

        //活动公告
        bindingView.cwHdgg.getTvRight().setVisibility(View.VISIBLE);
        badge5 = new QBadgeView(getActivity());
        badge5.bindTarget(bindingView.cwHdgg.getTvRight())
                .stroke(getResources().getColor(R.color.red), 1, true)
                .setBadgeGravity(Gravity.END | Gravity.TOP)
                .setGravityOffset(0, 16, true);
        badge5.setVisibility(View.GONE);
    }

    /**
     * 请求数字
     */
    private void requestData() {
        //车辆提醒
        loadNews(1);
        //收益通知
        loadNews(2);
        //订单通知
//        loadNews(3);
        //系统消息
        loadNews(4);
        //活动公告
        loadNews(5);
    }

    private void loadNews(int type) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberId", BusinessUtils.getBindAccountId());
            jsonObject.put("isRead", 0);
            switch (type) {
                case 1: //车辆
                    jsonObject.put("actions", "maintain,safety,medical_examination");
                    break;
                case 2: //收益通知
                    jsonObject.put("actions", "income_notice");
                    break;
                case 3://订单通知
                    break;
                case 4: //系统消息
                    jsonObject.put("actions", "feedback");
                    break;
                case 5: //活动公告
                    jsonObject.put("type", "1");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getMemberNotifyList(StringUtils.toURLEncoderUTF8(jsonObject.toString()),
                HttpUtils.start_page_java, 1)
                .compose(RxSchedulers.compose())
                .compose(this.bindToLifecycle())
                .subscribe(new Observer<BaseBean<List<GetMemberNotifyList>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseBean<List<GetMemberNotifyList>> bean) {
                        mIsFirst = false;
                        setNewsNum(type, bean);

                    }

                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.showShortToast("未获取到评论列表 请重试");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setNewsNum(int type, BaseBean<List<GetMemberNotifyList>> bean) {
        switch (type) {
            case 1:
                if (bean.getTotal() > 0) {
                    if (badge1.getVisibility() != View.VISIBLE) {
                        badge1.setVisibility(View.VISIBLE);
                    }
                    badge1.setBadgeNumber(bean.getTotal());
                } else {
                    if (badge1.getVisibility() != View.GONE) {
                        badge1.setVisibility(View.GONE);
                    }
                }
                break;
            case 2:
                if (bean.getTotal() > 0) {
                    if (badge2.getVisibility() != View.VISIBLE) {
                        badge2.setVisibility(View.VISIBLE);
                    }
                    badge2.setBadgeNumber(bean.getTotal());
                } else {
                    if (badge2.getVisibility() != View.GONE) {
                        badge2.setVisibility(View.GONE);
                    }
                }
                break;

            case 3:
                if (bean.getTotal() > 0) {
                    if (badge3.getVisibility() != View.VISIBLE) {
                        badge3.setVisibility(View.VISIBLE);
                    }
                    badge3.setBadgeNumber(bean.getTotal());
                } else {
                    if (badge3.getVisibility() != View.GONE) {
                        badge3.setVisibility(View.GONE);
                    }
                }
                break;

            case 4:
                if (bean.getTotal() > 0) {
                    if (badge4.getVisibility() != View.VISIBLE) {
                        badge4.setVisibility(View.VISIBLE);
                    }
                    badge4.setBadgeNumber(bean.getTotal());
                } else {
                    if (badge4.getVisibility() != View.GONE) {
                        badge4.setVisibility(View.GONE);
                    }
                }
                break;

            case 5:
                if (bean.getTotal() > 0) {
                    if (badge4.getVisibility() != View.VISIBLE) {
                        badge4.setVisibility(View.VISIBLE);
                    }
                    badge4.setBadgeNumber(bean.getTotal());
                } else {
                    if (badge4.getVisibility() != View.GONE) {
                        badge4.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    /**
     * 注册RxBus
     */
    private void initRxBus() {
        mRxBus = RxBus.getInstanceBus();
        mRxBus.registerRxBus(mRxBus, RxBusMessage.class, rxBusMessage -> {
            //根据事件类型进行处理
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_MY_NEWS_NUMBER) {
                if (rxBusMessage.getI() == 0) {
                    String type = rxBusMessage.getMsg();
                    //刷新消息个数
                    if (StringUtils.isNoEmpty(type)) {
                        loadNews(Integer.parseInt(type));
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
