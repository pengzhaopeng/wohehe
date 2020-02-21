package com.messoft.gaoqin.wanyiyuan.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.messoft.gaoqin.wanyiyuan.app.Constants;
import com.messoft.gaoqin.wanyiyuan.app.MyApplication;
import com.messoft.gaoqin.wanyiyuan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/19 0019.
 */

public class SysUtils {

    /**
     * 高勤IM图片地址加域名
     */
    public static String getImgURL(String url) {
        return Constants.MASTER_URL + url;
    }

    /**
     * 无参数跳转
     *
     * @param activity
     * @param cla
     */
    public static <T> void startActivity(Activity activity, Class<T> cla) {
        Intent intent = new Intent(activity, cla);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_in_right, R.anim.activity_out);
    }

    /**
     * 带参数跳转
     *
     * @param activity
     * @param cla
     * @param b        注意，接收Bundle的key为“b”
     */
    public static <T> void startActivity(Activity activity, Class<T> cla, Bundle b) {
        Intent intent = new Intent(activity, cla);
        if (b != null) {
            intent.putExtra("b", b);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_in_right, R.anim.activity_out);
    }

    /**
     * 跳转拨打电话界面
     */
    public static void goTelPage(Activity activity, String mobile) {
        if (!StringUtils.isNoEmpty(mobile)) {
            ToastUtil.showShortToast("号码为空");
            return;
        }
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
        activity.startActivity(dialIntent);
    }

    public static float getDimens(int resId) {
        return getResoure().getDimension(resId);
    }

    public static Resources getResoure() {
        return MyApplication.getContext().getResources();
    }

    public static void playTips(Context c) {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(c.getApplicationContext(), alert);
        r.play();
    }

    /**
     * 获取当前本地apk的版本
     *
     * @param
     * @return
     */
    public static int getVersionCode() {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = MyApplication.getContext().getPackageManager().
                    getPackageInfo(MyApplication.getContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @return
     */
    public static String getVerName() {
        String verName = "";
        try {
            verName = MyApplication.getContext().getPackageManager().
                    getPackageInfo(MyApplication.getContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 判断用户是否打开系统定位服务
     *
     * @return boolean
     */
    public static boolean isLocationEnabled() {
        LocationManager mLocationManager = (LocationManager) MyApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName
     *            ：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 判断某个Activity 界面是否在前台
     * @param context
     * @param className 某个界面名称
     * @return
     */
    public static boolean  isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;

    }

    /**
     * 获取手机IMEI号
     *
     * 需要动态权限: android.permission.READ_PHONE_STATE
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
        return imei;
    }


}
