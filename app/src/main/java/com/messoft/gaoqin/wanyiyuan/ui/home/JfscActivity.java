package com.messoft.gaoqin.wanyiyuan.ui.home;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.JfProductListAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.HomeBanner;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.bean.ProductInfo;
import com.messoft.gaoqin.wanyiyuan.bean.ProductType;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityJfscBinding;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.model.LoginModel;
import com.messoft.gaoqin.wanyiyuan.model.ProductModel;
import com.messoft.gaoqin.wanyiyuan.ui.my.JfDetailActivity;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DebugUtil;
import com.messoft.gaoqin.wanyiyuan.utils.GlideImageLoader;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.webview.WebActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Transformer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 积分商城
 */
public class JfscActivity extends BaseActivity<ActivityJfscBinding> {

    private ProductModel mProductModel;
    private LoginModel mLoginModel;
    private JfProductListAdapter mAdapter;

    private String mId; //积分商城id
    private int mPage = HttpUtils.start_page_java;
    private ArrayList<String> bannerListLink;//轮播图的链接
    private ArrayList<String> bannerList; //轮播图片

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jfsc);
    }

    @Override
    protected void initSetting() {
        showContentView();
        bannerListLink = new ArrayList<>();
        mProductModel = new ProductModel();
        mLoginModel = new LoginModel();

        mAdapter = new JfProductListAdapter(getActivity());
        initOnRefresh(getActivity(), bindingView.rc);
        bindingView.rc.setAdapter(mAdapter);
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
//        bindingView.refreshLayout.setEnableRefresh(false);
        bindingView.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPage = HttpUtils.start_page_java;
                //积分
                requestPersonInfo();
                loadProductList();
                refreshlayout.finishRefresh();
            }
        });
        bindingView.refreshLayout.setEnableLoadmore(false);
        //搜索
        bindingView.rlHead.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SearchActivity.goPage(getActivity(), 0);
            }
        });
        //积分明细
        bindingView.ll2.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                JfDetailActivity.goPage(getActivity(),0);
            }
        });
        bindingView.tvMore.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SysUtils.startActivity(getActivity(),AllJfProductListActivity.class);
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener<ProductInfo>() {
            @Override
            public void onClick(ProductInfo productInfo, int position) {
                ProductInfoActivity.goPage(getActivity(), productInfo.getId(), 0);
            }
        });
    }

    private void loadData() {
        //轮播图
        loadBanner();
        //积分
        requestPersonInfo();
        //商品列表
        loadProductTag();

    }

    /**
     * 个人信息
     */
    public void requestPersonInfo() {
        mLoginModel.getLoginMemberInfo(getActivity(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                LoginMemberInfo bean = (LoginMemberInfo) object;
                if (bean != null) {
                    bindingView.tvPoints.setText(String.valueOf(bean.getShoppingPoints()));
                }

            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    /**
     * 轮播图 data={projectType:"project_type_car",type:1,code:"car_index_0"}
     */
    private void loadBanner() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("projectType", "project_type_wyy");
            jsonObject.put("type", 1);
            jsonObject.put("codes", "wyy_jifen_0,wyy_jifen_1,wyy_jifen_2,wyy_jifen_3,");

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getJavaCommonServer().getImageList(jsonObject.toString())
                .compose(RxSchedulers.compose())
                .compose(this.bindToLifecycle())
                .subscribe(new BaseObserver<List<HomeBanner>>(getActivity(), true, "加载中...") {
                    @Override
                    protected void onSuccess(List<HomeBanner> login) {
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
        mProductModel.getAreaList(getActivity(), BusinessUtils.getBindAccountId(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                List<ProductType> mTabList = (List<ProductType>) object;
                if (mTabList != null && mTabList.size() > 0) {
//                    setProduct(mTabList);
                    setProduct1(mTabList);
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    private void setProduct1(List<ProductType> data) {
        mId = null;
        for (ProductType getChannelList : data) {
            if (getChannelList.getCode().equals("jfsc")) {
                mId = getChannelList.getId();
                break;
            }
        }
        loadProductList();
    }

    private void loadProductList() {
        if (mId == null) return;
        mProductModel.getProductList(getActivity(), null, mId, "1", "0", null,
                mPage, HttpUtils.per_page_20, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        showContentView();
                        List<ProductInfo> data = (List<ProductInfo>) object;
                        if (mPage == HttpUtils.start_page_java) {
                            if (data.size() > 0) {
                                mAdapter.clear();
                                mAdapter.addAll(data);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.clear();
                                mAdapter.notifyDataSetChanged();
//                                showNoData("暂无商品");
                            }
                        } else {
                            mAdapter.addAll(data);
                            mAdapter.notifyDataSetChanged();
                            bindingView.refreshLayout.finishLoadmore();
                        }
                    }

                    @Override
                    public void loadFailed(int errorCode, String errorMessage) {
                        ToastUtil.showShortToast(errorMessage);
//                        showError();
                    }
                });
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
    protected void onStop() {
        super.onStop();
        // 不可见时轮播图停止滚动
        if (bindingView != null && bindingView.banner != null) {
            bindingView.banner.stopAutoPlay();
        }
    }

}
