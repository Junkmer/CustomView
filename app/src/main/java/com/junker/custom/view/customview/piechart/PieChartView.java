package com.junker.custom.view.customview.piechart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;

import com.junker.custom.view.R;
import com.junker.custom.view.utils.DensityUtil;

/**
 * @Author Junker
 * @ClassName PieChartView
 * @date 2023/6/28 15:53
 * @Description TODO
 * @Version 1.0
 */
public class PieChartView extends View {
    private final static int DEFAULT_HEIGHT_SIZE = DensityUtil.dip2px(500);

    private float mTextSize;
    private int widthPixels;
    private int mWidth;
    private int mHeight;
    private int mRadius;
    private Paint mPiePaint;

    public PieChartView(Context context) {
        this(context, null);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        widthPixels = displayMetrics.widthPixels;

        //初始化自定义属性
        initAttr(context, attrs);
        //初始化画笔
        initPaint();
    }

    private void initAttr(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PieChartView);
        mTextSize = a.getDimension(R.styleable.PieChartView_tipTextSize, -1);
        a.recycle();
    }

    /**
     * 测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int contentMeasureWidth;
        int contentMeasureHeight;
        if (widthMode == MeasureSpec.EXACTLY) {
            int maxWidth = Math.max(widthSize, widthPixels / 3) - getPaddingLeft() - getPaddingRight();
            contentMeasureWidth = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
        } else {
            contentMeasureWidth = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            int maxHeight = Math.max(heightSize, DEFAULT_HEIGHT_SIZE) - getPaddingTop() - getPaddingBottom();
            contentMeasureHeight = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
        } else {
            contentMeasureHeight = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
        }

        setMeasuredDimension(contentMeasureWidth, contentMeasureHeight);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRadius = (int) (Math.min(mWidth, mHeight) / 2 * 0.7f);
    }

    private void initPaint() {
        mPiePaint = new Paint();
        mPiePaint.setColor(Color.BLUE);
        mPiePaint.setStyle(Paint.Style.FILL);
        mPiePaint.setAntiAlias(true);
        mPiePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPieChart(canvas);
    }

    private void drawPieChart(Canvas canvas){
        canvas.save();//save()方法需要配合restore()方法使用

        /**
         * RectF oval,
         * float startAngle,
         * float sweepAngle,
         * boolean useCenter,
         * Paint paint
         */
//        canvas.drawArc();

        canvas.restore();
    }
}
