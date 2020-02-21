package com.messoft.gaoqin.wanyiyuan.ui.my;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.base.BaseActivity;
import com.messoft.gaoqin.wanyiyuan.bean.LoginMemberInfo;
import com.messoft.gaoqin.wanyiyuan.databinding.ActivityYqhyBinding;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ClipUtils;
import com.messoft.gaoqin.wanyiyuan.utils.DensityUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;
import com.messoft.gaoqin.wanyiyuan.utils.PerfectClickListener;
import com.messoft.gaoqin.wanyiyuan.utils.ShareUtils;
import com.messoft.gaoqin.wanyiyuan.utils.SysUtils;
import com.messoft.gaoqin.wanyiyuan.utils.ToastUtil;
import com.messoft.gaoqin.wanyiyuan.utils.UIUtils;
import com.messoft.gaoqin.wanyiyuan.view.viewbigimage.ViewBigImageActivity;

/**
 * 邀请好友
 */
public class YqhyActivity extends BaseActivity<ActivityYqhyBinding> {

    private String mUrl;

    @Override
    protected boolean hasToolBar() {
        return false;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yqhy);


    }

    @SuppressLint("ResourceType")
    @Override
    protected void initSetting() {
        if (getActivity() != null && getActivity().getWindow() != null) {
            StatusBarUtil.setColor(getActivity(), Color.parseColor("#333145"), 0);
        }
        showContentView();
        LoginMemberInfo userData = BusinessUtils.getUserData();
        ImgLoadUtil.displayCircle(bindingView.ivHeader, SysUtils.getImgURL(userData.getImgName()));
        bindingView.tvName.setText(userData.getName());
        bindingView.tvPhone.setText(userData.getMobile());
        //二维码
        mUrl = "http://wyy-share.bybtb.com/join.html?&recommenderMobile=" + BusinessUtils.getMobile();
        QRCodeUtil.showThreadImage(getActivity(), mUrl, bindingView.ivCode, -1);
    }

    @Override
    protected void initListener() {
        bindingView.ivBack.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                onBackPressed();
            }
        });
        bindingView.tvShare.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                ShareUtils.shareDefault(getActivity(),
                        "邀请好友",
                        "快来加入我们吧",
                        mUrl,
                        R.mipmap.logo,
                        url -> ClipUtils.copyText(UIUtils.getContext(), url, "地址已复制到粘贴板"));
            }
        });
        bindingView.tvJt.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
//                ViewBigImageActivity.saveImageToGallery(getActivity(),
//                        DensityUtils.snapShotWithoutStatusBar(getActivity()));
//                ToastUtil.showLongToast("海报已保存至" + Environment.getExternalStorageDirectory().getAbsolutePath() + getResources().getString(R.string.app_name));

                try {
                    ViewBigImageActivity.saveImageToGallery(getActivity(),
                            DensityUtils.getBitmap(getActivity(),bindingView.rlShot));
                    ToastUtil.showLongToast("海报已保存至" + Environment.getExternalStorageDirectory().getAbsolutePath() + getResources().getString(R.string.app_name));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
