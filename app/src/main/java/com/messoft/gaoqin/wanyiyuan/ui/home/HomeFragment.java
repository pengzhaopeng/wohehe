package com.messoft.gaoqin.wanyiyuan.ui.home;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.MyFragmentPagerAdapterTwo;
import com.messoft.gaoqin.wanyiyuan.base.BaseFragment;
import com.messoft.gaoqin.wanyiyuan.bean.HomeBanner;
import com.messoft.gaoqin.wanyiyuan.bean.ProductType;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.FragmentHomeBinding;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.ProductModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DebugUtil;
import com.messoft.gaoqin.wanyiyuan.utils.GlideImageLoader;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.webview.WebActivity;
import com.youth.banner.Transformer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Glide图片缓存 https://blog.csdn.net/m0_37698386/article/details/82660811
 * TimeUtils.date2TimeStamp(data.getUpdateTime())
 */
public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    // 初始化完成后加载数据
    private boolean mIsPrepared = false;
    // 第一次显示时加载数据，第二次不显示
    private boolean mIsFirst = true;

    private ProductModel mProductModel;

    private ArrayList<String> bannerList; //轮播图片
    private ArrayList<String> bannerListLink;//轮播图的链接
    private List<HomeBanner> bannerListData;//轮播图的链接
    private MyFragmentPagerAdapterTwo mTagAdapter;

    private RxBus mRxBus;
    private String mCurrentColor = "#FF5C1A"; //当前颜色
    private List<ProductType> mTabList; //tab

    public void changeCurrentColor() {
        if (getBaseActivity() != null && getBaseActivity().getWindow() != null) {
            StatusBarUtil.setColor(getBaseActivity(), Color.parseColor(mCurrentColor), 0);
        }
    }

    @Override
    protected int setContent() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initSetting() {
        showContentView();
        initRxBus();
        bannerListLink = new ArrayList<>();
        mProductModel = new ProductModel();
        bannerListData = new ArrayList<>();
        mIsPrepared = true;
        loadData();
    }

    @Override
    protected void initListener() {
        bindingView.refreshLayout.setOnRefreshListener(refreshlayout -> refreshlayout.getLayout().postDelayed(() -> {
            requestData();
            refreshlayout.finishRefresh();
        }, 500));
        //搜索
        bindingView.rlHead.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SearchActivity.goPage(getActivity(), 0);
            }
        });
    }

    @Override
    protected void loadData() {
        // 显示时轮播图滚动
        if (bindingView != null && bindingView.banner != null) {
            bindingView.banner.startAutoPlay();
            bindingView.banner.setDelayTime(5000);
        }
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }
        synchronized (this) {
            bindingView.refreshLayout.postDelayed(this::requestData, 500);
        }
    }

    @SuppressLint("CheckResult")
    private void requestData() {
        //banner
        loadBanner();
        loadProductTag();
    }

    /**
     * 轮播图 data={projectType:"project_type_car",type:1,code:"car_index_0"}
     */
    private void loadBanner() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("projectType", "project_type_wyy");
            jsonObject.put("type", 1);
            jsonObject.put("codes", "wyy_index_0,wyy_index_1,wyy_index_2,wyy_index_3");

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getJavaCommonServer().getImageList(jsonObject.toString())
                .compose(RxSchedulers.compose())
                .compose(this.bindToLifecycle())
                .subscribe(new BaseObserver<List<HomeBanner>>(getContext(), true, "加载中...") {
                    @Override
                    protected void onSuccess(List<HomeBanner> login) {
                        bannerListData.clear();
                        bannerListData = login;
                        mIsFirst = false;
                        bannerListLink.clear();
                        if (login != null && login.size() > 0) {
                            if (bannerList == null) {
                                bannerList = new ArrayList<>();
                            } else {
                                bannerList.clear();
                            }
                            String updateTime = login.get(0).getUpdateTime();
                            for (HomeBanner datum : login) {
                                bannerList.add(SysUtils.getImgURL(datum.getImgName()));
                                bannerListLink.add(datum.getLink());
                            }

                            bindingView.banner.setBannerAnimation(Transformer.Default);
                            bindingView.banner.setOnBannerClickListener(position -> {
                                // 链接没有做缓存，如果轮播图使用的缓存则点击图片无效
                                position = position - 1;
                                if (bannerListLink != null && bannerListLink.size() > 0 &&
                                        StringUtils.isNoEmpty(bannerListLink.get(position))) {
                                    WebActivity.loadUrl(getActivity(), bannerListLink.get(position));
                                }
                            });
                            bindingView.banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int i, float v, int i1) {
//                                    ToastUtil.showShortToast("位置："+i);
                                    DebugUtil.error("onPageScrollStateChanged", "位置：" + i);
                                    //这里设置背景色
                                    if (i >= 0 && bannerListData != null && bannerListData.size() > 0
                                            && bannerListData.get(i) != null) {
                                        HomeBanner homeBanner = bannerListData.get(i);
                                        String title = homeBanner.getTitle();
                                        mCurrentColor = title.split("_")[1];
                                        DebugUtil.error("onPageScrollStateChanged", "颜色值：" + mCurrentColor);
                                        bindingView.rlHead.setBackgroundColor(Color.parseColor(mCurrentColor));
                                        bindingView.av.setBgColor(mCurrentColor);

                                        if (getBaseActivity() != null && getBaseActivity().getWindow() != null) {
                                            StatusBarUtil.setColor(getBaseActivity(), Color.parseColor(mCurrentColor), 0);
                                        }
//                                        StatusBarUtil.setColor(getActivity(), Color.parseColor(color));
                                    }
                                }

                                @Override
                                public void onPageSelected(int i) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int i) {
//                                    ToastUtil.showShortToast("位置："+i);
//                                    DebugUtil.error("onPageScrollStateChanged","位置："+i);
//                                    //这里设置背景色
//                                    if (i >= 0 && bannerListData != null && bannerListData.size() > 0
//                                            && bannerListData.get(i) != null) {
//                                        HomeBanner homeBanner = bannerListData.get(i);
//                                        String title = homeBanner.getTitle();
//                                        String color = title.split("_")[1];
//                                        DebugUtil.error("onPageScrollStateChanged","颜色值："+color);
//                                        bindingView.rlHead.setBackgroundColor(Color.parseColor(color));
//                                        bindingView.av.setBgColor(color);
//
//                                        StatusBarUtil.setColor(getActivity(), Color.parseColor(color),38);
////                                        StatusBarUtil.setColor(getActivity(), Color.parseColor(color));
//                                    }
                                }
                            });
                            //放最后
                            bindingView.banner.setImages(bannerList).setImageLoader(new GlideImageLoader(TimeUtils.date2TimeStamp(updateTime))).start();
                        }
                    }

                    @Override
                    protected void onFailure(int errorCode, String msg) {

                    }
                });
    }

    /**
     * 商品分类
     */
    private void loadProductTag() {
        mProductModel.getAreaList(getBaseActivity(), BusinessUtils.getBindAccountId(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                mTabList = (List<ProductType>) object;
                if (mTabList != null && mTabList.size() > 0) {
                    setProduct(mTabList);
                }

            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    /**
     * 设置商品分类
     *
     * @param data
     */
    private void setProduct(List<ProductType> data) {
        int size = data.size();
        ArrayList<Fragment> mFragments = new ArrayList<>(size);
        ArrayList<String> mTitleList = new ArrayList<>(size);

        mTitleList.clear();
        mFragments.clear();
        for (ProductType getChannelList : data) {
            mTitleList.add(getChannelList.getDescription());
            mFragments.add(ProductListFragment.newInstance(getChannelList.getId(),-1));
        }
        if (mTagAdapter == null) {
            mTagAdapter = new MyFragmentPagerAdapterTwo(getChildFragmentManager(), mFragments, mTitleList);
        } else {
            mTagAdapter.setFragments(getChildFragmentManager(), mFragments, mTitleList);
        }
        bindingView.viewpager.setAdapter(mTagAdapter);
        // 左右预加载页面的个数
        bindingView.viewpager.setOffscreenPageLimit(size - 1);
        mTagAdapter.notifyDataSetChanged();
        bindingView.tabLinli.setTabMode(TabLayout.MODE_SCROLLABLE);
        bindingView.tabLinli.setupWithViewPager(bindingView.viewpager);

    }


    @Override
    public void onResume() {
        super.onResume();
        // 失去焦点，否则RecyclerView第一个item会回到顶部
//        bindingView.xrvEveryday.setFocusable(false);
        DebugUtil.error("-----EverydayFragment----onResume()");
        // 开始图片请求
        Glide.with(getActivity()).resumeRequests();
    }

    @Override
    protected void onInvisible() {
        // 不可见时轮播图停止滚动
        if (bindingView != null && bindingView.banner != null) {
            bindingView.banner.stopAutoPlay();
        }
    }

    /**
     * 注册RxBus
     */
    private void initRxBus() {
        mRxBus = RxBus.getInstanceBus();
        mRxBus.registerRxBus(mRxBus, RxBusMessage.class, rxBusMessage -> {
            //根据事件类型进行处理
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_HOME_PAGE) {
                if (rxBusMessage.getI() == 0) {
                    //更新数据
                    requestData();
                }
            }
            //购买完成页面点击跳转 批发区 和 零售区页面
            if (null != rxBusMessage && rxBusMessage.getType() == RxCodeConstants.REFRESH_HOME_TAB) {
                if (rxBusMessage.getI() == 0) { //跳转首页唯一个批发区
                    //对应的tab 分类id
                    String msg = rxBusMessage.getMsg();
                    for (ProductType productType : mTabList) {
                        if (productType.getCode().equals(msg)) {
                            int index = mTabList.indexOf(productType);
                            if (index >= 0 && index < mTabList.size()) {
                                //切换到对应分类
                                bindingView.viewpager.setCurrentItem(index, true);
                                break;
                            }
                        }
                    }
                }
                if (rxBusMessage.getI() == 1) { //跳转首页随意一个零售区
                    //对应的tab 分类id
                    for (ProductType productType : mTabList) {
                        if (productType.getCode().contains("lsq")) {
                            int index = mTabList.indexOf(productType);
                            if (index >= 0 && index < mTabList.size()) {
                                //切换到对应分类
                                bindingView.viewpager.setCurrentItem(index, true);
                                break;
                            }
                        }
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
