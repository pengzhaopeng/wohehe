package com.messoft.gaoqin.wanyiyuan.ui.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.LifeNewsListAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.GetMemberNotifyList;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.CommonListBinding;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.List;

public class MyNewsActivity extends BaseActivity<CommonListBinding> {

    private int mPage = HttpUtils.start_page_java;
    private LifeNewsListAdapter mAdapter;
    private String mMemberID;
    private int mType = 1; //1-车辆管理 2-收益通知 3-订单通知 4-系统消息 5-活动公告


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_list);
    }

    @Override
    public void onBackPressed() {
        //标记已读
        isReadList();
    }

    @Override
    protected void initSetting() {
//        setTitle("消息");
        showContentView();
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
            String title = getIntent().getStringExtra("title");
            setTitle(title);
        }
        mMemberID = BusinessUtils.getBindAccountId();
        mAdapter = new LifeNewsListAdapter(getActivity());
        initOnRefresh(getActivity(), bindingView.rc);
//        bindingView.rc.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        bindingView.rc.setAdapter(mAdapter);

        pushNews();


    }

    @Override
    protected void initListener() {
        mAdapter.setOnItemClickListener((bean, position) -> {
            //TODO
            if (bean == null) {
                return;
            }
            String type = bean.getType(); //消息类型 1:公告 Announce，2:提醒 Remind,3:信息 Message
//            switch (type) {
//                case "3": //私信
//                    LifeNewsChatActivity.goPage(getActivity(), bean.getReceiver(), bean.getSender());
//                    break;
//                case "2": //提醒 (动态提醒 )
//                    //提醒信息的动作类型(评论[comment]、喜欢[like]、关注[attention])
//                    String action = bean.getAction();
//                    switch (action) {
//                        case "attention": //关注
//                            FansListActivity.goPage(getActivity(), 0);
//                            break;
//                        case "comment":
//                        case "like": //文章or微头条详情
//                            String target = bean.getTarget();
//                            goTargetInfo(target);
//                            break;
//                    }
//                    break;
//            }
        });

        bindingView.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage = HttpUtils.start_page_java;
                        pushNews();
                        refreshlayout.finishRefresh();
                    }
                }, 300);
            }

        });
        bindingView.refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                refreshlayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage++;
                        loadNews();
                        refreshlayout.finishLoadmore();
                    }
                }, 300);

            }
        });
    }


    @SuppressWarnings("unchecked")
    private void pushNews() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberId", mMemberID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().pullNews(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(this.bindToLifecycle())
                .subscribe(new BaseObserver(getActivity(), true, "拉取新消息...") {
                    @Override
                    protected void onSuccess(Object login) {
                        loadNews();
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        ToastUtil.showShortToast(msg);
                        loadNews();
                    }
                });
    }

    private void loadNews() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberId", mMemberID);
            switch (mType) {
                case 1: //车辆
                    jsonObject.put("actions", "maintain,safety,medical_examination");
                    break;
                case 2: //收益通知
                    jsonObject.put("actions", "arrival_notice");
                    break;
                case 3://订单通知
                    jsonObject.put("actions", "order_notice");
                    break;
                case 4: //系统消息
                    jsonObject.put("actions", "system_notice");
                    break;
                case 5: //活动公告
                    jsonObject.put("type", "1");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getMemberNotifyList(StringUtils.toURLEncoderUTF8(jsonObject.toString()),
                mPage, HttpUtils.per_page_20)
                .compose(RxSchedulers.compose())
                .compose(this.bindToLifecycle())
                .subscribe(new BaseObserver<List<GetMemberNotifyList>>(getActivity(), false, "加载中...") {
                    @Override
                    protected void onSuccess(List<GetMemberNotifyList> data) {
                        showContentView();
                        if (mPage == HttpUtils.start_page_java) {
                            if (data.size() > 0) {
                                mAdapter.clear();
                                mAdapter.addAll(data);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.clear();
                                mAdapter.notifyDataSetChanged();
                                showNoData("暂无消息");
                            }
                        } else {
                            mAdapter.addAll(data);
                            mAdapter.notifyDataSetChanged();
                            bindingView.refreshLayout.finishLoadmore();
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
                        ToastUtil.showShortToast(msg);
                    }
                });
    }

    /**
     * 标记已读
     */
    @SuppressWarnings("unchecked")
    private void isReadList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberId", mMemberID);
            switch (mType) {
                case 1: //车辆
                    jsonObject.put("action", "maintain,safety,medical_examination");
                    jsonObject.put("type", "2");
                    break;
                case 2: //收益通知
                    jsonObject.put("action", "income_notice");
                    jsonObject.put("type", "2");
                    break;
                case 3://订单通知
                    break;
                case 4: //系统消息
                    jsonObject.put("action", "feedback");
                    jsonObject.put("type", "2");
                    break;
                case 5: //活动公告
                    jsonObject.put("type", "1");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().isReadNewsList(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(this.bindToLifecycle())
                .subscribe(new BaseObserver(getActivity(), true, "加载中...") {
                    @Override
                    protected void onSuccess(Object data) {
                        RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_MY_NEWS_NUMBER, 0, mType + ""));
                        finish();
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {
//                        ToastUtil.showShortToast(msg);
                        finish();
                    }
                });
    }

    public static void goPage(Context context, int type,String title) {
        Intent intent = new Intent(context, MyNewsActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }
}
