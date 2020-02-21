package com.messoft.gaoqin.wanyiyuan.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.utils.ImgLoadUtil;


/**
 * 通用开关组件
 */
public class CommonSwitchWidget extends RelativeLayout {

    private Context mContext;
    private String mTvLeftStr;
    private boolean mHasImg;
    private int mLfetImg;
    private TextView mTvRight;
    private boolean mIsOpen;
    private SwitchListener mSwitchListener;
    private SwitchCompat mSwitchCompat;

    public CommonSwitchWidget(Context context) {
        this(context, null);
    }

    public CommonSwitchWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonSwitchWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommonSwitchWidget);
        mTvLeftStr = ta.getString(R.styleable.CommonSwitchWidget_common_switch_widget_title_left);
        mHasImg = ta.getBoolean(R.styleable.CommonSwitchWidget_common_switch_widget_has_img, false);
        mIsOpen = ta.getBoolean(R.styleable.CommonSwitchWidget_common_switch_is_open, false);
        if (mHasImg) {
            mLfetImg = ta.getInt(R.styleable.CommonSettingWidget_common_setting_widget_title_img, R.drawable.code);
        }
        ta.recycle();
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.common_switch_widget, this, true);
        ImageView img = findViewById(R.id.iv);
        TextView tvLeft = findViewById(R.id.tv_left);
        mSwitchCompat = findViewById(R.id.switchCompat);
        mTvRight = findViewById(R.id.tv_right);

        if (mHasImg) {
            ImgLoadUtil.displayEspImage(mLfetImg, img);
        }
        tvLeft.setText(mTvLeftStr);
        //开关
        mSwitchCompat.setChecked(mIsOpen);

        mSwitchCompat.setOnCheckedChangeListener((compoundButton, b) -> {
            if (mSwitchListener != null) {
                mSwitchListener.openSwitch(b);
            }
        });
    }

    public void setSwitch(boolean isOpen){
        if (mSwitchCompat != null) {
            mSwitchCompat.setChecked(isOpen);
        }
    }

    public interface SwitchListener {
        void openSwitch(boolean isOpen);
    }

    public void setSwitchListener(SwitchListener switchListener) {
        mSwitchListener = switchListener;
    }

}
