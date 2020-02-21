package com.messoft.gaoqin.wanyiyuan.base;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.trello.rxlifecycle2.components.support.RxFragment;


/**
 * @作者 Administrator
 * BaseFragment
 * @创建日期 2017/10/13 0013 11:44
 */


public abstract class BaseFragment<SV extends ViewDataBinding> extends RxFragment {
    //布局view
    protected SV bindingView;
    //fragment是否显示了
    protected boolean mIsVisible = false;
    //加载中
    private LinearLayout mLlProgressBar;
    //加载失败
    private LinearLayout mRefresh;
    private TextView tvError;
    //加载旋转动画
    private ImageView mImg;
    //内容布局
    protected RelativeLayout mContainer;
    private Animation mRotate;
    private LinearInterpolator interpolator;

    private BaseActivity mActivity;

    public BaseFragment getFragment() {
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View ll = inflater.inflate(R.layout.fragment_base, null);
        bindingView = DataBindingUtil.inflate(getActivity().getLayoutInflater(), setContent(), null, false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        mContainer = ll.findViewById(R.id.container);
        //加到第0个元素 放到最下面
        mContainer.addView(bindingView.getRoot(), 0);
        return ll;
    }

    public View getRootView(){
        return bindingView.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    protected BaseActivity getBaseActivity() {
        return mActivity;
    }

    protected View getBaseActivityView(){
        return mActivity.getContentView();
    }

    /**
     * 实现fragment数据的懒加载
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    protected void onInvisible() {

    }

    protected void onVisible() {
        loadData();
    }

    /**
     * 注意声明 isPrepared，先初始化
     * 显示时加载数据 先setUserVisibleHint再执行onActivityCreated
     * 在 onActivityCreated 之后第一次显示加载数据，只加载一次
     */
    protected void loadData() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLlProgressBar = getView(R.id.ll_progress_bar);
        mImg = getView(R.id.img_progress);

        //加载动画
        mRotate = AnimationUtils.loadAnimation(getActivity(), R.anim.common_progress_cirle);
        //设置匀速旋转，在xml文件中设置会出现卡顿
        interpolator = new LinearInterpolator();
        mRotate.setInterpolator(interpolator);
        //默认进入页面就开启动画
        if (mImg != null && mRotate != null) {
            mImg.startAnimation(mRotate);
        }

        mRefresh = getView(R.id.ll_error_refresh);
        tvError = getView(R.id.tv_error);
        //点击加载失败布局
        mRefresh.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                showLoading();
                onRefresh();
            }
        });

        bindingView.getRoot().setVisibility(View.GONE);

        initSetting();
        initListener();
    }

    protected abstract void initSetting();

    protected abstract void initListener();

    /**
     * 加载失败后点击后的操作
     */
    protected void onRefresh() {

    }


    protected <T extends View> T getView(int id) {
        return (T) getView().findViewById(id);
    }

    /**
     * 布局
     */
    protected abstract int setContent();

    /**
     * 显示加载中的状态
     */
    protected void showLoading() {
        if (mLlProgressBar.getVisibility() != View.VISIBLE) {
            mLlProgressBar.setVisibility(View.VISIBLE);
        }
        //开始动画
        if (mImg != null && mRotate != null) {
            mImg.startAnimation(mRotate);
        }
        if (bindingView.getRoot().getVisibility() != View.GONE) {
            bindingView.getRoot().setVisibility(View.GONE);
        }
        if (mRefresh.getVisibility() != View.GONE) {
            mRefresh.setVisibility(View.GONE);
        }
    }

    /**
     * 加载完成后的状态
     */
    protected void showContentView() {
        if (mLlProgressBar.getVisibility() != View.GONE) {
            mLlProgressBar.setVisibility(View.GONE);
        }
        //停止动画
        if (mImg != null && mRotate != null) {
            mImg.clearAnimation();
        }
        if (mRefresh.getVisibility() != View.GONE) {
            mRefresh.setVisibility(View.GONE);
        }
        if (bindingView.getRoot().getVisibility() != View.VISIBLE) {
            bindingView.getRoot().setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载失败点击重新加载的状态
     */
    protected void showError() {
        if (mLlProgressBar.getVisibility() != View.GONE) {
            mLlProgressBar.setVisibility(View.GONE);
        }
        //停止动画
        if (mImg != null && mRotate != null) {
            mImg.clearAnimation();
        }
        if (mRefresh.getVisibility() != View.VISIBLE) {
            mRefresh.setVisibility(View.VISIBLE);
        }
//        if (bindingView.getRoot().getVisibility() != View.GONE) {
//            bindingView.getRoot().setVisibility(View.GONE);
//        }
    }

    /**
     * 加载失败点击重新加载的状态
     */
    protected void showNoData(String strTip) {
        if (mLlProgressBar.getVisibility() != View.GONE) {
            mLlProgressBar.setVisibility(View.GONE);
        }
        //停止动画
        if (mImg != null && mRotate != null) {
            mImg.clearAnimation();
        }
        if (mRefresh.getVisibility() != View.VISIBLE) {
            mRefresh.setVisibility(View.VISIBLE);
            tvError.setText(strTip);
        }
//        if (bindingView.getRoot().getVisibility() != View.GONE) {
//            bindingView.getRoot().setVisibility(View.GONE);
//        }
    }

    /**
     * 初始化下下拉头
     */
    protected void initOnRefresh(Activity activity, RecyclerView xrcHome) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xrcHome.setLayoutManager(mLayoutManager);
        // 需加，不然滑动不流畅
        xrcHome.setNestedScrollingEnabled(false);
        xrcHome.setHasFixedSize(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
