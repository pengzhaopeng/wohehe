package com.messoft.gaoqin.wanyiyuan.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.jaeger.library.StatusBarUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.MyFragmentPagerAdapter;
import com.messoft.gaoqin.wanyiyuan.app.ActivityManage;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityMainBinding;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.ui.home.HomeFragment2;
import com.messoft.gaoqin.wanyiyuan.ui.market.MarketFragment;
import com.messoft.gaoqin.wanyiyuan.ui.my.MyFragment;
import com.messoft.gaoqin.wanyiyuan.ui.news.NewsFragment;
import com.messoft.gaoqin.wanyiyuan.ui.shopcar.ShopcarFragment;
import com.messoft.gaoqin.wanyiyuan.utils.DialogWithYesOrNoUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends BaseActivity<ActivityMainBinding> {

    HomeFragment2 homeFragment;
    MyFragment myFragment;
    private RxBus mRxBus;

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorAccent),38);
    }

    @Override
    protected void initSetting() {
        showContentView();
        initRxBus();
        initPage();

        //弹出用户协议，首次登录再弹出,以后读不弹出
        popUserAgree();
    }

//    private void requestPermisson() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                requestPermisson(MainActivity.this);
//            }
//        }, 100);
//        requestPermisson(MainActivity.this);
//    }

    @Override
    protected void initListener() {

    }

    private void popUserAgree() {
        requestPermisson();
//        if (BusinessUtils.getUserAgree()) {
//            requestPermisson();
//            return;
//        }
//        NiceDialog.init()
//                .setLayoutId(R.layout.dialog_user_agree)
//                .setConvertListener((ViewConvertListener) (holder, dialog) -> {
//                    //设置
//                    WebView web = holder.getView(R.id.webView);
//                    WebViewUtils.initWeb(getActivity(), web);
//                    web.setWebViewClient(new WebViewClient() {
//                        @Override
//                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                            view.loadUrl(url);
//                            return true;
//                        }
//
//                        @Override
//                        public void onPageFinished(WebView view, String url) {
//                            super.onPageFinished(view, url);
//                            view.loadUrl(String.format(Locale.CHINA, "javascript:document.body.style.paddingTop='%fpx'; void 0", DensityUtil.px2dp(web.getPaddingTop())));
//                        }
//                    });
//                    //加载数据
//                    JSONObject jsonObject = new JSONObject();
//                    try {
//                        jsonObject.put("searchId", "s_privacy");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    HttpClient.Builder.getJavaCommonServer().getNewsList(jsonObject.toString())
//                            .compose(RxSchedulers.compose())
//                            .compose(MainActivity.this.bindToLifecycle())
//                            .subscribe(new BaseObserver<List<GetNewsList>>(getActivity(), false, "加载中...") {
//                                @Override
//                                protected void onSuccess(List<GetNewsList> login) {
//                                    List<GetNewsList> data = login;
//                                    if (data != null && data.size() > 0) {
//                                        GetNewsList getNewsList = data.get(0);
////                                        if (StringUtils.isNoEmpty(getNewsList.getTitle())){
////                                            bindingView.tvTitle.setText(getNewsList.getTitle());
////                                        }
//                                        if (StringUtils.isNoEmpty(getNewsList.getContent())) {
//                                            web.loadData(HtmlFormat.getNewContent(getNewsList.getContent()), "text/html; charset=UTF-8", null); // 加载定义的代码，并设定编码格式和字符集。
//                                        }
//                                    }
//
//                                }
//
//                                @Override
//                                protected void onFailure(int errorCode, String msg) {
//                                    ToastUtil.showShortToast(msg);
//                                }
//                            });
//                    //取消
//                    holder.setOnClickListener(R.id.btn_cancel, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(), "提示", "您需要同意《万益源隐私政策》方可以使用本软件", "我知道了", new DialogWithYesOrNoUtils.DialogCallBack() {
//                                @Override
//                                public void exectEvent(DialogInterface dialog) {
//                                    dialog.dismiss();
//                                }
//
//                                @Override
//                                public void exectCancel(DialogInterface dialog) {
//
//                                }
//                            });
//                        }
//                    });
//                    //同意
//                    holder.setOnClickListener(R.id.btn_agree, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            BusinessUtils.setUserAgree(true);
//                            dialog.dismiss();
//                            requestPermisson();
//                        }
//                    });
//
//                })
//                .setDimAmount(0.3f)
////                .setShowBottom(true)
//                .setMargin(20)
//                .setOutCancel(false)
//                .show(getSupportFragmentManager());
    }


    private void initPage() {
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        homeFragment = new HomeFragment2();
        mFragmentList.add(homeFragment);
        mFragmentList.add(new MarketFragment());
        mFragmentList.add(new ShopcarFragment());
        mFragmentList.add(new NewsFragment());
        myFragment = new MyFragment();
        mFragmentList.add(myFragment);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        bindingView.viewpager.setAdapter(adapter);
        //设置ViewPage最大缓存的页面个数（cpu消耗少）
        bindingView.viewpager.setOffscreenPageLimit(mFragmentList.size() - 1);
        bindingView.viewpager.setCurrentItem(0);
        adapter.notifyDataSetChanged();

        //去掉不显示图片默认颜色：
        bindingView.byeBurger.setItemIconTintList(null);
        // >3去除底部放大效果
//        BottomNavigationViewHelper.disableShiftMode(bindingView.byeBurger);
        bindingView.byeBurger.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                resetToDefaultIcon();//重置到默认不选中图片
                switch (item.getItemId()) {
                    case R.id.item_home: //首页
                        item.setIcon(R.drawable.item_home);
                        bindingView.viewpager.setCurrentItem(0);
                        //改变颜色
                        homeFragment.changeCurrentColor();
                        break;
                    case R.id.item_market://市场
                        if (getActivity() != null && getActivity().getWindow() != null) {
                            StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.theme_color), 0);
                        }
                        item.setIcon(R.drawable.item_market);
                        bindingView.viewpager.setCurrentItem(1);
                        break;
                    case R.id.item_shopcar://购物车
                        if (getActivity() != null && getActivity().getWindow() != null) {
                            StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.colorPrimaryDark), 0);
                        }
                        item.setIcon(R.drawable.item_shopcar);
                        bindingView.viewpager.setCurrentItem(2);
                        break;
                    case R.id.item_news://消息
                        if (getActivity() != null && getActivity().getWindow() != null) {
                            StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.colorPrimaryDark), 0);
                        }
                        item.setIcon(R.drawable.item_news);
                        bindingView.viewpager.setCurrentItem(3);

                        break;
                    case R.id.item_my://我的
                        if (getActivity() != null && getActivity().getWindow() != null) {
                            StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.theme_color), 38);
                        }
                        //刷新个人信息
                        myFragment.requestData1();
                        item.setIcon(R.drawable.item_my);
                        bindingView.viewpager.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });

    }

    private void resetToDefaultIcon() {
        MenuItem home = bindingView.byeBurger.getMenu().findItem(R.id.item_home);
        home.setIcon(R.drawable.item_home1);
        MenuItem market = bindingView.byeBurger.getMenu().findItem(R.id.item_market);
        market.setIcon(R.drawable.item_market1);
        MenuItem shopcar = bindingView.byeBurger.getMenu().findItem(R.id.item_shopcar);
        shopcar.setIcon(R.drawable.item_shopcar1);
        MenuItem news = bindingView.byeBurger.getMenu().findItem(R.id.item_news);
        news.setIcon(R.drawable.item_news1);
        MenuItem my = bindingView.byeBurger.getMenu().findItem(R.id.item_my);
        my.setIcon(R.drawable.item_my1);
    }

    @SuppressLint("CheckResult")
    private void requestPermisson() {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储权限
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS
                )
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) { //拒绝
//                            SetPermissionDialog mSetPermissionDialog = new SetPermissionDialog(MainActivity.this);
//                            mSetPermissionDialog.show();
//                            mSetPermissionDialog.setConfirmCancelListener(new SetPermissionDialog.OnConfirmCancelClickListener() {
//                                @Override
//                                public void onLeftClick() {
//                                    finish();
//                                }
//
//                                @Override
//                                public void onRightClick() {
//                                    finish();
//                                }
//                            });
                            RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_HOME_PAGE, 0));
                            DialogWithYesOrNoUtils.getInstance().showDialog(getActivity(), null, "您拒绝了某些权限，部分功能将不可用！", new DialogWithYesOrNoUtils.DialogCallBack() {
                                @Override
                                public void exectEvent(DialogInterface dialog) {
                                    dialog.dismiss();
                                }

                                @Override
                                public void exectCancel(DialogInterface dialog) {
                                    dialog.dismiss();
                                }
                            });
                        } else { //勾选回调
                            //通知homepage更新数据
                            RxBus.getInstanceBus().post(new RxBusMessage(RxCodeConstants.REFRESH_HOME_PAGE, 0));
                        }
                    }
                });
    }

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    /**
     * 第一种解决办法 通过监听keyUp
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                ToastUtil.showShortToast("再按一次退出万益源");
                firstTime = secondTime;
                return true;
            } else {
                exit();
            }
        }

        return super.onKeyUp(keyCode, event);
    }

    private void exit() {
        finish();
        ActivityManage.appExit();
    }

    /**
     * 注册RxBus
     */
    private void initRxBus() {
        mRxBus = RxBus.getInstanceBus();
        mRxBus.registerRxBus(mRxBus, RxBusMessage.class, rxBusMessage -> {
            //根据事件类型进行处理
            //购买完成页面点击跳转 批发区 和 零售区页面
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_HOME_TAB) {
                //切刀首页
                resetToDefaultIcon();//重置到默认不选中图片
                bindingView.byeBurger.setSelectedItemId(bindingView.byeBurger.getMenu().getItem(0).getItemId());
                bindingView.viewpager.setCurrentItem(0);

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
