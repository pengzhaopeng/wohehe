package com.messoft.gaoqin.wanyiyuan.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.messoft.gaoqin.wanyiyuan.R;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.UIUtils;


/**
 * 通用设置组件
 */
public class CommonSettingWidget extends RelativeLayout {

    private Context mContext;
    private String mTvLeftStr;
    private String mTvRightStr;
    private String mTvRightHit;
    private int mTvRightColor;
    private boolean mHasImg;
    private int mLfetImg;
    private TextView mTvRight;
    private boolean mHasUnderLine;//是否有下划线
    private int mLeftTextSize;
    private TextView mTvLeft;

    public CommonSettingWidget(Context context) {
        this(context, null);
    }

    public CommonSettingWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonSettingWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommonSettingWidget);
        mTvLeftStr = ta.getString(R.styleable.CommonSettingWidget_common_setting_widget_title_left);
        mLeftTextSize = ta.getInteger(R.styleable.CommonSettingWidget_common_setting_widget_title_left_textSize, 14);
        mTvRightStr = ta.getString(R.styleable.CommonSettingWidget_common_setting_widget_title_right);
        mTvRightHit = ta.getString(R.styleable.CommonSettingWidget_common_setting_widget_title_right_hit);
        mTvRightColor = ta.getColor(R.styleable.CommonSettingWidget_common_setting_widget_title_right_color, UIUtils.getColor(R.color.title1));
        mHasImg = ta.getBoolean(R.styleable.CommonSettingWidget_common_setting_widget_has_img, true);
        mHasUnderLine = ta.getBoolean(R.styleable.CommonSettingWidget_common_setting_widget_has_underLine, false);
        if (mHasImg) {
            mLfetImg = ta.getResourceId(R.styleable.CommonSettingWidget_common_setting_widget_title_img, R.drawable.code);
        }

        ta.recycle();
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.common_settting_widget, this, true);
        ImageView img = findViewById(R.id.iv);
        mTvLeft = findViewById(R.id.tv_left);
        mTvLeft.setTextSize(mLeftTextSize);
        mTvRight = findViewById(R.id.tv_right);
        View underline = findViewById(R.id.underLine);

        if (mHasImg) {
            img.setVisibility(VISIBLE);
            img.setImageResource(mLfetImg);
        }
        if (!StringUtils.isNoEmpty(mTvRightStr)) {
            mTvRight.setHint(null);
        } else {
            mTvRight.setHint(mTvRightHit);
        }
        mTvLeft.setText(mTvLeftStr);
        mTvRight.setText(mTvRightStr);
        mTvRight.setTextColor(mTvRightColor);

        if (!mHasUnderLine) {
            underline.setVisibility(GONE);
        }
    }

    public void setTvLeftStr(String str) {
        mTvLeft.setText(str);
    }

    public TextView getTvRight() {
        return mTvRight;
    }

    public void setTvRight(String str) {
        mTvRight.setText(str);
    }

    public String getTvRightStr() {
        return mTvRight.getText().toString().trim();
    }

    public void setTvRightColor(int color) {
        mTvRight.setTextColor(color);
    }

}
