package com.junker.custom.view.customview.slidemenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;

import com.junker.custom.view.R;

/**
 * @Author Junker
 * @ClassName SlideMenuView
 * @date 2023/6/13 14:36
 * @Description TODO
 * @Version 1.0
 */
public class SlideMenuView extends ViewGroup implements View.OnClickListener {

    private static final String TAG = SlideMenuView.class.getSimpleName();
    private int mFunction;
    private View mContentView;
    private View mEditView;
    private OnEditClickListener mOnEditClickListener;
    private TextView mReadTv;
    private TextView mTopTv;
    private TextView mDeleteTv;
    private int offsetX;
    private float downX;
    private float moveX;
    private Scroller mScroller;

    //是否打开
    private boolean isOpen;
    private Direction mCurrentDirect = Direction.NOME;

    private int mMaxDuration = 800;
    private int mMinDuration = 300;

    enum Direction {
        LEFT, RIGHT, NOME
    }

    public SlideMenuView(Context context) {
        this(context, null);
    }

    public SlideMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideMenuView);
        mFunction = a.getInt(R.styleable.SlideMenuView_function, 0x30);
        //释放资源
        a.recycle();

        mScroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        Log.e(TAG, "child count == > " + childCount);
        //判断，只能有一个子view
        if (childCount > 1) {
            throw new IllegalArgumentException("no more then one child.");
        }
        mContentView = getChildAt(0);
        //根据属性，继续添加子view
        mEditView = LayoutInflater.from(getContext()).inflate(R.layout.item_slide_action, this, false);
        this.addView(mEditView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //测量第一个孩子
        //宽度，跟父控件一样宽，高度有三种情况：1、指定大小，则取指定的大小；2、wrap_content,设置为 at_most；3、match_parent，给他设置大小
        LayoutParams contentLayoutParams = mContentView.getLayoutParams();
        int contentHeight = contentLayoutParams.height;
        int contentHeightMeasureSpace;
        if (contentHeight == LayoutParams.MATCH_PARENT) {
            contentHeightMeasureSpace = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        } else if (contentHeight == LayoutParams.WRAP_CONTENT) {
            contentHeightMeasureSpace = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
        } else {
            //指定大小
            contentHeightMeasureSpace = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }
        mContentView.measure(widthMeasureSpec, contentHeightMeasureSpace);
        //拿到内容部分测量侧滑的高度
        int contentMeasureHeight = mContentView.getMeasuredHeight();
        //测量编辑部分，宽度：3/4,高度跟内容一致
        int editWidthSize = widthSize * 3 / 4;
        mEditView.measure(MeasureSpec.makeMeasureSpec(editWidthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(contentMeasureHeight, MeasureSpec.EXACTLY));
        initEditView();
        //测量自己
        //宽：所有item宽度总和，高：跟内容一致
        setMeasuredDimension(widthSize + editWidthSize, contentMeasureHeight);
    }

    private void initEditView() {
        mReadTv = mEditView.findViewById(R.id.read_tv);
        mTopTv = mEditView.findViewById(R.id.top_tv);
        mDeleteTv = mEditView.findViewById(R.id.delete_tv);
//        mReadTv.setOnClickListener(this);
//        mTopTv.setOnClickListener(this);
//        mDeleteTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mOnEditClickListener == null) {
            Log.d(TAG, "mOnEditClickListener is null...");
            return;
        }
        if (v == mReadTv) {
            mOnEditClickListener.onReadClick();
        } else if (v == mTopTv) {
            mOnEditClickListener.onTopClick();
        } else if (v == mDeleteTv) {
            mOnEditClickListener.onDeleteClick();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //摆放内容
        int contentLeft = 0;
        int contentTop = 0;
        int contentRight = contentLeft + mContentView.getMeasuredWidth();
        int contentBottom = mContentView.getMeasuredHeight();
        mContentView.layout(contentLeft, contentTop, contentRight, contentBottom);
        //摆放编辑部分
        int editRight = contentRight + mEditView.getMeasuredWidth();
        mEditView.layout(contentRight, contentTop, editRight, contentBottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int scrollX = getScrollX();
                moveX = event.getX();
                offsetX = (int) (moveX - downX);

                if (offsetX > 0) {
                    mCurrentDirect = Direction.RIGHT;
                } else {
                    mCurrentDirect = Direction.LEFT;
                }

                //判断是否滑动到边界
                int resultScrollX = -offsetX + scrollX;
                if (resultScrollX <= 0 || resultScrollX >= mEditView.getMeasuredWidth()) {
                    scrollBy(0, 0);
                } else {
                    scrollBy(-offsetX, 0);
                }
                downX = moveX;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                int hasBeanScrollX = getScrollX();
                int scrollWidth = mEditView.getMeasuredWidth();
                //主要关注，是否已打开，滑动方向
                if (isOpen) {
                    //打开状态
                    if (mCurrentDirect == Direction.LEFT) {
                        open();
                    } else if (mCurrentDirect == Direction.RIGHT) {
                        if (hasBeanScrollX <= scrollWidth * 3 / 4) {
                            close();
                        } else {
                            open();
                        }
                    }
                } else {
                    //关闭状态
                    if (mCurrentDirect == Direction.LEFT) {
                        //向左滑动，判断滑动距离
                        if (hasBeanScrollX > scrollWidth / 4) {
                            open();
                        } else {
                            close();
                        }
                    } else if (mCurrentDirect == Direction.RIGHT) {
                        close();
                    }
                }
                invalidate();
                break;
        }
        return true;
    }

    private void open() {
        //显示
        int dx = mEditView.getMeasuredWidth() - getScrollX();
        int duration = (int) (dx / (mEditView.getMeasuredWidth() * 4 / 5f) * mMaxDuration);
        int absDuration = Math.abs(duration);
        if (absDuration < mMinDuration){
            absDuration = mMinDuration;
        }
        mScroller.startScroll(getScrollX(), 0, dx, 0, absDuration);
        isOpen = true;
    }

    private void close() {
        //隐藏"
        int dx = -getScrollX();
        int duration = (int) (dx / (mEditView.getMeasuredWidth() * 4 / 5f) * mMaxDuration);
        int absDuration = Math.abs(duration);
        if (absDuration < mMinDuration){
            absDuration = mMinDuration;
        }
        mScroller.startScroll(getScrollX(), 0, dx, 0, absDuration);
        isOpen = false;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            invalidate();
        }
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.mOnEditClickListener = listener;
    }

    public interface OnEditClickListener {
        void onReadClick();

        void onTopClick();

        void onDeleteClick();
    }
}
