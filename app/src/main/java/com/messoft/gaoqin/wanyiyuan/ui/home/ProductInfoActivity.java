package com.messoft.gaoqin.wanyiyuan.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.adapter.ProductCommentAdapter;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.BaseBean;
import com.messoft.gaoqin.wanyiyuan.bean.EvaluationList;
import com.messoft.gaoqin.wanyiyuan.bean.GetCartList;
import com.messoft.gaoqin.wanyiyuan.bean.ProductDetailById;
import com.messoft.gaoqin.wanyiyuan.bean.ProductInfo;
import com.messoft.gaoqin.wanyiyuan.bean.Sku;
import com.messoft.gaoqin.wanyiyuan.bean.SkuAndQuantity;
import com.messoft.gaoqin.wanyiyuan.bean.SkuAttribute;
import com.messoft.gaoqin.wanyiyuan.bean.SkuList;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityProductInfoBinding;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RequestImpl;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.model.ProductModel;
import com.messoft.gaoqin.wanyiyuan.model.ShopCarModel;
import com.messoft.gaoqin.wanyiyuan.utils.AddToCartAnimation;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DebugUtil;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.GlideImageLoader;
import com.messoft.gaoqin.wanyiyuan.utils.HtmlFormat;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.VerifyOrderImpl;
import com.messoft.gaoqin.wanyiyuan.utils.VerifyOrderUtils;
import com.messoft.gaoqin.wanyiyuan.utils.WebViewUtils;
import com.messoft.gaoqin.wanyiyuan.view.recyclerview.RecycleViewDivider;
import com.messoft.gaoqin.wanyiyuan.view.sku.view.ProductSkuDialog;
import com.messoft.gaoqin.wanyiyuan.view.viewbigimage.ViewBigImageActivity;
import com.messoft.gaoqin.wanyiyuan.view.webview.MyWebViewClient;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.youth.banner.Transformer;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import q.rorbin.badgeview.QBadgeView;

/**
 * 商品详情
 */
public class ProductInfoActivity extends BaseActivity<ActivityProductInfoBinding> {

    private int mType; //0-商品列表点进来 //1-我的商品点进来 不展示操作按钮 //2-生活必备，不加任何下单条件限制
    private ProductModel mModel;
    private ShopCarModel mShopCarModel;

    private String mId;
    private String mSkuId;
    // [{"key": "颜色","value": [{"attrValueId": 11,"attrValueName": "紫色"}]} , ... ]
//    private List<ProductSKuAttr.ProductAttrItemListMapBean> mProductAttrItemListMap;
    // [{"key": "42","value": "2,10"}...]
//    private List<ProductSKuAttr.ProductAttrSkuListMapBean> mProductAttrSkuListMap;

    private ArrayList<String> bannerList; //轮播图片
    private ProductCommentAdapter mCommentAdapter;

    private ProductSkuDialog dialog;

    private List<Sku> mSkuList; //所有SKU 属性集合

    private int mQuantity = 0; //选购数量
    private Sku mSelectSku = null;//选中的SKU
    private int popType = 0; //0-属性选择弹窗  1-立即购买弹窗
    private String mClassId;
    private String mPayType; //支付方式(业务) 0.任意,1.钱包 2.现金
    private String mUpdateTime;

    private QBadgeView mCarBadge;//购物车数量
    private int mCarTotalNum = 0; //购物车数量 每次成功加一次购物车数字就加1，按SKU数量算，单商品件数不给算
    private ImageView mLogoImageView; //加购物小人
    private int[] mStartLocation;//加购物车小人开始位置

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
    }

    @Override
    protected void initSetting() {
        showContentView();
        mModel = new ProductModel();
        mShopCarModel = new ShopCarModel();
        if (getIntent() != null) {
            mType = getIntent().getIntExtra("type", -1);
            mId = getIntent().getStringExtra("id");
        }
        if (mType == 1) {
            //我的商品点进来 不展示操作按钮
            bindingView.rlBottom.setVisibility(View.GONE);
            bindingView.rlSelectSku.setVisibility(View.GONE);
            bindingView.tab.setVisibility(View.GONE);
            bindingView.tvMyProductTitle.setVisibility(View.VISIBLE);
        }

        //动态设置banner宽高
        LinearLayout.LayoutParams bannerParams = (LinearLayout.LayoutParams) bindingView.banner.getLayoutParams();
        bannerParams.width = DensityUtils.getScreenWidth(getActivity());
        bannerParams.height = DensityUtils.getScreenWidth(getActivity());
        bindingView.banner.setLayoutParams(bannerParams);

        bannerList = new ArrayList<>();
        // 显示时轮播图滚动
        if (bindingView != null && bindingView.banner != null) {
            bindingView.banner.startAutoPlay();
            bindingView.banner.setDelayTime(4000);
        }

        //购物车数量
//        initCarNum();

        //加入购物车的静态动画
//        initAddCarAnim();

        //顶部tab
        initTab();

        //底部评价
        initComment();

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
        bindingView.goHome.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
        bindingView.goShare.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                ToastUtil.showShortToast("开发中...");
            }
        });
        //切换tab
        bindingView.tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tag = (int) tab.getTag();
                switch (tag) {
                    case 0:
                        ToastUtil.showShortToast("商品");
                        break;
                    case 1:
                        ToastUtil.showShortToast("详情");
                        break;
                    case 2:
                        ToastUtil.showShortToast("评价");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        bindingView.wvContent.setWebViewClient(new MyWebViewClient() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onPageFinished(WebView view, String url) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageFinished(view, url);
                addImageClickListener(view);//待网页加载完全后设置图片点击的监听方法
                view.loadUrl(String.format(Locale.CHINA, "javascript:document.body.style.paddingTop='%fpx'; void 0",
                        DensityUtil.px2dp(bindingView.wvContent.getPaddingTop())));

            }

            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            private void addImageClickListener(WebView webView) {
                webView.loadUrl("javascript:(function(){" +
                        "var objs = document.getElementsByTagName(\"img\"); " +
                        "for(var i=0;i<objs.length;i++)  " +
                        "{"
                        + "    objs[i].onclick=function()  " +
                        "    {  "
                        + "        window.imagelistener.openImage(this.src);  " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                        "    }  " +
                        "}" +
                        "})()");
            }
        });

        //属性选择
        bindingView.rlSelectSku.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                popType = 0;
                showSkuDialog(0);
            }
        });

        //假如购物车
        bindingView.addShopcar.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (mSelectSku != null) {
                    //直接加入
                    addShopCar(mSelectSku, mQuantity);
                } else {
                    //弹框选择
                    showSkuDialog(2);
                }
            }
        });

        //立即购买
        bindingView.goOrder.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                popType = 1;
                if (mSelectSku != null) {
                    goOrder();
                } else {
                    showSkuDialog(0);
                }
            }
        });
        bindingView.goOrder1.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                popType = 1;
                if (mSelectSku != null) {
                    goOrder();
                } else {
                    showSkuDialog(0);
                }
            }
        });
    }

    private void initAddCarAnim() {
        // 获取SKU面板Logo拷贝
        mLogoImageView = new ImageView(getActivity());
        bindingView.ivAddCartAnim.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(bindingView.ivAddCartAnim.getDrawingCache());
        mLogoImageView.setImageBitmap(bitmap);
        bindingView.ivAddCartAnim.setDrawingCacheEnabled(false);

        mStartLocation = new int[2];
        bindingView.ivAddCartAnim.getLocationOnScreen(mStartLocation);
    }

    /**
     * 购物车数量
     */
    private void initCarNum() {
        mCarBadge = new QBadgeView(getActivity());
        mCarBadge.bindTarget(bindingView.badgeNumber)
                .stroke(getResources().getColor(R.color.red), 1, true)
                .setBadgeGravity(Gravity.END | Gravity.TOP)
                .setGravityOffset(0, 0, true)
                .setBadgeTextSize(8, true);
        mCarBadge.setVisibility(View.GONE);

        //获取购物车数量
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("memberId", BusinessUtils.getBindAccountId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getCartList(StringUtils.toURLEncoderUTF8(jsonObject.toString()))
                .compose(RxSchedulers.compose())
                .compose(getActivity().bindToLifecycle())
                .subscribe(new Observer<BaseBean<List<GetCartList>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseBean<List<GetCartList>> listBaseBean) {
                        if (listBaseBean == null) return;
                        mCarTotalNum = listBaseBean.getTotal();
                        if (mCarTotalNum > 0) {
                            if (mCarBadge.getVisibility() != View.VISIBLE) {
                                mCarBadge.setVisibility(View.VISIBLE);
                            }
                            mCarBadge.setBadgeNumber(mCarTotalNum);
                        } else {
                            if (mCarBadge.getVisibility() != View.GONE) {
                                mCarBadge.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 初始化tab
     */
    private void initTab() {
        List<String> tabList = new ArrayList<>();
        tabList.add("商品");
        tabList.add("详情");
        tabList.add("评价");
        for (int i = 0; i < tabList.size(); i++) {
            bindingView.tab.addTab(bindingView.tab.newTab().setTag(i).setText(tabList.get(i)));
        }
    }

    /**
     * 初始化底部评价
     */
    private void initComment() {
        mCommentAdapter = new ProductCommentAdapter(getActivity());
        initOnRefresh(getActivity(), bindingView.rcComment);
//        bindingView.rcAddress.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        bindingView.rcComment.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL,
                DensityUtils.dp2px(getActivity(), (float) 0.5),
                getResources().getColor(R.color.colorPageBg)));
        bindingView.rcComment.setAdapter(mCommentAdapter);
    }

    private void loadData() {
        // 商品标识查询
        getProductById();
        //根据条件获取商品Sku属性 作废
//        getProductSKuAttr();
        //根据 productId 来查询商品评价
        getProductComment();
        //根据商品id 查出该商品对应的所有SUK
        getSkuList();
    }

    /**
     * 根据商品id 查出该商品对应的所有SUK
     */
    private void getSkuList() {
        mModel.getSkuList(getActivity(), mId, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                List<SkuList> data = (List<SkuList>) object;
                //整理成需要的 SKU 列表 赋值给弹出窗
                setSkuData(data);
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {

            }
        });
    }

    /**
     * 整理成需要的 SKU 列表 赋值给弹出窗
     *
     * @param data
     */
    private void setSkuData(List<SkuList> data) {
        if (data == null || data.size() <= 0) return;
        mSkuList = new ArrayList<>();
        mClassId = data.get(0).getClassId();
        for (SkuList datum : data) {
            //"property": "颜色:白色|规格:全网通256G",
            String property = datum.getProperty();
            String[] split = property.split("\\|");
            List<SkuAttribute> skuAttributes = new ArrayList<>();
            for (String aSplit : split) {
                skuAttributes.add(new SkuAttribute(aSplit.split(":")[0], aSplit.split(":")[1]));
            }

            Sku sku = new Sku(
                    datum.getId(),
                    datum.getImgAttrValueId(),
                    datum.getQty(),
                    datum.getMarketPrice(),
                    datum.getPrice(),
                    datum.getProductId(),
                    datum.getProductName(),
                    datum.getClassCode(),
                    datum.getClassId(),
                    datum.getUnitValue(),
                    mPayType,
                    datum.getPoints(),
                    skuAttributes);
            mSkuList.add(sku);
        }
        //取第一个SKU 为默认值 去设置默认图 选完SKU记得修改Banner

        String mainImage = mSkuList.get(0).getMainImage();
        setBanner(mainImage);
    }

    /**
     * 弹窗选择SKU
     * 0-正常操作  2-点击购物车弹出
     */
    private void showSkuDialog(int typeFlag) {
        if (mSkuList == null || mSkuList.size() <= 0) return;

        if (dialog == null) {
            dialog = new ProductSkuDialog(this);
            dialog.setData(mSkuList, mUpdateTime, new ProductSkuDialog.Callback() {
                @Override
                public void onAdded(int type, Sku sku, int quantity) {
                    mSelectSku = sku;
                    mQuantity = quantity;

                    bindingView.tvName.setText(sku.getProductName());
//                    bindingView.tvPrice.setText(sku.getSellingPrice() + "");
                    BusinessUtils.setPriceAndPoints(bindingView.tvPrice, 13, sku.getPoints(), sku.getSellingPrice());
                    bindingView.tvPriceMarket.setText("市场价" + sku.getOriginPrice());
                    bindingView.tvPriceMarket.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    //设置轮播图
                    setBanner(sku.getMainImage());
                    //设置显示属性
                    List<SkuAttribute> attributes = sku.getAttributes();
                    StringBuilder sb = new StringBuilder();
                    for (SkuAttribute attribute : attributes) {
                        sb.append(attribute.getValue());
                        sb.append("/");
                    }
                    bindingView.tvProperty.setText(sb.toString() + quantity + sku.getUnitValue());

                    if (typeFlag == 2) { //点击购物车按钮弹出框后选择的操作，就是直接加入购物车

                        addShopCar(sku, quantity);

                    } else {
                        if (popType == 1) {
                            //回调完后直接购买
                            goOrder();
                        }
                    }


                }
            });
        }
        dialog.show();
    }

    /**
     * 加入购物车
     *
     * @param sku
     * @param quantity
     */
    private void addShopCar(Sku sku, int quantity) {

        // 获取SKU面板Logo拷贝
        mLogoImageView = new ImageView(getActivity());
        bindingView.ivAddCartAnim.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(bindingView.ivAddCartAnim.getDrawingCache());
        mLogoImageView.setImageBitmap(bitmap);
        bindingView.ivAddCartAnim.setDrawingCacheEnabled(false);

        mStartLocation = new int[2];
        bindingView.ivAddCartAnim.getLocationOnScreen(mStartLocation);

        //TODO companyId默认给个0
        mShopCarModel.addCartForm(getActivity(),
                BusinessUtils.getBindAccountId(),
                "0",
                sku.getClassId(),
                sku.getProductId(),
                sku.getId(),
                String.valueOf(sku.getSellingPrice()),
                quantity, new RequestImpl() {
                    @Override
                    public void loadSuccess(Object object) {
                        //成功加入购物车
                        // 执行动画
                        startAddToCartAnimation(mLogoImageView, mStartLocation);
                    }

                    @Override
                    public void loadFailed(int errorCode, String errorMessage) {
                        ToastUtil.showShortToast(errorMessage);
                    }
                });
    }

    /**
     * ********************************添加到购物车动画***********************************
     */
    private ViewGroup mAnimationMaskLayout;      // 动画浮层

    private ViewGroup buildAddToCartAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private View addViewToCartAnimLayout(View view, int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    private void startAddToCartAnimation(final View v, int[] startLocation) {
        mAnimationMaskLayout = buildAddToCartAnimLayout();
        mAnimationMaskLayout.addView(v);
        final View view = addViewToCartAnimLayout(v, startLocation);
        int[] bottomCartLocation = new int[2];                    // 这是用来存储动画结束位置的X、Y坐标
        bindingView.ivShoppingCart.getLocationInWindow(bottomCartLocation);

        AddToCartAnimation animation = new AddToCartAnimation(startLocation, bottomCartLocation);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
                mAnimationMaskLayout.removeView(v);

                //显示数字
                if (mCarBadge.getVisibility() != View.VISIBLE) {
                    mCarBadge.setVisibility(View.VISIBLE);
                }
                //TODO 这里加入购物车每个 SKU 算一件，不管数量
                mCarTotalNum += 1;
                mCarBadge.setBadgeNumber(mCarTotalNum);
            }
        });
    }

    /**
     * 商品标识查询
     */
    private void getProductById() {
        mModel.getProductById(getActivity(), mId, new RequestImpl() {
            @SuppressLint("SetTextI18n")
            @Override
            public void loadSuccess(Object object) {
                ProductInfo data = (ProductInfo) object;
                if (data != null) {
                    mUpdateTime = data.getUpdateTime();
                    mPayType = data.getPayType();
                    mClassId = data.getClassId();
                    mSkuId = data.getSkuId();
                    //设置部分详情
                    bindingView.tvName.setText(data.getName());

                    BusinessUtils.setPriceAndPoints(bindingView.tvPrice, 13, data.getPoints(), Double.parseDouble(data.getPrice()));

                    bindingView.tvPriceMarket.setText("市场价" + data.getMarketPrice());
                    bindingView.tvPriceMarket.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    bindingView.tvBuys.setText("已售" + data.getBuys() + "件");
                    //根据商品详细信息标识  productDetailId 来查询商品详细信息
                    getProductByProductDetailID(data.getProductDetailId());

                    //判断是否为积分商品
                    if (data.getClassCode().equals("jfsc")) {
                        bindingView.tvPriceMarket.setVisibility(View.GONE);

                        bindingView.goOrder1.setText("立即兑换");
                        bindingView.goOrder.setText("立即兑换");
                    } else {
                        bindingView.tvPriceMarket.setVisibility(View.VISIBLE);

                        bindingView.goOrder1.setText("立即购买");
                        bindingView.goOrder.setText("立即购买");
                    }
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    /**
     * 根据商品详细信息标识  productDetailId 来查询商品详细信息
     *
     * @param productDetailId
     */
    private void getProductByProductDetailID(String productDetailId) {
        mModel.getProductDetailById(getActivity(), productDetailId, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
                ProductDetailById data = (ProductDetailById) object;
                if (data != null) {
                    //详情
                    WebViewUtils.initWeb(getActivity(), bindingView.wvContent);
                    //网页内容
                    if (StringUtils.isNoEmpty(data.getDetailDesc())) {
                        String msg = data.getDetailDesc();
                        if (!data.getDetailDesc().startsWith("<p>")) { //加 <p> 标签
                            msg = "<p>"+data.getDetailDesc()+"</p>";
                        }
                        bindingView.wvContent.loadData(HtmlFormat.getNewContent(msg), "text/html; charset=UTF-8", null); // 加载定义的代码，并设定编码格式和字符集。
                    }
                }
            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });
    }

    /**
     * 根据条件获取商品Sku属性
     */
    private void getProductSKuAttr() {
        mModel.getProductSKuAttr(getActivity(), mId, new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
//                ProductSKuAttr sKuAttrList = (ProductSKuAttr) object;
//                mProductAttrItemListMap = sKuAttrList.getProductAttrItemListMap();
//                mProductAttrSkuListMap = sKuAttrList.getProductAttrSkuListMap();
//                //难道SKU属性 弹窗加购物车购买
//                initPayPop();
//
//                // mProductAttrSkuListMap：[{"key": "42","value": "2,10"}...] key是skuid,根据key获取第一个数值，就是默认的轮播图片*6
//                if (mSkuId != null && mProductAttrSkuListMap != null && mProductAttrSkuListMap.size() > 0) {
//                    for (ProductSKuAttr.ProductAttrSkuListMapBean bean : mProductAttrSkuListMap) {
//                        if (bean.getKey().equals(mSkuId)) {
//                            String value = bean.getValue();
//                            String id = value.split(",")[0];
//                            //http://mstest-image.mesandbox.com/wyy/img/pimg/22/2/pa1_200_200.jpg  sku
//                            setBanner(id);
//                        }
//                    }
//                }

            }

            @Override
            public void loadFailed(int errorCode, String errorMessage) {
                ToastUtil.showShortToast(errorMessage);
            }
        });

    }

    /**
     * 商品评价
     */
    private void getProductComment() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "1"); //评价类型(1:商品评价类别,2:店铺评价类别)
            jsonObject.put("productId", mId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpClient.Builder.getWYYServer().getEvaluationList(StringUtils.toURLEncoderUTF8(jsonObject.toString()), 0, 10)
                .compose(RxSchedulers.compose())
                .compose(this.bindToLifecycle())
                .subscribe(new Observer<BaseBean<List<EvaluationList>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseBean<List<EvaluationList>> listBaseBean) {
                        if (listBaseBean == null) return;
                        int total = listBaseBean.getTotal();
                        if (total <= 0) {
                            bindingView.rlMoreComment.setVisibility(View.GONE);
                        } else {
                            bindingView.rlMoreComment.setVisibility(View.VISIBLE);
                            bindingView.tvCommentTitle.setText("用户评价" + "(" + total + ")");
                        }
                        List<EvaluationList> data = listBaseBean.getData();
                        mCommentAdapter.clear();
                        mCommentAdapter.addAll(data);
                        mCommentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 设置轮播图
     * http://mstest-image.mesandbox.com/wyy/img/pimg/22/2/pa1_200_200.jpg  sku
     *
     * @param id
     */
    private void setBanner(String id) {
        if (bannerList == null) return;
        bannerList.clear();
        for (int i = 1; i <= 6; i++) {
            String url = Constants.MASTER_URL + "wyy/img/pimg/" + mId + "/" + id + "/pa" + i + "_600_600.jpg";
            bannerList.add(url);
        }
        bindingView.banner.setBannerAnimation(Transformer.Default);
        bindingView.banner.setOnBannerClickListener(position -> {
            // 链接没有做缓存，如果轮播图使用的缓存则点击图片无效
            position = position - 1;
            //点击放大
            Bundle bundle = new Bundle();
            bundle.putInt("selet", 2);// 2,大图显示当前页数，1,头像，不显示页数
            bundle.putInt("code", position);//第几张
            bundle.putStringArrayList("imageuri", bannerList);
            Intent intent = new Intent(getActivity(), ViewBigImageActivity.class);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        });
        //放最后
        bindingView.banner.setImages(bannerList).setImageLoader(new GlideImageLoader(TimeUtils.date2TimeStamp(mUpdateTime))).start();
    }

    /**
     * 最后提交到订单页面
     */

    private void goOrder() {
        if (mSelectSku == null) {
            ToastUtil.showShortToast("未选择商品");
            return;
        }
        if (!StringUtils.isNoEmpty(mClassId)) {
            ToastUtil.showShortToast("classId为空");
            return;
        }
        //生活必备不加下单条件判断
        if (mType==2) {
            SkuAndQuantity skuAndQuantity = new SkuAndQuantity(mSelectSku, mQuantity);
            List<SkuAndQuantity> data = new ArrayList<>();
            data.add(skuAndQuantity);
            OrderActivity.goPage(getActivity(), 9, data);
        }else{
            VerifyOrderUtils.verifyOrder(getActivity(),
                    mul(mSelectSku.getSellingPrice(), mQuantity),
                    mSelectSku.getClassCode(),
                    mSelectSku.getId(),
                    mSelectSku.getClassId(), new VerifyOrderImpl() {
                        @Override
                        public void verifyResult(boolean reuslt) {
                            if (reuslt) {
                                SkuAndQuantity skuAndQuantity = new SkuAndQuantity(mSelectSku, mQuantity);
                                List<SkuAndQuantity> data = new ArrayList<>();
                                data.add(skuAndQuantity);
                                OrderActivity.goPage(getActivity(), 0, data);
                            }
                        }

                        @Override
                        public void loadFailed(int errorCode, String errorMessage) {
                            ToastUtil.showShortToast(errorMessage);
                        }
                    });
        }
//        SkuAndQuantity skuAndQuantity = new SkuAndQuantity(mSelectSku, mQuantity);
//        List<SkuAndQuantity> data = new ArrayList<>();
//        data.add(skuAndQuantity);
//        OrderActivity.goPage(getActivity(), 0, data);
    }

    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
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


    public static void goPage(Context context, String id, int type) {
        Intent intent = new Intent(context, ProductInfoActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
}
