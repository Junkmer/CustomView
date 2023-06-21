package com.junker.custom.view.customview.watchface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.junker.custom.view.R;
import com.junker.custom.view.utils.DensityUtil;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * @Author Junker
 * @ClassName WatchFace
 * @date 2023/6/21 10:41
 * @Description TODO
 * @Version 1.0
 */
public class WatchFaceView extends View {
    private static final String TAG = WatchFaceView.class.getSimpleName();

    private final static int DEFAULT_CENTER_RADIUS_SIZE = DensityUtil.dip2px(5f);
    private final static int DEFAULT_SCALE_STROKE_WIDTH = DensityUtil.dip2px(3f);
    private final static int DEFAULT_SECOND_STROKE_WIDTH = DensityUtil.dip2px(2f);
    private final static int DEFAULT_MINUTE_STROKE_WIDTH = DensityUtil.dip2px(3.2f);
    private final static int DEFAULT_HOUR_STROKE_WIDTH = DensityUtil.dip2px(4f);

    private int mSecondColor;
    private int mMinColor;
    private int mHourColor;
    private int mScaleColor;
    private boolean mIsScaleShow;
    private Paint mSecondPaint;
    private Paint mMinPaint;
    private Paint mHourPaint;
    private Paint mScalePaint;
    private Bitmap mBackgroundImage;
    private int mWidth;
    private int mHeight;
    private Rect mSrcRect;
    private Rect mSrcDes;
    private final Calendar mCalender;
    private int mRadius;
    private int mCenterRadius;
    private int mScaleWidth = DEFAULT_SCALE_STROKE_WIDTH;
    private boolean isUpdate;

    public WatchFaceView(Context context) {
        this(context, null);
    }

    public WatchFaceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WatchFaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化相关属性
        initAttrs(context, attrs);
        //获取日历实例
        mCalender = Calendar.getInstance();
        //设置时区
        mCalender.setTimeZone(TimeZone.getDefault());
        //初始化画笔
        initPaints();
    }

    /**
     * 创建相关画笔
     */
    private void initPaints() {
        //创建秒针画笔
        mSecondPaint = new Paint();
        mSecondPaint.setColor(mSecondColor);//设置画笔颜色
        mSecondPaint.setStyle(Paint.Style.STROKE);
        mSecondPaint.setStrokeWidth(DEFAULT_SECOND_STROKE_WIDTH);//设置 stroke方式的宽度
        mSecondPaint.setAntiAlias(true);//设置抗锯齿
        mSecondPaint.setStrokeCap(Paint.Cap.ROUND);//设置圆角

        //创建分针画笔
        mMinPaint = new Paint();
        mMinPaint.setColor(mMinColor);//设置画笔颜色
        mMinPaint.setStyle(Paint.Style.STROKE);
        mMinPaint.setStrokeWidth(DEFAULT_MINUTE_STROKE_WIDTH);//设置 stroke方式的宽度
        mMinPaint.setAntiAlias(true);//设置抗锯齿
        mMinPaint.setStrokeCap(Paint.Cap.ROUND);//设置圆角

        //创建时针画笔
        mHourPaint = new Paint();
        mHourPaint.setColor(mHourColor);//设置画笔颜色
        mHourPaint.setStyle(Paint.Style.STROKE);
        mHourPaint.setStrokeWidth(DEFAULT_HOUR_STROKE_WIDTH);//设置 stroke方式的宽度
        mHourPaint.setAntiAlias(true);//设置抗锯齿
        mHourPaint.setStrokeCap(Paint.Cap.ROUND);//设置圆角

        //创建表盘画笔
        mScalePaint = new Paint();
        mScalePaint.setColor(mScaleColor);//设置画笔颜色
        mScalePaint.setStyle(Paint.Style.STROKE);
        mScalePaint.setStrokeWidth(DEFAULT_SCALE_STROKE_WIDTH);//设置 stroke方式的宽度
        mScalePaint.setAntiAlias(true);//设置抗锯齿
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WatchFaceView);
        mSecondColor = a.getColor(R.styleable.WatchFaceView_secondColor, getResources().getColor(R.color.second_default_color));
        mMinColor = a.getColor(R.styleable.WatchFaceView_minColor, getResources().getColor(R.color.min_default_color));
        mHourColor = a.getColor(R.styleable.WatchFaceView_hourColor, getResources().getColor(R.color.hour_default_color));
        mScaleColor = a.getColor(R.styleable.WatchFaceView_scaleColor, getResources().getColor(R.color.scale_default_color));
        mIsScaleShow = a.getBoolean(R.styleable.WatchFaceView_scaleShow, true);
        mCenterRadius = a.getInteger(R.styleable.WatchFaceView_centerRadius, DEFAULT_CENTER_RADIUS_SIZE);
        int mBgResId = a.getResourceId(R.styleable.WatchFaceView_faceBackground, -1);
        if (mBgResId != -1) {
            mBackgroundImage = BitmapFactory.decodeResource(getResources(), mBgResId);
        }
        a.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isUpdate = true;
        post(new Runnable() {
            @Override
            public void run() {
                if (isUpdate) {
                    invalidate();
                    postDelayed(this, 1000);
                } else {
                    removeCallbacks(this);
                }

            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.isUpdate = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量自己
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        Log.e(TAG, "MeasureSpec.AT_MOST ==> " + MeasureSpec.AT_MOST);
//        Log.e(TAG, "MeasureSpec.EXACTLY ==> " + MeasureSpec.EXACTLY);
//        Log.e(TAG, "MeasureSpec.UNSPECIFIED ==> " + MeasureSpec.UNSPECIFIED);
//        Log.e(TAG, "widthMode ==> " + widthMode);
//        Log.e(TAG, "widthSize ==> " + widthSize);
//        Log.e(TAG, "heightMode ==> " + heightMode);
//        Log.e(TAG, "heightSize ==> " + heightSize);
        //减去外边距
        int widthTargetSize = widthSize - getPaddingLeft() - getPaddingRight();
        int heightTargetSize = heightSize - getPaddingTop() - getPaddingBottom();
        //判断大小，取小的值
        int targetSize = Math.min(widthTargetSize, heightTargetSize);
        setMeasuredDimension(targetSize, targetSize);

        //初始化Rect
        initRect();
    }

    private void initRect() {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        if (mBackgroundImage == null) {
            Log.d(TAG, "mBackgroundImage is null...");
            return;
        }
        //源坑 --> 选取图片内容区域的大小
        mSrcRect = new Rect();
        mSrcRect.left = 0;
        mSrcRect.top = 0;
        mSrcRect.right = mBackgroundImage.getWidth();
        mSrcRect.bottom = mBackgroundImage.getHeight();
        //目标坑 --> 图片需要显示的大小
        mSrcDes = new Rect();
        mSrcDes.left = 0;
        mSrcDes.top = 0;
        mSrcDes.right = mWidth;
        mSrcDes.bottom = mHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long currentTimeMillis = System.currentTimeMillis();
        mCalender.setTimeInMillis(currentTimeMillis);
        //绘制背景
        canvas.drawColor(getResources().getColor(R.color.black));
        //半径
        mRadius = (int) (mWidth / 2f);
        //绘制表盘
        drawScale(canvas);
        /* 绘制针 */
        //绘制时针
        drawHour(canvas);
        //绘制分针
        drawMin(canvas);
        //绘制秒针
        drawSecond(canvas);
    }

    private void drawSecond(Canvas canvas) {
        canvas.save();//save()方法需要配合restore()方法使用

        int secondRadius = (int) (mRadius * 0.83f);
        int secondValue = mCalender.get(Calendar.SECOND);
        float secondRotate = secondValue * 6f;
        //旋转角度
        canvas.rotate(secondRotate, mRadius, mRadius);
        canvas.drawLine(mRadius, mRadius - mCenterRadius - mScaleWidth, mRadius, mRadius - secondRadius, mSecondPaint);

        canvas.restore();//restore()方法需要配合方法使用save()
    }

    private void drawMin(Canvas canvas) {
        canvas.save();//save()方法需要配合restore()方法使用

        int minRadius = (int) (mRadius * 0.8f);
        int minValue = mCalender.get(Calendar.MINUTE);
        float minRotate = minValue * 6f;
        //旋转角度
        canvas.rotate(minRotate, mRadius, mRadius);
        canvas.drawLine(mRadius, mRadius - mCenterRadius - mScaleWidth, mRadius, mRadius - minRadius, mMinPaint);

        canvas.restore();//restore()方法需要配合方法使用save()
    }

    private void drawHour(Canvas canvas) {
        canvas.save();//save()方法需要配合restore()方法使用

        int hourRadius = (int) (mRadius * 0.7f);
        int hourValue = mCalender.get(Calendar.HOUR);
        int minOffsetValue = mCalender.get(Calendar.MINUTE);
        float hourOffsetRotate = minOffsetValue / 2f;//60分钟时针转30度，所以分针转动对应时针偏移量 = minValue/60*30 = minValue/2;
        float hourRotate = hourValue * 30 + hourOffsetRotate;//一小时时针转30度 再加上 分针的转动 等于 时针实际偏移量
        //旋转角度
        canvas.rotate(hourRotate, mRadius, mRadius);
        canvas.drawLine(mRadius, mRadius - mCenterRadius - mScaleWidth, mRadius, mRadius - hourRadius, mHourPaint);

        canvas.restore();//restore()方法需要配合方法使用save()
    }

    private void drawScale(Canvas canvas) {
        if (mBackgroundImage != null) {
            canvas.drawBitmap(mBackgroundImage, mSrcRect, mSrcDes, mScalePaint);
        } else {
            //内环半径
            int innerC = (int) (mWidth / 2 * 0.85f);
            //中环半径
            int middleC = (int) (mWidth / 2 * 0.9f);
            //外环半径
            int outerC = (int) (mWidth / 2 * 0.95f);
            //画外环圆
            canvas.drawCircle(mRadius, mRadius, outerC, mScalePaint);
            //画中心圆
            canvas.drawCircle(mRadius, mRadius, mCenterRadius, mScalePaint);
//            //绘制刻度方法1️
//            for (int i = 0; i < 12; i++) {
//                double th = i * Math.PI * 2 / 12;
//                //内环
//                int innerB = (int) (Math.cos(th) * innerC);
//                int innerY = mHeight / 2 - innerB;
//                int innerA = (int) (Math.sin(th) * innerC);
//                int innerX = mWidth / 2 + innerA;
//                //外环
//                int outerB = (int) (Math.cos(th) * outerC);
//                int outerY = mHeight / 2 - outerB;
//                int outerA = (int) (Math.sin(th) * outerC);
//                int outerX = mWidth / 2 + outerA;
//                canvas.drawLine(innerX, innerY, outerX, outerY, mScalePaint);
//            }
            //绘制刻度方法2️⃣
            canvas.save();
            for (int i = 0; i < 60; i++) {
                if (i % 3 != 0) {
                    canvas.drawLine(mRadius, mRadius - outerC, mRadius, mRadius - middleC, mScalePaint);
                } else {
                    canvas.drawLine(mRadius, mRadius - outerC, mRadius, mRadius - innerC, mScalePaint);
                }
                canvas.rotate(6, mRadius, mRadius);
            }
            canvas.restore();
        }
    }
}
