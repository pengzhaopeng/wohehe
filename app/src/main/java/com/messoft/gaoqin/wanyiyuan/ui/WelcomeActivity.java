package com.messoft.gaoqin.wanyiyuan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.app.ActivityManage;
import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.bean.GetNewsList;
import com.messoft.gaoqin.wanyiyuan.http.BaseObserver;
import com.messoft.gaoqin.wanyiyuan.http.HttpClient;
import com.messoft.gaoqin.wanyiyuan.http.RxSchedulers;
import com.messoft.gaoqin.wanyiyuan.ui.login.LoginActivity;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.HtmlFormat;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.SPUtils;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.UIUtils;
import com.messoft.gaoqin.wanyiyuan.utils.WebViewUtils;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;


public class WelcomeActivity extends AppCompatActivity {

    private boolean isIn = false;
    private boolean animationEnd;

    private ImageView ivPic;
    private ImageView ivDefultPic;
    private TextView tvJump;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (!BusinessUtils.getUserAgree()){
            //用户协议
            showAgree();
        }else{
            goPage();
        }

    }

    private void showAgree() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_user_agree)
                .setConvertListener((ViewConvertListener) (holder, dialog) -> {
                    //设置
                    WebView web = holder.getView(R.id.webView);
                    WebViewUtils.initWeb(WelcomeActivity.this, web);
                    web.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                            view.loadUrl(String.format(Locale.CHINA, "javascript:document.body.style.paddingTop='%fpx'; void 0", DensityUtil.px2dp(web.getPaddingTop())));
                        }
                    });
                    //加载数据
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("searchId", "s_privacy");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    HttpClient.Builder.getJavaCommonServer().getNewsList(jsonObject.toString())
                            .compose(RxSchedulers.compose())
//                            .compose(MainActivity.this.bindToLifecycle())
                            .subscribe(new BaseObserver<List<GetNewsList>>(WelcomeActivity.this, false, "加载中...") {
                                @Override
                                protected void onSuccess(List<GetNewsList> login) {
                                    List<GetNewsList> data = login;
                                    if (data != null && data.size() > 0) {
                                        GetNewsList getNewsList = data.get(0);
//                                        if (StringUtils.isNoEmpty(getNewsList.getTitle())){
//                                            bindingView.tvTitle.setText(getNewsList.getTitle());
//                                        }
                                        if (StringUtils.isNoEmpty(getNewsList.getContent())) {
                                            web.loadData(HtmlFormat.getNewContent(getNewsList.getContent()), "text/html; charset=UTF-8", null); // 加载定义的代码，并设定编码格式和字符集。
                                        }
                                    }

                                }

                                @Override
                                protected void onFailure(int errorCode, String msg) {
                                    ToastUtil.showShortToast(msg);
                                }
                            });
                    //取消
                    holder.setOnClickListener(R.id.btn_cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            ActivityManage.appExit();
                        }
                    });
                    //同意
                    holder.setOnClickListener(R.id.btn_agree, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BusinessUtils.setUserAgree(true);
                            dialog.dismiss();
                            goPage();
                        }
                    });

                })
                .setDimAmount(0.3f)
//                .setShowBottom(true)
                .setMargin(20)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    private void goPage() {
        //如果启动app的Intent中带有额外的参数，表明app是从点击通知栏的动作中启动的
        //将参数取出，传递到MainActivity中
        if(getIntent().getBundleExtra(Constants.EXTRA_BUNDLE) != null){
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            intent.putExtra(Constants.EXTRA_BUNDLE, getIntent().getBundleExtra(Constants.EXTRA_BUNDLE));
            startActivity(intent);
            //这里return的目的是为了代码再启动首页
            finish();
            return;
        }

        ivPic = findViewById(R.id.iv_pic);
        ivDefultPic = findViewById(R.id.iv_defult_pic);
        tvJump = findViewById(R.id.tv_jump);


        //先显示默认图
//        ivDefultPic.setImageDrawable(getResources().getDrawable(R.drawable.welcome_bg));


        tvJump.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                toMainActivity();
            }
        });


        UIUtils.getMainThreadHandler().postDelayed(this::toMainActivity, 1000);
    }

    private void toMainActivity() {
        if (isIn) {
            return;
        }
        //判断是否登陆，否则跳转登录页
        if (SPUtils.getBoolean("isLogin", false)) {
            goPage(MainActivity.class);
        } else {
            goPage(LoginActivity.class);
        }
    }

    /**
     * 跳转页面
     *
     * @param cla
     * @param <T>
     */
    private <T> void goPage(Class<T> cla) {
        startActivity(new Intent(WelcomeActivity.this, cla));
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        finish();
        isIn = true;
    }



    /**
     * 实现监听跳转
     */
    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            animationEnd();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private void animationEnd() {
        synchronized (WelcomeActivity.this) {
            if (!animationEnd) {
                animationEnd = true;
                ivPic.clearAnimation();
                toMainActivity();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManage.finishActivity(this);
    }
}
