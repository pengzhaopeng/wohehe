package com.messoft.gaoqin.wanyiyuan.ui.market;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.DropDownAdapter;
import com.messoft.gaoqin.wanyiyuan.adapter.MyFragmentPagerAdapterTwo;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.BaseFragment;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.databinding.FragmentMarketBinding;
import com.messoft.gaoqin.wanyiyuan.utils.DebugUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.RegexUtil;
import com.messoft.gaoqin.wanyiyuan.utils.SoftKeyboardUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.view.CustomPopWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MarketFragment extends BaseFragment<FragmentMarketBinding> {

    private CustomPopWindow mListPopWindow;
    private View mDropdownPop;//下拉菜单
    private DropDownAdapter mDropDownAdapter;

    private View mMenuView;
    private PopupWindow mMenuPopWindow;
    private MenuItemView mMenuItemView;
    private MenuItemController mMenuController;
    GSFragment gsFragment;


    String phone = null;
    String beanAmountBegin = null;
    String beanAmountEnd = null;

    public BaseActivity getActivityOne() {
        return this.getBaseActivity();
    }

    @Override
    protected int setContent() {
        return R.layout.fragment_market;
    }

    @Override
    protected void initSetting() {
        showContentView();

        ArrayList<Fragment> mFragments = new ArrayList<>();
        ArrayList<String> mTitleList = new ArrayList<>();
        gsFragment = new GSFragment();
        mFragments.add(gsFragment);
        mTitleList.add("狗子");
        MyFragmentPagerAdapterTwo mTagAdapter = new MyFragmentPagerAdapterTwo(getChildFragmentManager(), mFragments, mTitleList);
        bindingView.viewpager.setAdapter(mTagAdapter);

        //init 下拉 pop
        initDownPop();

        //右上角菜单
        //加号
        mMenuView = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.drop_down_menu, null);
        mMenuPopWindow = new PopupWindow(mMenuView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);

        //菜单
        mMenuItemView = new MenuItemView(mMenuView);
        mMenuItemView.initModule();
        mMenuController = new MenuItemController(this);
        mMenuItemView.setListeners(mMenuController);
    }

    @Override
    protected void initListener() {
        bindingView.refreshLayout.setLoadmoreFinished(false);
        bindingView.refreshLayout.setOnRefreshListener(refreshlayout -> refreshlayout.getLayout().postDelayed(() -> {
            gsFragment.onRefresh();
            refreshlayout.finishRefresh();
        }, 500));
        //下拉选择菜单
        bindingView.rlRight.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                showDropListView();
            }
        });

        //下拉选择点击
        mDropDownAdapter.setOnItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onClick(String s, int position) {
                if (mListPopWindow != null) {
                    mListPopWindow.dissmiss();
                }
                bindingView.tvNumber.setText(s);
                switch (position) {
                    case 0:
                        beanAmountBegin = null;
                        beanAmountEnd = null;
                        break;
                    case 1:
                        beanAmountBegin = "0";
                        beanAmountEnd = "2000";
                        break;
                    case 2:
                        beanAmountBegin = "2000";
                        beanAmountEnd = "6000";
                        break;
                    case 3:
                        beanAmountBegin = "6000";
                        beanAmountEnd = "10000";
                        break;
                    case 4:
                        beanAmountBegin = "10000";
                        break;
                }
                gsFragment.setArgument(phone, beanAmountBegin, beanAmountEnd);
            }
        });

        bindingView.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//搜索按键action
                    //先隐藏键盘
                    SoftKeyboardUtils.closeInoutDecorView(Objects.requireNonNull(getBaseActivity()));
                    phone = bindingView.etSearch.getText().toString().trim();
                    if (TextUtils.isEmpty(phone)) {
                        ToastUtil.showShortToast("请输入账号");
                        return true;
                    }
                    if (!RegexUtil.checkMobile(phone)) {
                        ToastUtil.showShortToast("输入正确的手机号");
                        return true;
                    }
                    DebugUtil.debug("开始搜索");
                    gsFragment.setArgument(phone, beanAmountBegin, beanAmountEnd);
                    return true;
                }
                return false;
            }
        });


        RxTextView.textChangeEvents(bindingView.etSearch)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TextViewTextChangeEvent>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                        String money = bindingView.etSearch.getText().toString().trim();
                        if (money.equals("") && phone != null) { //回退为空情况
                            phone = null;
                            gsFragment.setArgument(phone, beanAmountBegin, beanAmountEnd);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        //右上角更多菜单
        bindingView.more.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                showRigitMoreWindow();
            }
        });
    }

    private void showDropListView() {
        if (mDropdownPop == null) {
            return;
        }
        if (mListPopWindow == null) {
            mListPopWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
//                    .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
//                    .setBgDarkAlpha(0.8f) // 控制亮度
                    .setView(mDropdownPop)
                    .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)//显示大小
                    .create();

        }
        mListPopWindow.showAsDropDown(bindingView.rlRight, 0, 10);

    }

    /**
     * 下拉pop
     */
    private void initDownPop() {
        mDropdownPop = LayoutInflater.from(getActivity()).inflate(R.layout.pop_list, null);
        RecyclerView recyclerView = (RecyclerView) mDropdownPop.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        mDropDownAdapter = new DropDownAdapter();
        recyclerView.setAdapter(mDropDownAdapter);

        mDropDownAdapter.addAll(mockData());
        mDropDownAdapter.notifyDataSetChanged();
    }

    private List<String> mockData() {
        List<String> data = new ArrayList<>();
        data.add("不限");
        data.add("2000以下");
        data.add("2000-6000");
        data.add("6000-10000");
        data.add("10000以上");
        return data;
    }

    /**
     * 右上角 + 号
     */
    public void showRigitMoreWindow() {
        mMenuPopWindow.setTouchable(true);
        mMenuPopWindow.setOutsideTouchable(true);
        mMenuPopWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        if (mMenuPopWindow.isShowing()) {
            mMenuPopWindow.dismiss();
        } else {
            mMenuPopWindow.showAsDropDown(getRootView().findViewById(R.id.more), -10, -5);
//            mMenuPopWindow.showAsDropDown(, -10, -5);
        }
    }

    public void dismissPopWindow() {
        //右上角
        if (mMenuPopWindow.isShowing()) {
            mMenuPopWindow.dismiss();
        }

        //选择器
        if (mListPopWindow != null) {
            mListPopWindow.dissmiss();
        }
    }


}
