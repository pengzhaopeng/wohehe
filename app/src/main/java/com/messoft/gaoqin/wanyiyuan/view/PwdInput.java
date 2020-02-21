package com.messoft.gaoqin.wanyiyuan.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;


import com.messoft.gaoqin.wanyiyuan.R;

import java.util.ArrayList;

public class PwdInput extends View implements View.OnKeyListener {
    private static final String TAG = PwdInput.class.getSimpleName();
    private static final int DEFAULT_PWD_LENGTH = 6;
    private int mPwdLength;
    private int itemWidth;
    private int itemHeight;
    private Paint mPaint;
    private RectF mRectF;
    private InputMethodManager inputMethodManager;
    private ArrayList<String> mPasswords;
    private TextPaint mTextPaint;
    private ArrayList<String> mNeedHidden = new ArrayList<>();
    private OnInputFinished onInputFinished;
    private Paint circlePaint;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (msg.what == 100) {
                mNeedHidden.add((String) msg.obj);
                invalidate();
            }
        }
    };
    private float textSize;
    private int itemBorderColor;
    private int itemCircleColor;
    private Rect textRect;
    private float mRadius;
    private float roundRectRaidus;
    private float mStrokeWidth;

    public void setOnInputFinished(OnInputFinished onInputFinished) {
        this.onInputFinished = onInputFinished;
    }

    public PwdInput(Context context) {
        this(context, null);
    }

    public PwdInput(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PwdInput(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PwdInput);
        //密码长度
        mPwdLength = array.getInteger(R.styleable.PwdInput_pwd_length, DEFAULT_PWD_LENGTH);
        //输入框文字大小
        textSize = array.getDimension(R.styleable.PwdInput_pwd_textsize, dp2px(15));
        //密码框颜色
        itemBorderColor = array.getColor(R.styleable.PwdInput_pwd_item_border_color, Color.BLACK);
        //圆点颜色
        itemCircleColor = array.getColor(R.styleable.PwdInput_pwd_circle_color, Color.MAGENTA);
        //圆点半径
        mRadius = array.getDimension(R.styleable.PwdInput_pwd_circle_radius, dp2px(8));
        //密码框圆角半径
        roundRectRaidus = array.getDimension(R.styleable.PwdInput_pwd_round_rect_radius, dp2px(4));
        mStrokeWidth=array.getDimension(R.styleable.PwdInput_pwd_round_rect_border_width,dp2px(0.5f));
        array.recycle();
        init();
    }

    private void init() {
        //键盘输入监听
        setOnKeyListener(this);
        //获取焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        //键盘管理
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //密码框 paint
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(itemBorderColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        //设置阴影效果
        mPaint.setShadowLayer(3, 3, 3, Color.GRAY);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        //密码框rect
        mRectF = new RectF();
        //存放密码集合
        mPasswords = new ArrayList<>();
        //密码明文paint
        mTextPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(textSize);
        //密码暗文paint
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(itemCircleColor);
        //测量文字所需rect
        textRect = new Rect();
        //密码框宽高 默认宽高一样
        itemWidth = getMaxItemWidth();
        itemHeight = getMaxItemWidth();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure: " + getPaddingBottom());
        //整体宽度等于密码框宽度和密码框之间间隙总和再加上padding大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width, height;
        if (widthMode != MeasureSpec.EXACTLY) {//宽度未知
            //宽度未知时
            //密码框之间的间隙等于密码框大小的1/5
            width = itemWidth * mPwdLength + (mPwdLength - 1) * itemWidth / 5 + getPaddingLeft() + getPaddingRight();
            if (heightMode == MeasureSpec.EXACTLY) {//高度已知
                itemHeight = heightSize - getPaddingTop() - getPaddingBottom();
                height = heightSize;
            } else {//高度未知
                height = itemHeight + getPaddingTop() + getPaddingBottom();
            }
        } else {//宽度已知
            itemWidth = (widthSize - getPaddingLeft() - getPaddingRight()) * 5 / (mPwdLength * 6 - 1);
            width = widthSize;
            if (heightMode == MeasureSpec.EXACTLY) {//高度已知
                itemHeight = heightSize - getPaddingTop() - getPaddingBottom();
                height = heightSize;
            } else {//高度未知
                height = itemHeight + getPaddingBottom() + getPaddingTop();
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: ");
        //绘制圆角矩形
        for (int i = 0; i < mPwdLength; i++) {
            mRectF.left = i * (itemWidth + itemWidth / 5) + getPaddingLeft() + mStrokeWidth / 2;
            mRectF.top = getPaddingTop() + mStrokeWidth / 2;
            mRectF.right = mRectF.left + itemWidth - mStrokeWidth;
            mRectF.bottom = mRectF.top + itemHeight - mStrokeWidth;
            canvas.drawRoundRect(mRectF, roundRectRaidus, roundRectRaidus, mPaint);
        }
        //绘制文字
        for (int i = 0; i < mPasswords.size(); i++) {
            String text = mPasswords.get(i);
            textRect.setEmpty();
            //测量文字宽高
            mTextPaint.getTextBounds(text, 0, text.length(), textRect);
            //绘制文字坐标
            float textX = i * itemWidth / 5.0f + (i * 2 + 1) * itemWidth / 2.0f + getPaddingLeft() - textRect.width() / 2.0f;
            float textY = getPaddingTop() + itemHeight / 2.0f + textRect.height() / 2.0f;
            canvas.drawText(text, textX, textY, mTextPaint);
        }
        //绘制圆点
        for (int i = 0; i < mNeedHidden.size(); i++) {
            //圆心坐标
            float cx = i * itemWidth / 5.0f + (i * 2 + 1) * itemWidth / 2.0f + getPaddingLeft();
            float cy = itemHeight / 2.0f + getPaddingTop() ;
            canvas.drawCircle(cx, cy, mRadius, circlePaint);
        }
    }

    //dp px 换算
    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
    //获取密码框最大宽高
    private int getMaxItemWidth() {
        return ((getResources().getDisplayMetrics().widthPixels - getPaddingRight() - getPaddingLeft()) * 5) / (mPwdLength * 6 - 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "onTouchEvent: ");
            requestFocus();
            inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_FORCED);
            return true;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;
        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
        return new BaseInputConnection(this, false);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (KeyEvent.ACTION_DOWN != event.getAction()) {
            return false;
        }
        if (event.isShiftPressed()) {
            return false;
        }
        //只能输入数字
        if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            if (mPasswords.size() < mPwdLength) {
                Log.d(TAG, "onKey: add");
                mPasswords.add(String.valueOf(keyCode - 7));
                confirmFinshed();
                Message message = Message.obtain();
                message.what = 100;
                message.obj = String.valueOf(keyCode - 7);
                mHandler.sendMessageDelayed(message, 250);
                invalidate();

            }
            return true;
        }
        //删除键
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (mPasswords.size() > 0) {
                Log.d(TAG, "onKey: delete");
                mPasswords.remove(mPasswords.size() - 1);
                if (!mNeedHidden.isEmpty()) {
                    mNeedHidden.remove(mNeedHidden.size() - 1);
                }
                invalidate();
            }
            return true;
        }
        //确认
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            confirmFinshed();
            return true;
        }
        return false;
    }

    //获取密码
    public String getPwd() {
        StringBuilder sb = new StringBuilder();
        for (String p : mPasswords) {
            sb.append(p);
        }
        return sb.toString();
    }
    //密码输入完成 隐藏键盘
    private void confirmFinshed() {
        if (mPasswords.size() == mPwdLength) {
            inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            if (onInputFinished != null) {
                onInputFinished.onFinished(getPwd());
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
    }
    //对外输出密码
     public interface OnInputFinished {
        void onFinished(String pwd);
    }
}