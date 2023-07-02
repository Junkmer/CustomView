package com.junker.custom.view.customview.piechart;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.junker.custom.view.R;
import com.junker.custom.view.utils.DensityUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author Junker
 * @ClassName PieChartView
 * @date 2023/6/28 15:53
 * @Version 1.0
 * @Description TODO：绘制一个扇形统计图，主要有三部分需要绘制：1.扇形图 、2.折线 、3.文本提示
 */
public class PieChartView extends View {
    private final static int DEFAULT_HEIGHT_SIZE = 500;
    private static final String TAG = PieChartActivity.class.getSimpleName();

    private final static int[] DEFAULT_COLORS = new int[]
            {
                    Color.BLUE, Color.RED, Color.DKGRAY,
                    Color.GREEN, Color.CYAN, Color.LTGRAY,
                    Color.MAGENTA, Color.GRAY, Color.WHITE,
                    Color.YELLOW, Color.BLACK
            };

    private List<PieChartBean> mListPieChat;
    private List<String> mListTitle;

    private float mTextSize;
    private final int widthPixels;
    private int centerX;
    private int centerY;
    private int mRadius;
    private Paint mPiePaint;
    private List<SectorsData> mListSector;

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
            int widthDp = DensityUtil.px2dip(widthSize);
            int maxWidth = Math.max(widthDp, widthPixels / 3) - getPaddingLeft() - getPaddingRight();
            contentMeasureWidth = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
        } else {
            contentMeasureWidth = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            int heightDp = DensityUtil.px2dip(heightSize);
            int maxHeight = Math.max(heightDp, DEFAULT_HEIGHT_SIZE) - getPaddingTop() - getPaddingBottom();
            contentMeasureHeight = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
        } else {
            contentMeasureHeight = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
        }

        setMeasuredDimension(contentMeasureWidth, contentMeasureHeight);

        initRectF();

        //初始化数据
        initData();
    }

    private void initData() {
        mListSector = new ArrayList<>();
        mListPieChat = new ArrayList<>();
        mListTitle = new ArrayList<>();

        mListSector.add(new SectorsData("红队", 40f));
        mListSector.add(new SectorsData("蓝队", 3f));
        mListSector.add(new SectorsData("黄队", 20f));
        mListSector.add(new SectorsData("绿队", 55f));
        mListSector.add(new SectorsData("紫队", 80f));
        mListSector.add(new SectorsData("白队", 50f));
        mListSector.add(new SectorsData("黑队", 7f));

        //对数据进行排序
        Collections.sort(mListSector);

        float totalCount = 0;
        float maxProportion = 0;
        for (SectorsData sectorsData : mListSector) {
            totalCount += sectorsData.getProportion();
            if (sectorsData.getProportion() > maxProportion) {
                maxProportion = sectorsData.getProportion();
            }
        }

        float itemStartAngle = 0;
        float itemSweepAngle = 0;
        for (int i = 0; i < mListSector.size(); i++) {
            SectorsData sectorsData = mListSector.get(i);
            float skewingLength;
            if (sectorsData.getProportion() == maxProportion) {
                skewingLength = 30f;
            } else {
                skewingLength = 3f;
            }

            itemStartAngle += itemSweepAngle;
            itemSweepAngle = sectorsData.getProportion() / totalCount * 360f;
            PieChartBean chartBean = calculateDirectionCord(itemStartAngle, itemSweepAngle, skewingLength, DEFAULT_COLORS[i]);
            mListPieChat.add(chartBean);

            mListTitle.add(sectorsData.getTitle());
        }
    }

    /**
     * 根据扇形角度计算扇形偏移方向及最终坐标
     */
    private PieChartBean calculateDirectionCord(float startAngle, float sweepAngle, float skewingLength, int color) {
        PieChartBean chartBean = new PieChartBean();

        chartBean.setStartAngle(startAngle);
        chartBean.setSweepAngle(sweepAngle);
        chartBean.setColor(color);

        //扇形中心角度 = startAngle(起始角度) + sweepAngle(扫过的角度)/2
        float centerAngle = startAngle + sweepAngle / 2f;
        //角度转弧度： Math.PI/180 * 角度
        //已知 skewingLength 为斜边，根据正弦、余弦函数，可得出 x轴与y轴偏移量
        //以水平X轴为起始角度向下滑扫，因此 x坐标 = Math.con(斜边长度 * Math.PI / 180); y坐标 = Math.sin(斜边长度 * Math.PI / 180);
        float skewingX = (float) (skewingLength * Math.cos(centerAngle * Math.PI / 180));
        float skewingY = (float) (skewingLength * Math.sin(centerAngle * Math.PI / 180));

        chartBean.setLeft(centerX - mRadius + skewingX);
        chartBean.setTop(centerY - mRadius + skewingY);
        chartBean.setRight(centerX + mRadius + skewingX);
        chartBean.setBottom(centerY + mRadius + skewingY);

        return chartBean;
    }

    private void initRectF() {
        int mWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();
        centerX = mWidth / 2;
        centerY = mHeight / 2;
        mRadius = (int) (Math.min(centerX, centerY) * 0.80f);
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
        //绘制扇形图
        drawPieChart(canvas);
        //绘制折线
        //绘制文本
    }

    private void drawPieChart(Canvas canvas) {
//        canvas.save();//save()方法需要配合restore()方法使用

        /*
         * float left, float top, float right, float bottom：绘制区域
         * float startAngle：设置圆弧是从哪个角度来顺时针绘画的
         * float sweepAngle：设置圆弧扫过的角度
         * boolean useCenter：设置在绘画圆弧时，是否经过圆心
         * Paint paint：设置画笔对象的属性
         * drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter,Paint paint)
         */

        for (PieChartBean chartBean : mListPieChat) {
            mPiePaint.setColor(chartBean.getColor());
            canvas.drawArc(chartBean.getLeft(), chartBean.getTop(), chartBean.getRight(), chartBean.getBottom(),
                    chartBean.getStartAngle(), chartBean.getSweepAngle(), true, mPiePaint);
        }

//        canvas.restore();
    }
}
