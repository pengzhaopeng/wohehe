package com.messoft.gaoqin.wanyiyuan.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.ProductListAdapter;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.base.baseadapter.OnItemClickListener;
import com.messoft.gaoqin.wanyiyuan.bean.BaseBean;
import com.messoft.gaoqin.wanyiyuan.bean.ProductInfo;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivitySearchBinding;
import com.messoft.gaoqin.wanyiyuan.db.RecordsDao;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DebugUtil;
import com.messoft.gaoqin.wanyiyuan.utils.KeyBoardUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.UIUtils;
import com.messoft.gaoqin.wanyiyuan.view.WarpLinearLayout;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.GridSpacingItemDecoration;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends BaseActivity<ActivitySearchBinding> {


    private RecordsDao mRecordsDao;
    private List<String> mRecordsList;
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void initSetting() {
        showContentView();
        KeyBoardUtil.showSoftInputFromWindow(getActivity(), bindingView.etSearch);
        mRecordsDao = RecordsDao.getIntance(this);
        mRecordsList = mRecordsDao.getRecordsList();


        //init rc
        initOnRefresh(getActivity(), bindingView.rc);
        bindingView.rc.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //
        if (getIntent() != null) {
            //type 0-搜索商品
            mType = getIntent().getIntExtra("type", -1);
            switch (mType) {
                case 0:
                    //商品
                    bindingView.etSearch.setHint("请输入商品名称");
                    searchProducts();
                    break;
            }
        }

        initHistorySearch();

    }


    /**
     * 搜商品
     */
    @SuppressLint({"unchecked", "CheckResult"})
    private void searchProducts() {
        final ProductListAdapter mAllSqAdapter = new ProductListAdapter(getActivity());
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        bindingView.rc.setNestedScrollingEnabled(false);
        bindingView.rc.setHasFixedSize(false);
        bindingView.rc.setLayoutManager(mLayoutManager);
        bindingView.rc.addItemDecoration(new GridSpacingItemDecoration(
                2,
                UIUtils.dip2Px(10),
                true));
        bindingView.rc.setAdapter(mAllSqAdapter);
        //点击事件
        mAllSqAdapter.setOnItemClickListener(new OnItemClickListener<ProductInfo>() {
            @Override
            public void onClick(ProductInfo productInfo, int position) {
                //商品详情
                if (productInfo != null && productInfo.getId() != null) {
                    ProductInfoActivity.goPage(getActivity(), productInfo.getId(), 0);
                }
            }
        });
        //bservable.debounce:仅在过了一个指定的时间后还没有发射数据才进行数据的发射。
        RxTextView.textChanges(bindingView.etSearch)
                .map(new Function<CharSequence, CharSequence>() {
                    @Override
                    public CharSequence apply(CharSequence charSequence) {
                        if (charSequence.toString().length() <= 0) {
                            //隐藏rc
                            if (bindingView.rc.getVisibility() != View.GONE) {
                                bindingView.rc.setVisibility(View.GONE);
                            }
                        }
                        return charSequence;
                    }
                })
                .debounce(600, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(CharSequence charSequence) {
                        //过滤字符为空的情况
                        int length = charSequence.toString().length();
                        return length > 0;
                    }
                })
                .map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(CharSequence charSequence) {
                        if (charSequence != null && StringUtils.isNoEmpty(charSequence.toString())) {
                            return charSequence.toString().trim();
                        }
                        return null;
                    }
                })
                //flatmap:将所有请求排列起来，然后一个个发射
                //switchMap:只发射最近的一个请求
                .switchMap((Function<String, ObservableSource<BaseBean<List<ProductInfo>>>>) s -> {
//                    HttpDialogUtils.showDialog(getActivity(), false, "搜索中...");
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("isSale", 1);
                        jsonObject.put("isDel", 0);
                        jsonObject.put("name", s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return HttpClient.Builder.getWYYServer().getProductList(StringUtils.toURLEncoderUTF8(jsonObject.toString()),
                            HttpUtils.start_page_java, HttpUtils.per_page_20);//最多展示20条就好了
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseBean<List<ProductInfo>>>bindToLifecycle())
                .subscribe(new Consumer<BaseBean<List<ProductInfo>>>() {

                    @Override
                    public void accept(BaseBean<List<ProductInfo>> listBaseBean) {
//                        HttpDialogUtils.dismissDialog();
                        DebugUtil.debug("SearchActivity", listBaseBean.getData().toString());
                        if (listBaseBean.getData() != null && listBaseBean.getData().size() > 0) {
                            if (bindingView.rc.getVisibility() != View.VISIBLE) {
                                bindingView.rc.setVisibility(View.VISIBLE);
                            }
                            mAllSqAdapter.clear();
                            mAllSqAdapter.addAll(listBaseBean.getData());
                            mAllSqAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtil.showLongToast("没有搜索到此商品");
                            mAllSqAdapter.clear();
                            mAllSqAdapter.notifyDataSetChanged();
                            if (bindingView.rc.getVisibility() != View.GONE) {
                                bindingView.rc.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }


    @Override
    protected void initListener() {
        //返回
        bindingView.ivBack.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
        //搜索
        bindingView.tvSearch.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
//                ToastUtil.showLongToast("搜索");
                doSearch();
            }
        });
        //删除历史记录
        bindingView.ivDeleteHistory.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                doDeleteHistory();
            }
        });
    }


    /**
     * 删除历史记录
     */
    private void doDeleteHistory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("温馨提示");
        builder.setMessage("确定删除全部历史记录？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mRecordsDao.deleteAllRecords();
                bindingView.historySearch.removeAllViews();
                bindingView.rlHistory.setVisibility(View.GONE);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 搜索
     */
    private void doSearch() {
        String etSearch = bindingView.etSearch.getText().toString().trim();
        if (!StringUtils.isNoEmpty(etSearch)) {
            ToastUtil.showLongToast("搜索内容不能为空");
            return;
        }
        addSql(etSearch);
        SearchResultActivity.goPage(getActivity(), etSearch, mType);
    }

    /**
     * 将记录添加到数据库
     *
     * @param etSearch
     */
    private void addSql(String etSearch) {
        //判断数据库中是否有记录
        if (!mRecordsDao.isHasRecord(etSearch) && mRecordsList != null) {
            mRecordsList.add(etSearch);
            addOneSerchItemView(etSearch, bindingView.historySearch);
            mRecordsDao.addRecords(etSearch);
        }
    }

    /**
     * 历史记录
     */
    private void initHistorySearch() {
        if (mRecordsList == null || mRecordsList.size() == 0) {
            bindingView.rlHistory.setVisibility(View.GONE);
            return;
        }
        addSerchItemView(mRecordsList, bindingView.historySearch);
    }


    /**
     * 添加多个item
     *
     * @param hotSearchList
     * @param hotSearch
     */
    private void addSerchItemView(List<String> hotSearchList, WarpLinearLayout hotSearch) {
        if (hotSearch != null) {
            hotSearch.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            for (String title : hotSearchList) {
                TextView textView = new TextView(this);
                textView.setTextSize(14);
                textView.setBackgroundResource(R.drawable.search_item_bg);
                textView.setPadding(20, 10, 20, 10);
//                textView.setTextColor(textView.getResources().getColor(R.color.default_text));
                textView.setText(title);
                textView.setLayoutParams(params);
                hotSearch.addView(textView, 0);
//                textView.setTag();
                addItemClick(textView);
            }
        }
    }

    /**
     * 添加一个item到历史记录
     *
     * @param record
     * @param hotSearch
     */
    private void addOneSerchItemView(String record, WarpLinearLayout hotSearch) {
        if (bindingView.rlHistory.getVisibility() == View.GONE) {
            bindingView.rlHistory.setVisibility(View.VISIBLE);
        }
        if (hotSearch != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(this);
            textView.setTextSize(14);
            textView.setBackgroundResource(R.drawable.search_item_bg);
            textView.setPadding(20, 10, 20, 10);
//                textView.setTextColor(textView.getResources().getColor(R.color.default_text));
            textView.setText(record);
            textView.setLayoutParams(params);
            hotSearch.addView(textView, 0);
            addItemClick(textView);
        }
    }

    /**
     * 给item设置点击和长按事件
     *
     * @param textView
     */
    private void addItemClick(final TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = textView.getText().toString().trim();
                if (StringUtils.isNoEmpty(trim)) {
                    addSql(trim);
                    SearchResultActivity.goPage(getActivity(), trim, mType);
                }
            }
        });
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mRecordsList != null && mRecordsList.size() > 0) {
                    final String text = textView.getText().toString();
                    final int positon = getPositon(bindingView.historySearch, text);
//                    ToastUtil.showLongToast("嘿嘿" + positon);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("删除此条记录？");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (bindingView.historySearch != null && bindingView.historySearch.getChildCount() > positon
                                    && bindingView.historySearch.getChildAt(positon) != null) {

                                bindingView.historySearch.removeViewAt(positon);
                                mRecordsDao.deleteOneRecords(text);
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
            }
        });
    }

    /**
     * 遍历viewGroup
     */
    private int getPositon(ViewGroup viewGroup, CharSequence text) {
        if (viewGroup == null) {
            return -1;
        }
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            int position = i;
            View view = viewGroup.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (text.equals(textView.getText())) {
                    return position;
                }
            }
//            else if(view instanceof ViewGroup){
//                //不做处理
//            }
        }
        return -1;
    }

    public static void goPage(Context context, int type) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

}
