package com.messoft.gaoqin.wanyiyuan.ui.shopcar;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.ShopCarAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseFragment;
import com.messoft.gaoqin.wanyiyuan.bean.GetCartList;
import com.messoft.gaoqin.wanyiyuan.bean.NormalBean;
import com.messoft.gaoqin.wanyiyuan.bean.ShopBean;
import com.messoft.gaoqin.wanyiyuan.databinding.FragmentShopcarBinding;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.model.ShopCarModel;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DebugUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.shopcar.RecyclerViewWithContextMenu;
import com.messoft.gaoqin.wanyiyuan.utils.shopcar.bean.CartItemBean;
import com.messoft.gaoqin.wanyiyuan.utils.shopcar.bean.ICartItem;
import com.messoft.gaoqin.wanyiyuan.utils.shopcar.listener.CartOnCheckChangeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ShopcarFragment extends BaseFragment<FragmentShopcarBinding> {

    // 初始化完成后加载数据
    private boolean mIsPrepared = false;
    // 第一次显示时加载数据，第二次不显示
    private boolean mIsFirst = true;
    private ShopCarModel mShopCarModel;

    ShopCarAdapter mAdapter;

    private boolean isEditing;//是否处于编辑状态
    private int totalCount;//购物车商品ChildItem的总数量，店铺条目不计算在内
    private int totalCheckedCount;//勾选的商品总数量，店铺条目不计算在内
    private double totalPrice;//勾选的商品总价格

    private List<ICartItem> mDataList;

    @Override

    protected int setContent() {
        return R.layout.fragment_shopcar;
    }

    @Override
    protected void initSetting() {
        showContentView();
        mShopCarModel = new ShopCarModel();
        mDataList = new ArrayList<>();

        bindingView.tvTitle.setText(getString(R.string.cart, 0));
        bindingView.btnGoToPay.setText(getString(R.string.go_settle_X, 0));
        bindingView.tvTotalPrice.setText(getString(R.string.rmb_X, 0.00));

        bindingView.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mAdapter = new ShopCarAdapter(getActivity(), mDataList);
//        mAdapter.setOnCheckChangeListener(new CartOnCheckChangeListener(bindingView.recycler, mAdapter) {
//            @Override
//            public void onCalculateChanged(ICartItem cartItemBean) {
//                calculate();
//            }
//        });
//        bindingView.recycler.setAdapter(mAdapter);

        // 给列表注册 ContextMenu 事件。
        // 同时如果想让ItemView响应长按弹出菜单，需要在item xml布局中设置 android:longClickable="true"
        registerForContextMenu(bindingView.recycler);

        mIsPrepared = true;
        loadData();
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

    @Override
    protected void initListener() {
        bindingView.refreshLayout.setLoadmoreFinished(false);
        bindingView.refreshLayout.setOnRefreshListener(refreshlayout -> refreshlayout.getLayout().postDelayed(() -> {
            requestData();
            refreshlayout.finishRefresh();
        }, 500));

        //编辑按钮事件
        bindingView.tvEdit.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                isEditing = !isEditing;
                bindingView.tvEdit.setText(getString(isEditing ? R.string.edit_done : R.string.edit));
                bindingView.btnGoToPay.setText(getString(isEditing ? R.string.delete_X : R.string.go_settle_X, totalCheckedCount));
            }
        });
        //提交订单 & 删除选中（编辑状态）
        bindingView.btnGoToPay.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                submitEvent();
            }
        });
        //全选
        bindingView.checkboxAll.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                mAdapter.checkedAll(((CheckBox) v).isChecked());
            }
        });
    }

    private void submitEvent() {
        if (isEditing) {
            if (totalCheckedCount == 0) {
                ToastUtil.showShortToast("请勾选你要删除的商品");
            } else {
                mAdapter.removeChecked();
            }

        } else {
            if (totalCheckedCount == 0) {
                ToastUtil.showShortToast("你还没有选择任何商品");
            } else {
                ToastUtil.showShortToast("你选择了" + totalCheckedCount + "件商品" +
                        "共计 " + totalPrice + "元");
            }
        }
    }

    public void requestData() {
        synchronized (this) {
            //购物车信息
            bindingView.refreshLayout.postDelayed(this::loadShopCarList, 500);
        }

    }

    private void loadShopCarList() {
        mShopCarModel.getCartList(getBaseActivity(), BusinessUtils.getBindAccountId(), new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                mIsFirst = false;
                List<GetCartList> data = (List<GetCartList>) object;
                if (data != null && data.size() > 0) {
                    hasData();
                    setData(data);
                } else {
                    noData();
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    /**
     * 数据初始化尤其重要
     * 1. childItem 数据全部在 GroupItem 数据的下方，数据顺序严格按照对应关系；
     * 2. GroupItem 下的 ChildItem 数据不能为空；
     * 3. 初始化时如果不需要，所有类型的条目都可以不设置ID，GroupItem也不用设置setChilds()；
     *
     * 列表操作时数据动态的变化设置：
     * 1. 通过 CartAdapter 的 addData、setNewData；
     * 2. 单个添加各个条目可以通过对应的 add 方法；
     * 3. 单独添加一个 GroupItem ,可以把它的 ChildItem 数据放到 setChilds 中。
     * @return
     */
    private void setData(List<GetCartList> data) {
        //清空一下
        mDataList.clear();
        NormalBean normalBean = new NormalBean();
        normalBean.setMarkdownNumber(6);
        mDataList.add(normalBean);
        //开始遍历数据
        Map<String, List<GetCartList>> map = new HashMap<>();
        for (GetCartList datum : data) {
            String className = datum.getClassName();
            if (map.containsKey(className)) { //有就追加
                List<GetCartList> getCartLists = map.get(className);
                getCartLists.add(datum);
                map.put(className, getCartLists);
            } else { //没有就重新添加
                List<GetCartList> getCartLists = new ArrayList<>();
                getCartLists.add(datum);
                map.put(className, getCartLists);
            }
        }
        //遍历数据
        for (Map.Entry<String, List<GetCartList>> item : map.entrySet()) {
            //父
            ShopBean shopBean = new ShopBean();
            shopBean.setShop_name(item.getKey());
            shopBean.setItemType(CartItemBean.TYPE_GROUP);
            mDataList.add(shopBean);
            //子
            List<GetCartList> value = item.getValue();
            for (GetCartList getCartList : value) {
                getCartList.setItemType(CartItemBean.TYPE_CHILD);
                mDataList.add(getCartList);
            }
        }
        DebugUtil.error("ShopcarFragment", "数据：" + mDataList);
        //设置数据
//        mAdapter.setNewData(mDataList);
//        mAdapter.notifyDataSetChanged();

        mAdapter = new ShopCarAdapter(getActivity(), mDataList);
        mAdapter.setOnCheckChangeListener(new CartOnCheckChangeListener(bindingView.recycler, mAdapter) {
            @Override
            public void onCalculateChanged(ICartItem cartItemBean) {
                calculate();
            }
        });
        bindingView.recycler.setAdapter(mAdapter);
    }

    private void hasData() {
        bindingView.refreshLayout.setVisibility(View.VISIBLE);
        bindingView.rlNoData.setVisibility(View.GONE);
    }

    private void noData() {
        bindingView.refreshLayout.setVisibility(View.GONE);
        bindingView.rlNoData.setVisibility(View.VISIBLE);
    }

    /**
     * 添加选项菜单
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.main_contextmenu, menu);
    }

    /**
     * 选项菜单点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //获取到的是 listView 里的条目信息
        RecyclerViewWithContextMenu.RecyclerViewContextInfo info = (RecyclerViewWithContextMenu.RecyclerViewContextInfo) item.getMenuInfo();
        Log.d("ContentMenu", "onCreateContextMenu position = " + (info != null ? info.getPosition() : "-1"));
        if (info != null && info.getPosition() != -1) {
            switch (item.getItemId()) {
                case R.id.action_remove:
                    mAdapter.removeChild(info.getPosition());
                    ToastUtil.showShortToast("成功移入收藏");
                    break;
                case R.id.action_findmore:
                    ToastUtil.showShortToast("查找与" + ((GetCartList) mAdapter.getData().get(info.getPosition())).getProductName() + "相似的产品");
                    break;
                case R.id.action_delete:
                    mAdapter.removeChild(info.getPosition());
                    break;
                default:
                    break;
            }
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给相关的 textView 进行数据填充
     */
    private void calculate() {
        totalCheckedCount = 0;
        totalCount = 0;
        totalPrice = 0.00;
        int notChildTotalCount = 0;
        if (mAdapter.getData() != null) {
            for (ICartItem iCartItem : mAdapter.getData()) {
                if (iCartItem.getItemType() == ICartItem.TYPE_CHILD) {
                    totalCount++;
                    if (iCartItem.isChecked()) {
                        totalCheckedCount++;
                        totalPrice += ((GetCartList) iCartItem).getSkuPrice() * ((GetCartList) iCartItem).getSkuQty();
                    }
                } else {
                    notChildTotalCount++;
                }
            }
        }

        bindingView.tvTitle.setText(getString(R.string.cart, totalCount));
        bindingView.btnGoToPay.setText(getString(isEditing ? R.string.delete_X : R.string.go_settle_X, totalCheckedCount));
        bindingView.tvTotalPrice.setText(getString(R.string.rmb_X, totalPrice));
        if (bindingView.checkboxAll.isChecked() && (totalCheckedCount == 0 || (totalCheckedCount + notChildTotalCount) != mAdapter.getData().size())) {
            bindingView.checkboxAll.setChecked(false);
        }
        if (totalCheckedCount != 0 && (!bindingView.checkboxAll.isChecked()) && (totalCheckedCount + notChildTotalCount) == mAdapter.getData().size()) {
            bindingView.checkboxAll.setChecked(true);
        }
    }
}
