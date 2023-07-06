package com.junker.custom.view.customview.piechart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
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
    private final static int DEFAULT_TEXT_SIZE = DensityUtil.dip2px(13);
    private final static int DEFAULT_HIGHLIGHT_OFFSET_SIZE = 3;
    private static final String TAG = PieChartActivity.class.getSimpleName();

    private final static int[] DEFAULT_COLORS = new int[]
            {
                    Color.BLUE, Color.RED, Color.DKGRAY,
                    Color.GREEN, Color.CYAN, Color.LTGRAY,
                    Color.MAGENTA, Color.GRAY, Color.WHITE,
                    Color.YELLOW, Color.BLACK
            };

    private List<PieChartBean> mListPieChart;
    private List<LineChartBean> mListLineChart;
    private List<TextChartBean> mListTextChart;

    private final int widthPixels;
    private int centerX;
    private int centerY;
    private int mRadius;
    private Paint mPiePaint;
    private List<SectorsData> mListSector = new ArrayList<>();

    private int[] mPieChartColor = DEFAULT_COLORS;
    private float mTextSize;
    private int mTextAndLineColor;
    private float mHighlightOffsetSize;

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

    private void initAttr(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PieChartView);
        mTextSize = a.getDimension(R.styleable.PieChartView_tipTextSize, DEFAULT_TEXT_SIZE);
        mTextAndLineColor = a.getColor(R.styleable.PieChartView_textAndLineColor, getResources().getColor(R.color.black));
        mHighlightOffsetSize = a.getDimension(R.styleable.PieChartView_highlightOffsetSize, DEFAULT_HIGHLIGHT_OFFSET_SIZE);

        a.recycle();
    }

    /**
     * 测量
     *
     * @param widthMeasureSpec  默认宽度
     * @param heightMeasureSpec 默认高度
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

        initImportantParam();

    }

    /**
     * 初始化数据
     */
    public void initData(List<SectorsData> mList) {
        if (mList == null) {
            throw new IllegalArgumentException("mList parameter in the initData method cannot be empty.");
        }
        mListSector = mList;
//        Collections.sort(mListSector);
        invalidate();//刷新view
    }

    public void addData(SectorsData mData) {
        mListSector.add(mData);
        Collections.sort(mListSector);
        invalidate();//刷新view
    }

    public void updateData(List<SectorsData> mList) {
        this.mListSector = mList;
        Collections.sort(mListSector);
        invalidate();//刷新view
    }

    public List<SectorsData> getData() {
        return mListSector;
    }

    /**
     * 初始化绘制图形前的重要参数
     */
    private void initImportantParam() {
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
        mPiePaint.setStrokeWidth(5);
        mPiePaint.setAntiAlias(true);
        mPiePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制扇形图
        drawPieChart(canvas);
        //绘制折线
        drawLine(canvas);
        //绘制文本
        drawText(canvas);
    }

    private void drawText(@NonNull Canvas canvas) {
        canvas.save();

        mPiePaint.setTextSize(mTextSize);
        mPiePaint.setColor(mTextAndLineColor);
        mListTextChart = calculateTextChartDatas();

        for (TextChartBean bean : mListTextChart) {
            mPiePaint.setTextAlign(bean.getPaintAlign());
            float textX = 0;
            if (bean.getPaintAlign() == Paint.Align.LEFT) {
                textX = bean.getStartX() + 10;
            } else if (bean.getPaintAlign() == Paint.Align.RIGHT) {
                textX = bean.getStartX() - 10;
            }
            float textY = bean.getStartY() + mTextSize / 2f;
            canvas.drawText(bean.getText(), textX, textY, mPiePaint);
        }

        canvas.restore();
    }

    @NonNull
    private List<TextChartBean> calculateTextChartDatas() {
        List<TextChartBean> textChartBeans = new ArrayList<>();

        for (int i = 0; i < mListSector.size(); i++) {
            TextChartBean bean = new TextChartBean();

            LineChartBean lineChartBean = mListLineChart.get(i);
            bean.setStartX(lineChartBean.getEndX());
            bean.setStartY(lineChartBean.getEndY());
            bean.setText(mListSector.get(i).getTitle());

            if (lineChartBean.getStartAngle() > 90 && lineChartBean.getStartAngle() < 270) {
                bean.setPaintAlign(Paint.Align.RIGHT);
            } else {
                bean.setPaintAlign(Paint.Align.LEFT);
            }

            textChartBeans.add(bean);
        }

        return textChartBeans;
    }

    private void drawLine(@NonNull Canvas canvas) {
        canvas.save();
        mPiePaint.setColor(mTextAndLineColor);//灰色
        mListLineChart = calculateLineChartDatas();

        for (LineChartBean bean : mListLineChart) {
            Log.e(TAG, bean.toString());
            //绘制[起始坐标]至[转折坐标]的线
            canvas.drawLine(bean.getStartX(), bean.getStartY(), bean.getBendX(), bean.getBendY(), mPiePaint);
            //[转折坐标]至[结束坐标]的线
            canvas.drawLine(bean.getBendX(), bean.getBendY(), bean.getEndX(), bean.getEndY(), mPiePaint);
        }

        canvas.restore();
    }

    @NonNull
    private List<LineChartBean> calculateLineChartDatas() {
        List<LineChartBean> lineChartBeans = new ArrayList<>();

        for (int i = 0; i < mListSector.size(); i++) {
            LineChartBean bean = new LineChartBean();

            PieChartBean chartBean = mListPieChart.get(i);

            //起始坐标
            float startOffsetX = (float) (mRadius * Math.cos(chartBean.getMiddleAngle() * Math.PI / 180));
            float startOffsetY = (float) (mRadius * Math.sin(chartBean.getMiddleAngle() * Math.PI / 180));
            bean.setStartX(chartBean.getCenterX() + startOffsetX);
            bean.setStartY(chartBean.getCenterY() + startOffsetY);

            //转折坐标
            float bendOffsetX = (float) ((mRadius + 50) * Math.cos(chartBean.getMiddleAngle() * Math.PI / 180));
            float bendOffsetY = (float) ((mRadius + 50) * Math.sin(chartBean.getMiddleAngle() * Math.PI / 180));
            bean.setBendX(chartBean.getCenterX() + bendOffsetX);
            bean.setBendY(chartBean.getCenterY() + bendOffsetY);

            //结束坐标
            float endX;
            if (chartBean.getMiddleAngle() > 90 && chartBean.getMiddleAngle() < 270) {
                endX = centerX - mRadius - 100;
            } else {
                endX = centerX + mRadius + 100;
            }
            float endY = bean.getBendY();
            bean.setEndX(endX);
            bean.setEndY(endY);
            bean.setStartAngle(chartBean.getMiddleAngle());

            lineChartBeans.add(bean);
        }

        processYDistance(lineChartBeans);

        return lineChartBeans;
    }

    /**
     * 解决 当两条转折线y轴坐标靠的很近时，引起的title内容显示重叠问题
     */
    private void processYDistance(List<LineChartBean> chartBeans) {

        int rightClosestToHorizonAnglePosition = -1;//右边最靠经水平线的数据position
        int leftClosestToHorizonAnglePosition = -1;//左边最靠近说明先的数据position

        float rightBeginAngle = chartBeans.get(0).getStartAngle();
        float rightEndAngle = chartBeans.get(chartBeans.size() - 1).getStartAngle();
        if (rightBeginAngle < 90 && rightEndAngle > 270) {
            rightClosestToHorizonAnglePosition = rightBeginAngle > 360 - rightEndAngle ? chartBeans.size() - 1 : 0;
        } else {
            if (rightBeginAngle < 90) {
                rightClosestToHorizonAnglePosition = 0;
            }
            if (rightEndAngle > 270) {
                rightClosestToHorizonAnglePosition = chartBeans.size() - 1;
            }
        }

        boolean isLeftHaveData = false;
        int leftClosestToPosition = 0;
        for (int i = 0; i < chartBeans.size(); i++) {
            float mAngle = chartBeans.get(i).getStartAngle();
            if (mAngle >= 90 && mAngle <= 270) {
                float oldAngle = Math.abs(180 - chartBeans.get(leftClosestToPosition).getStartAngle());
                float currentAngle = Math.abs(180 - mAngle);
                leftClosestToPosition = oldAngle > currentAngle ? i : leftClosestToPosition;
                isLeftHaveData = true;
            }
        }
        if (isLeftHaveData) {
            leftClosestToHorizonAnglePosition = leftClosestToPosition;
        }

        LineChartBean rightClosestToBean = null;
        if (rightClosestToHorizonAnglePosition != -1) {
            rightClosestToBean = chartBeans.get(rightClosestToHorizonAnglePosition);
        }

        LineChartBean leftClosestToBean = null;
        if (leftClosestToHorizonAnglePosition != -1) {
            leftClosestToBean = chartBeans.get(leftClosestToHorizonAnglePosition);
        }

        List<LineChartBean> rightBottomList = new ArrayList<>();
        List<LineChartBean> leftBottomList = new ArrayList<>();
        List<LineChartBean> leftTopList = new ArrayList<>();
        List<LineChartBean> rightTopList = new ArrayList<>();

        for (LineChartBean item : chartBeans) {
            if (item.getStartAngle() < 90) {
                rightBottomList.add(item);
            } else if (item.getStartAngle() >= 90 && item.getStartAngle() < 180) {
                leftBottomList.add(item);
            } else if (item.getStartAngle() >= 180 && item.getStartAngle() <= 270) {
                leftTopList.add(item);
            } else if (item.getStartAngle() > 270) {
                rightTopList.add(item);
            }
        }

        float beforeY = 0;
        float currentY;

        //------------- 处理右下区域 -----------//
        if (rightClosestToBean != null) {
            beforeY = rightClosestToBean.getBendY();
        }
        int rightBottomListSize = rightBottomList.size();
        for (int i = 0; i < rightBottomListSize; i++) {
            LineChartBean item = rightBottomList.get(i);
            currentY = item.getBendY();
            if (chartBeans.get(rightClosestToHorizonAnglePosition).getBendY() != currentY) {
                float offset = Math.abs(currentY - beforeY) - mTextSize;
                if (offset < 10) {//10 表示两个title内容的间距
                    currentY = currentY - (10 - offset);
                    chartBeans.get(i).setBendY(currentY);
                    chartBeans.get(i).setEndY(currentY);
                }
                beforeY = currentY;
            }
        }

        //------------- 处理左下区域 -----------//
        if (leftClosestToBean != null) {
            beforeY = leftClosestToBean.getBendY();
        }
        int leftBottomListSize = leftBottomList.size();
        for (int i = 0; i < leftBottomList.size(); i++) {//处理左下区域
            int position = leftBottomListSize - 1 - i;
            LineChartBean item = leftBottomList.get(position);
            currentY = item.getBendY();
            if (chartBeans.get(leftClosestToHorizonAnglePosition).getBendY() != currentY) {
                float offset = Math.abs(currentY - beforeY) - mTextSize;
                if (offset < 10) {//10 表示两个title内容的间距
                    currentY = currentY - (10 - offset);
                    chartBeans.get(rightBottomListSize + position).setBendY(currentY);
                    chartBeans.get(rightBottomListSize + position).setEndY(currentY);
                }
                beforeY = currentY;
            }
        }

        //------------- 处理左上区域 -----------//
        if (leftClosestToBean != null) {
            beforeY = leftClosestToBean.getBendY();
        }
        int leftTopListSize = leftBottomList.size();
        for (int i = 0; i < leftTopListSize; i++) {//处理左上区域
            LineChartBean item = leftBottomList.get(i);
            currentY = item.getBendY();
            if (chartBeans.get(leftClosestToHorizonAnglePosition).getBendY() != currentY) {
                float offset = Math.abs(currentY - beforeY) - mTextSize;
                if (offset < 10) {//10 表示两个title内容的间距
                    currentY = currentY - (10 - offset);
                    int position = rightBottomListSize + leftBottomListSize + i;
                    chartBeans.get(position).setBendY(currentY);
                    chartBeans.get(position).setEndY(currentY);
                }
                beforeY = currentY;
            }
        }

        //-------------处理右上区域 -----------//
        if (rightClosestToBean != null) {
            beforeY = rightClosestToBean.getBendY();
        }
        int rightTopListSize = rightTopList.size();
        for (int i = 0; i < rightTopListSize; i++) {//处理右上区域
            int position = rightTopListSize - 1 - i;
            LineChartBean item = rightTopList.get(position);
            currentY = item.getBendY();
            if (chartBeans.get(rightClosestToHorizonAnglePosition).getBendY() != currentY) {
                float offset = Math.abs(currentY - beforeY) - mTextSize;
                if (offset < 10) {//10 表示两个title内容的间距
                    currentY = currentY - (10 - offset);
                    chartBeans.get(rightBottomListSize + leftBottomListSize + leftTopListSize + position).setBendY(currentY);
                    chartBeans.get(rightBottomListSize + leftBottomListSize + leftTopListSize + position + position).setEndY(currentY);
                }
                beforeY = currentY;
            }
        }
    }

    private void drawPieChart(@NonNull Canvas canvas) {
        canvas.save();//save()方法需要配合restore()方法使用

        mListPieChart = calculatePieChartDatas();//初始化数据

        /*
         * float left, float top, float right, float bottom：绘制区域
         * float startAngle：设置圆弧是从哪个角度来顺时针绘画的
         * float sweepAngle：设置圆弧扫过的角度
         * boolean useCenter：设置在绘画圆弧时，是否经过圆心
         * Paint paint：设置画笔对象的属性
         * drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter,Paint paint)
         */

        for (PieChartBean bean : mListPieChart) {
            mPiePaint.setColor(bean.getColor());
            canvas.drawArc(bean.getLeft(), bean.getTop(), bean.getRight(), bean.getBottom(),
                    bean.getStartAngle(), bean.getSweepAngle(), true, mPiePaint);
        }

        canvas.restore();
    }

    @NonNull
    private List<PieChartBean> calculatePieChartDatas() {

        List<PieChartBean> pieChartBeans = new ArrayList<>();

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
                skewingLength = mHighlightOffsetSize;
            } else {
                skewingLength = DEFAULT_HIGHLIGHT_OFFSET_SIZE;
            }

            itemStartAngle += itemSweepAngle;
            itemSweepAngle = sectorsData.getProportion() / totalCount * 360f;
            PieChartBean chartBean = calculateDirectionCord(itemStartAngle, itemSweepAngle, skewingLength, mPieChartColor[i % mPieChartColor.length]);
            pieChartBeans.add(chartBean);
        }

        return pieChartBeans;
    }

    /**
     * 根据扇形角度计算扇形偏移方向及最终坐标
     */
    @NonNull
    private PieChartBean calculateDirectionCord(float startAngle, float sweepAngle, float skewingLength, int color) {
        PieChartBean chartBean = new PieChartBean();

        chartBean.setStartAngle(startAngle);
        chartBean.setSweepAngle(sweepAngle);
        chartBean.setColor(color);

        //扇形中心角度 = startAngle(起始角度) + sweepAngle(扫过的角度)/2
        float centerAngle = startAngle + sweepAngle / 2f;
        chartBean.setMiddleAngle(centerAngle);
        //角度转弧度： Math.PI/180 * 角度
        //已知 skewingLength 为斜边，根据正弦、余弦函数，可得出 x轴与y轴偏移量
        //以水平X轴为起始角度向下滑扫，因此 x坐标 = Math.con(斜边长度 * Math.PI / 180); y坐标 = Math.sin(斜边长度 * Math.PI / 180);
        float skewingX = (float) (skewingLength * Math.cos(centerAngle * Math.PI / 180));
        float skewingY = (float) (skewingLength * Math.sin(centerAngle * Math.PI / 180));

        chartBean.setLeft(centerX - mRadius + skewingX);
        chartBean.setTop(centerY - mRadius + skewingY);
        chartBean.setRight(centerX + mRadius + skewingX);
        chartBean.setBottom(centerY + mRadius + skewingY);
        chartBean.setCenterX(centerX + skewingX);
        chartBean.setCenterY(centerY + skewingY);

        return chartBean;
    }

    public int[] getPieChartColor() {
        return mPieChartColor;
    }

    public void setPieChartColor(int[] mPieChartColor) {
        this.mPieChartColor = mPieChartColor;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public int getTextAndLineColor() {
        return mTextAndLineColor;
    }

    public void setTextAndLineColor(int mTextAndLineColor) {
        this.mTextAndLineColor = mTextAndLineColor;
    }

    public float getHighlightOffsetSize() {
        return mHighlightOffsetSize;
    }

    public void setHighlightOffsetSize(float mHighlightOffsetSize) {
        this.mHighlightOffsetSize = mHighlightOffsetSize;
    }
}
