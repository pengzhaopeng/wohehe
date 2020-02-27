package com.messoft.gaoqin.wanyiyuan.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.MyFragmentPagerAdapterTwo;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.ProductType;
import com.messoft.gaoqin.wanyiyuan.bean.RxBusMessage;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityHxqBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxBus;
import com.messoft.gaoqin.wanyiyuan.http.rx.RxCodeConstants;
import com.messoft.gaoqin.wanyiyuan.model.ProductModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 互销区
 */
public class HxqActivity extends BaseActivity<ActivityHxqBinding> {

    private ProductModel mProductModel;
    private List<ProductType> mTabList;
    private MyFragmentPagerAdapterTwo mTagAdapter;
    private String mCode = null;
    private RxBus mRxBus;
    private int mType = -1; //0-跳转首页唯一个批发区 1-跳转首页随意一个零售区

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hxq);
    }

    @Override
    protected void initSetting() {
        showContentView();
        initRxBus();
        mProductModel = new ProductModel();
        if (getIntent() != null) {
            mCode = getIntent().getStringExtra("tabName");
            mType = getIntent().getIntExtra("type", -1);
        }
        loadProductTag();
    }

    @Override
    protected void initListener() {
        bindingView.ivBack.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });

        //搜索
        bindingView.rlHead.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SearchActivity.goPage(getActivity(), 0);
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
     * 将积分商城过滤
     *
     * @param data
     */
    private void setProduct(List<ProductType> data) {
//        int size = data.size();
        ArrayList<Fragment> mFragments = new ArrayList<>();
        ArrayList<String> mTitleList = new ArrayList<>();

        mTitleList.clear();
        mFragments.clear();
        for (ProductType getChannelList : data) {
            if (!getChannelList.getCode().equals("jfsc") && !getChannelList.getCode().contains("shq")) {
                mTitleList.add(getChannelList.getDescription());
                mFragments.add(ProductListFragment.newInstance(getChannelList.getId(),-1));
            }
        }
        if (mTagAdapter == null) {
            mTagAdapter = new MyFragmentPagerAdapterTwo(getSupportFragmentManager(), mFragments, mTitleList);
        } else {
            mTagAdapter.setFragments(getSupportFragmentManager(), mFragments, mTitleList);
        }
        bindingView.viewpager.setAdapter(mTagAdapter);
        // 左右预加载页面的个数
        bindingView.viewpager.setOffscreenPageLimit(mFragments.size() - 1);
        mTagAdapter.notifyDataSetChanged();
        bindingView.tabLinli.setTabMode(TabLayout.MODE_SCROLLABLE);
        bindingView.tabLinli.setupWithViewPager(bindingView.viewpager);

        //0-跳转首页唯一个批发区 1-跳转首页随意一个零售区
        switch (mType){
            case 0:
                for (ProductType productType : mTabList) {
                    if (productType.getCode().equals(mCode)) {
                        int index = mTabList.indexOf(productType);
                        if (index >= 0 && index < mTabList.size()) {
                            //切换到对应分类
                                bindingView.viewpager.setCurrentItem(index, true);
                            break;
                        }
                    }
                }
                break;
            case 1:
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

    public static void goPage(Context context, String tabName, int type) {
        Intent intent = new Intent(context, HxqActivity.class);
        intent.putExtra("tabName", tabName);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
}
