package com.messoft.gaoqin.wanyiyuan.ui.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.MyFragmentPagerAdapterTwo;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.ProductType;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityHxqBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.ProductModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 生活必备
 */
public class ShbbActivity extends BaseActivity<ActivityHxqBinding> {

    private ProductModel mProductModel;
    private List<ProductType> mTabList;
    private MyFragmentPagerAdapterTwo mTagAdapter;
//    private int mType = -1; //0-跳转首页唯一个批发区 1-跳转首页随意一个零售区

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
        mProductModel = new ProductModel();
//        if (getIntent() != null) {
//            mType = getIntent().getIntExtra("type", -1);
//        }
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
        mProductModel.getAreaList1(getActivity(), BusinessUtils.getBindAccountId(),"shq", new RequestImpl() {
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
//            if (!getChannelList.getCode().equals("jfsc")) {
//                mTitleList.add(getChannelList.getDescription());
//                mFragments.add(ProductListFragment.newInstance(getChannelList.getId()));
//            }
            mTitleList.add(getChannelList.getDescription());
            mFragments.add(ProductListFragment.newInstance(getChannelList.getId(),0));
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


    }


//    public static void goPage(Context context, int type) {
//        Intent intent = new Intent(context, ShbbActivity.class);
//        intent.putExtra("type", type);
//        context.startActivity(intent);
//    }
}
