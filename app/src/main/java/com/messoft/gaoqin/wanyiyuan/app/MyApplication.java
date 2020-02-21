package com.messoft.gaoqin.wanyiyuan.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.http.HttpUtils;
import com.messoft.gaoqin.wanyiyuan.ui.MainActivity;
import com.messoft.gaoqin.wanyiyuan.utils.DebugUtil;
import com.messoft.gaoqin.wanyiyuan.utils.GlobalHandler;
import com.messoft.gaoqin.wanyiyuan.view.refreshHeader.ClassicsHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MyApplication extends MultiDexApplication {
    private static List<Activity> mActivitys =
            Collections.synchronizedList(new LinkedList<Activity>());


    //以下属性应用于整个应用程序，合理利用资源，减少资源浪费
    private static Context mContext;//上下文
    private static Application sApplication;
    private static Thread mMainThread;//主线程
    private static long mMainThreadId;//主线程id
    private static Looper mMainLooper;//循环队列
    private static GlobalHandler mHandler;//主线程Handler

    public static IWXAPI mWxapi;

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
//                layout.setPrimaryColorsId(R.color.pull_refresh_bg, R.color.default_text);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //对全局属性赋值
        sApplication = this;
        mContext = getApplicationContext();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
        mHandler = GlobalHandler.getInstance();

        //初始化网络请求
        HttpUtils.getInstance().init(this, DebugUtil.DEBUG);

        //注册管理activity
        registerActivityListener();

        //initBugly
        initBugly();

        //注册微信
        registToWX();
    }

    public static Context getContext() {
        return mContext;
    }

    public static GlobalHandler getMainHandler() {
        return mHandler;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    private void registerActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                /**
                 *  监听到 Activity创建事件 将该 Activity 加入list
                 */
                ActivityManage.pushActivity(activity);

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (null == mActivitys && mActivitys.isEmpty()) {
                    return;
                }
                if (mActivitys.contains(activity)) {
                    /**
                     *  监听到 Activity销毁事件 将该Activity 从list中移除
                     */
                    ActivityManage.popActivity(activity);
                }
            }
        });
    }

    /**
     * 向微信注册app
     */
    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxapi = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APP_ID, false);
        // 将该app注册到微信
        mWxapi.registerApp(Constants.WEIXIN_APP_ID);
    }

    /**
     * 初始化bugly(更新)
     * 参数1：上下文对象
     * 参数2：注册时申请的APPID
     * 参数3：是否开启debug模式，true表示打开debug模式，false表示关闭调试模式
     */
    private void initBugly() {
        /**** Beta高级设置*****/
        /**
         * true表示app启动自动初始化升级模块；
         * false不好自动初始化
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false
         * 在后面某个时刻手动调用
         */
        Beta.autoInit = true;

        /**
         * true表示初始化时自动检查升级
         * false表示不会自动检查升级，需要手动调用Beta.checkUpgrade()方法
         */
        Beta.autoCheckUpgrade = true;

        /**
         * 设置升级周期为60s（默认检查周期为0s），60s内SDK不重复向后天请求策略
         */
        Beta.initDelay = 1 * 1000;

        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源；
         */
        Beta.largeIconId = R.mipmap.logo;

        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源id;
         */
        Beta.smallIconId = R.mipmap.logo;


        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        Beta.defaultBannerId = R.mipmap.ic_launcher;

        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = false;

        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity.class);
//
////        setStrictMode();
//        // 设置是否开启热更新能力，默认为true
//        Beta.enableHotfix = false;
//        // 设置是否自动下载补丁
//        Beta.canAutoDownloadPatch = true;
//        // 设置是否提示用户重启
//        Beta.canNotifyUserRestart = true;
//        // 设置是否自动合成补丁
//        Beta.canAutoPatch = true;
//
        long start = System.currentTimeMillis();
        Bugly.init(getApplicationContext(), "e6a282ebe6", DebugUtil.DEBUG);
        long end = System.currentTimeMillis();
        DebugUtil.error("bugly init time--->", end - start + "ms");
    }
}
