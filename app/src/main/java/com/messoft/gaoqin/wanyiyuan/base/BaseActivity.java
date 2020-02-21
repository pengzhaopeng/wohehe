package com.messoft.gaoqin.wanyiyuan.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.messoft.gaoqin.wanyiyuan.app.ActivityManage;
import com.messoft.gaoqin.wanyiyuan.utils.KeyBoardUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityBaseBinding;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


/**
 * @作者 Administrator
 * BaseActivity
 * @创建日期 2017/10/12 0012 17:41
 * http://blog.csdn.net/hijson/article/details/54377662
 */

public abstract class BaseActivity<SV extends ViewDataBinding> extends RxAppCompatActivity {
    private static final String TAG = "BaseActivity";
//    private SkinInflaterFactory mSkinInflaterFactory;

    //布局view
    protected SV bindingView;
    private LinearLayout llProgressbar;
    private View refresh; //加载失败，重新加载
    private TextView tvError;//加载失败或者 加载数为空的提示
    private ActivityBaseBinding mBaseBinding;
    private Animation mRotate;
    private LinearInterpolator interpolator;
    private ImageView mImg;

    //获取view
    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //设置背景色透明
    @SuppressLint("ResourceAsColor")
    public void setBackgroudTranslucent(){
        bindingView.getRoot().setBackgroundColor(R.color.transparent);
        bindingView.getRoot().setVisibility(View.GONE);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        mBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_base, null, false);
        bindingView = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);

        //content
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        RelativeLayout mContainer = mBaseBinding.getRoot().findViewById(R.id.container);
        View root = bindingView.getRoot();
        mContainer.addView(root);
        getWindow().setContentView(mBaseBinding.getRoot());

        llProgressbar = getView(R.id.ll_progress_bar);
        refresh = getView(R.id.ll_error_refresh);
        tvError = getView(R.id.tv_error);
        mImg = getView(R.id.img_progress);

        //加载动画
        mRotate = AnimationUtils.loadAnimation(this, R.anim.common_progress_cirle);
        //设置匀速旋转，在xml文件中设置会出现卡顿
        interpolator = new LinearInterpolator();
        mRotate.setInterpolator(interpolator);
        //默认进入页面就开启动画
        if (mImg != null && mRotate != null) {
            mImg.startAnimation(mRotate);
        }

        //设置toolbar
        if (hasToolBar()) {
            setToolBar();
        } else {
            mBaseBinding.appBar.setVisibility(View.GONE);
        }

        //点击加载失败布局
        refresh.setOnClickListener(new PerfectClickListener() {
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

    public BaseActivity getActivity() {
        return this;
    }

    public View getContentView(){
        return bindingView.getRoot();
    }

    protected abstract void initSetting();

    protected abstract void initListener();


    /**
     * 设置titleBar
     */
    protected void setToolBar() {
        setSupportActionBar(mBaseBinding.toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }
        //手动设置才有效果（子类确定要不要右边的菜单栏）
        mBaseBinding.toolBar.setTitleTextAppearance(this, R.style.ToolBar_Title);
        mBaseBinding.toolBar.setSubtitleTextAppearance(this, R.style.Toolbar_SubTitle);
        //返回键
        mBaseBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    protected View getToolBar() {
        return mBaseBinding.toolBar;
    }

    /**
     * 设置标题栏
     *
     * @param text text
     */
    public void setTitle(CharSequence text) {
        mBaseBinding.tvTitle.setText(text);
    }

    protected TextView getTvTitle() {
        return mBaseBinding.tvTitle;
    }

    /**
     * 获取右边textView
     */
    protected TextView getRightTv() {
        return mBaseBinding.tvRight;
    }

    /**
     * 设置toolbar右边文字
     *
     * @param text text
     */
    protected void setRightText(CharSequence text) {
        mBaseBinding.tvRight.setText(text);
    }

    /**
     * 设置toolbar右边文字图片
     *
     */
    protected void setRightImg(int imgId) {
        Drawable img = getResources().getDrawable(imgId);
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        mBaseBinding.tvRight.setCompoundDrawables(img, null, null, null); //设置左图标
    }

    /**
     * 设置title 右边图片
     *
     */
    protected void setTitleRightImg(int imgId) {
        Drawable img = getResources().getDrawable(imgId);
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        mBaseBinding.tvTitle.setCompoundDrawables(null, null, img, null); //设置右图标
    }

    /**
     * 子类是否需要继承父类的toolbar
     *
     */
    protected boolean hasToolBar() {
        return true;
    }


    protected void showLoading() {
        if (llProgressbar.getVisibility() != View.VISIBLE) {
            llProgressbar.setVisibility(View.VISIBLE);
        }
        //开始动画
        if (mImg != null && mRotate != null) {
            mImg.startAnimation(mRotate);
        }
        if (bindingView.getRoot().getVisibility() != View.GONE) {
            bindingView.getRoot().setVisibility(View.GONE);
        }
        if (refresh.getVisibility() != View.GONE) {
            refresh.setVisibility(View.GONE);
        }
    }

    protected void showContentView() {
        if (llProgressbar.getVisibility() != View.GONE) {
            llProgressbar.setVisibility(View.GONE);
        }
        //停止动画
        if (mImg != null && mRotate != null) {
            mImg.clearAnimation();
        }
        if (refresh.getVisibility() != View.GONE) {
            refresh.setVisibility(View.GONE);
        }
        if (bindingView.getRoot().getVisibility() != View.VISIBLE) {
            bindingView.getRoot().setVisibility(View.VISIBLE);
        }
    }

    protected void showError() {
        if (llProgressbar.getVisibility() != View.GONE) {
            llProgressbar.setVisibility(View.GONE);
        }
        // 停止动画
        if (mImg != null && mRotate != null) {
            mImg.clearAnimation();
        }
        if (refresh.getVisibility() != View.VISIBLE) {
            refresh.setVisibility(View.VISIBLE);
        }
        if (bindingView.getRoot().getVisibility() != View.GONE) {
            bindingView.getRoot().setVisibility(View.GONE);
        }
    }

    protected void showError(String msg) {
        if (llProgressbar.getVisibility() != View.GONE) {
            llProgressbar.setVisibility(View.GONE);
        }
        // 停止动画
        if (mImg != null && mRotate != null) {
            mImg.clearAnimation();
        }
        if (refresh.getVisibility() != View.VISIBLE) {
            refresh.setVisibility(View.VISIBLE);

            tvError.setText(msg);
        }
        if (bindingView.getRoot().getVisibility() != View.GONE) {
            bindingView.getRoot().setVisibility(View.GONE);
        }
    }

    protected void showNoData(String strTip) {
        if (llProgressbar.getVisibility() != View.GONE) {
            llProgressbar.setVisibility(View.GONE);
        }
        // 停止动画
        if (mImg != null && mRotate != null) {
            mImg.clearAnimation();
        }
        if (refresh.getVisibility() != View.VISIBLE) {
            refresh.setVisibility(View.VISIBLE);
            if (!StringUtils.isNoEmpty(strTip)) {
                strTip = "暂无数据";
            }
            tvError.setText(strTip);
        }
//        if (bindingView.getRoot().getVisibility() != View.GONE) {
//            bindingView.getRoot().setVisibility(View.GONE);
//        }
    }

    /**
     * 初始化下下拉头
     */
    public void initOnRefresh(Activity activity, RecyclerView xrcHome) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xrcHome.setLayoutManager(mLayoutManager);
        // 需加，不然滑动不流畅
        xrcHome.setNestedScrollingEnabled(false);
        xrcHome.setHasFixedSize(false);
    }

    /**
     * 失败后点击刷新
     */
    protected void onRefresh() {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManage.finishActivity(this);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isClickWb()) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    View view = getCurrentFocus();
                    KeyBoardUtil.hideKeyboard(ev, view, BaseActivity.this);//调用方法判断是否需要隐藏键盘
                    break;

                default:
                    break;
            }
            return super.dispatchTouchEvent(ev);
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    protected boolean isClickWb() {
        return true;
    }

}
