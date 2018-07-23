package com.lx.utils.view;

/**
 * Created by lixiao2 on 2018/3/12.
 */
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.lx.utils.R;

/**
 * 自定义容器(LinearLayout),支持padding，margin，设置水平垂直方向，和gravity
 */
public class MyViewGroup extends ViewGroup {
    private int mOrientation;
    private int mGravity;
    private int mPaddingTop;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingBottom;
    private int childWidth;
    private int childHeight;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private static final int[] ORIENTATION_FLAGS = {
            HORIZONTAL, VERTICAL
    };

    private Scroller mScroller;
    private int mTouchSlop;
    private boolean sameDirection;//是否同向(嵌套时)

    private float downX, downY, moveX, moveY, lastX, lastY;
    private boolean isFirst;
    private VelocityTracker mVelocityTracker;
    private int mPointerId;
    private int mMaxVelocity;//最大速度

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyViewGroup);
        mOrientation = ORIENTATION_FLAGS[a.getInt(R.styleable.MyViewGroup_orientation, 1)];
        mGravity = a.getInt(R.styleable.MyViewGroup_gravity, Gravity.START | Gravity.TOP);
        a.recycle();

        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop(); // 触发移动的最小距离
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mOrientation == VERTICAL) {
            layoutVertical(l, t, r, b);
        } else {
            layoutHorizontal(l, t, r, b);
        }
        //摆放好后getParent()才有值
        if (getParent() instanceof MyViewGroup) {
            sameDirection = ((MyViewGroup) getParent()).getOrientation() == getOrientation();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    void layoutHorizontal(int l, int t, int r, int b) {
        int childLeft = mPaddingLeft;
        int childTop = mPaddingTop;

        final int height = b - t;
        int childBottom = height - mPaddingBottom;
        int childSpace = height - mPaddingTop - mPaddingBottom;
        final int childCount = getChildCount();
        final int majorGravity = mGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        final int minorGravity = mGravity & Gravity.VERTICAL_GRAVITY_MASK;

        final int layoutDirection = getLayoutDirection();
        switch (Gravity.getAbsoluteGravity(majorGravity, layoutDirection)) {
            case Gravity.RIGHT:
                childLeft = mPaddingLeft + r - l - childWidth;
                break;
            case Gravity.CENTER_HORIZONTAL:
                childLeft = mPaddingLeft + (r - l - childWidth) / 2;
                break;
            case Gravity.LEFT:
            default:
                childLeft = mPaddingLeft;
                break;
        }
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();
                int gravity = params.gravity;
                if (gravity < 0) {
                    gravity = minorGravity;
                }

                switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                    case Gravity.TOP:
                        childTop = mPaddingTop + params.topMargin;
                        break;
                    case Gravity.CENTER_VERTICAL:
                        childTop = mPaddingTop + ((childSpace - childHeight) / 2)
                                + params.topMargin - params.bottomMargin;
                        break;
                    case Gravity.BOTTOM:
                        childTop = childBottom - childHeight - params.bottomMargin;
                        break;
                    default:
                        childTop = mPaddingTop;
                        break;
                }
                childLeft += params.leftMargin;
                childTop += params.topMargin;
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
                childLeft += childWidth + params.rightMargin;
                childTop = mPaddingTop;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    void layoutVertical(int l, int t, int r, int b) {
        int childLeft = mPaddingLeft;
        int childTop = mPaddingTop;

        final int width = r - l;
        int childRight = width - mPaddingRight;
        int childSpace = width - mPaddingLeft - mPaddingRight;
        final int childCount = getChildCount();
        final int majorGravity = mGravity & Gravity.VERTICAL_GRAVITY_MASK;
        final int minorGravity = mGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;

        switch (majorGravity) {
            case Gravity.BOTTOM:
                childTop = mPaddingTop + b - t - childHeight;
                break;
            case Gravity.CENTER_VERTICAL:
                childTop = mPaddingTop + (b - t - childHeight) / 2;
                break;
            case Gravity.TOP:
            default:
                childTop = mPaddingTop;
                break;
        }
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child == null) {
                childTop += 0;
            } else if (child.getVisibility() != GONE) {
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();
                int gravity = params.gravity;
                if (gravity < 0) {
                    gravity = minorGravity;
                }
                final int layoutDirection = getLayoutDirection();
                final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
                switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.CENTER_HORIZONTAL:
                        childLeft = mPaddingLeft + ((childSpace - childWidth) / 2)
                                + params.leftMargin - params.rightMargin;
                        break;
                    case Gravity.RIGHT:
                        childLeft = childRight - childWidth - params.rightMargin;
                        break;

                    case Gravity.LEFT:
                    default:
                        childLeft = mPaddingLeft + params.leftMargin;
                        break;
                }
                childLeft += params.leftMargin;
                childTop += params.topMargin;
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
                childLeft = mPaddingTop;
                childTop += childHeight + params.bottomMargin;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPaddingTop = getPaddingTop();
        mPaddingLeft = getPaddingLeft();
        mPaddingRight = getPaddingRight();
        mPaddingBottom = getPaddingBottom();

        childWidth = 0;//所有child占用的总宽高
        childHeight = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                //为了添加child的margin属性值
                LayoutParams lp = (LayoutParams) child.getLayoutParams();

                //▲变化1：将measureChild改为measureChildWithMargin
                measureChildWithMargins(child, widthMeasureSpec, 0,
                        heightMeasureSpec, 0);
              /*原来：  measureChild(child, widthMeasureSpec,
                        heightMeasureSpec);*/
                if (mOrientation == HORIZONTAL) {
                    childWidth += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                    childHeight = Math.max(childHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                } else {
                    childWidth = Math.max(childWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                    childHeight += child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
                }
            }
        }
        childWidth += mPaddingLeft + mPaddingRight;
        childHeight += mPaddingTop + mPaddingBottom;
        setMeasuredDimension(resolveSize(childWidth, widthMeasureSpec), resolveSize(childHeight, heightMeasureSpec));
    }

    @Override
    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public android.view.ViewGroup.LayoutParams generateLayoutParams(
            AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected android.view.ViewGroup.LayoutParams generateLayoutParams(
            android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public static class LayoutParams extends MarginLayoutParams {
        public int gravity = -1;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray ta = c.obtainStyledAttributes(attrs,
                    R.styleable.MyViewGroup);

            gravity = ta.getInt(R.styleable.MyViewGroup_gravity, -1);

            ta.recycle();
        }

        public LayoutParams(int width, int height) {
            this(width, height, -1);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.gravity = gravity;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
        if (getParent() instanceof MyViewGroup) {
            sameDirection = ((MyViewGroup) getParent()).getOrientation() == orientation;
        }
        requestLayout();
    }

    //------------------------我是快乐的分割线------------------------------------------------------


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            downX = lastX = ev.getX();
            downY = lastY = ev.getY();
        } else if (action == MotionEvent.ACTION_MOVE) {
            //拦截move事件
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        obtainVelocityTracker(event);
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        if (action == MotionEvent.ACTION_DOWN) {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            mPointerId = event.getPointerId(0);
            lastX = x;
            lastY = y;
            getParent().requestDisallowInterceptTouchEvent(true);
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (isFirst) {
                lastX = x;
                lastY = y;
                isFirst = false;
            }
            if (mOrientation == HORIZONTAL) {
                touchMoveHorizontal(event);
            } else {
                touchMoveVertical(event);
            }
            if (scrollChangeListener != null) {//滚动距离的回调
                scrollChangeListener.onScrollChange(getScrollX(), getScrollY());
            }
            mScrollState = SCROLLING;
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            //计算1000ms的速度
            mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
            //获取x，y在mPointerId上的的速度
            final float velocityX = mVelocityTracker.getXVelocity(mPointerId);
            final float velocityY = mVelocityTracker.getYVelocity(mPointerId);
            if (mOrientation == HORIZONTAL) {
                if (getScrollX() < 0) {//超出起始边界，弹回起始位置
                    mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 300);
                } else if (getScrollX() + getWidth() > childWidth) {//超过结尾边界同理
                    mScroller.startScroll(getScrollX(), 0, (int) -(getScrollX() + getWidth() - childWidth), 0, 300);
                } else {//中间时候，按最后的瞬时速度抛出，不超过剩下的距离
                    mScroller.fling(getScrollX(), 0, (int) -velocityX, 0, 0, childWidth - getWidth() + 100, 0, 0);
                }
            } else {
                if (getScrollY() < 0) {
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 300);
                } else if (getScrollY() + getHeight() > childHeight) {
                    mScroller.startScroll(0, getScrollY(), 0, (int) -(getScrollY() + getHeight() - childHeight), 300);
                } else {
                    mScroller.fling(0, getScrollY(), 0, (int) -velocityY, 0, 0, 0, childHeight - getHeight() + 100);
                }
            }
            isFirst = true;
            recycleVelocityTracker();
            postInvalidate();//调用重绘才会调用computeScroll方法，形成动画
        }
        return true;
    }

    /**
     * 水平滚动
     */
    private void touchMoveHorizontal(MotionEvent event) {
        float x = event.getX();
        int offsetX = 0;
        moveX = x;
        //超出界限时，增加阻力
        if (getScrollX() < 0 || getScrollX() + getWidth() > childWidth) {
            offsetX = (int) ((moveX - lastX) / 2.5);
        } else {
            offsetX = (int) (moveX - lastX);
        }
        if (!sameDirection) {
//          不同向
            if (Math.abs(x - downX) + mTouchSlop > Math.abs(event.getY() - downY) && childWidth > getWidth()) {
                getParent().requestDisallowInterceptTouchEvent(true);
            } else {
                getParent().requestDisallowInterceptTouchEvent(false);
            }
        } else {
//          同向
            if (lastX >= downX) {
                if (getScrollX() <= 0) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            } else {
                if (getScrollX() >= childWidth - getWidth()) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            }
        }
        if (Math.abs(downX - x) > mTouchSlop) {
            scrollBy(-offsetX, 0);
        }
        lastX = moveX;
    }

    /**
     * 垂直滚动
     */
    private void touchMoveVertical(MotionEvent event) {
        float y = event.getY();
        int offsetY = 0;
        moveY = y;
        //超出界限时，增加阻力
        if (getScrollY() < 0 || getScrollY() + getHeight() > childHeight) {
            offsetY = (int) ((moveY - lastY) / 2.5);
        } else {
            offsetY = (int) (moveY - lastY);
        }
        if (!sameDirection) {
//          不同向
            if (Math.abs(event.getX() - downX) < Math.abs(y - downY) + mTouchSlop && childHeight > getHeight()) {
                getParent().requestDisallowInterceptTouchEvent(true);
            } else {
                getParent().requestDisallowInterceptTouchEvent(false);
            }
        } else {
//          同向
            if (lastY >= downY) {
                if (getScrollY() <= 0) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            } else {
                if (getScrollY() >= childHeight - getHeight()) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            }
        }
        //有效滑动，滚动-offsetY距离
        if (Math.abs(downY - y) > mTouchSlop) {
            scrollBy(0, -offsetY);
        }
        lastY = moveY;
    }

    /**
     * 创建新的速度监视对象
     *
     * @param event 滑动事件
     */
    private void obtainVelocityTracker(MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 释放资源
     */
    private void recycleVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();//当没滚动到需要的位置时，不断的重绘，形成动画
            mScrollState = SCROLLING;
        } else {
            mScrollState = IDLE;
        }
    }

    /**
     * 滚动距离监听器
     */
    interface ScrollChangeListener {
        void onScrollChange(int scrollX, int scrollY);
    }

    private ScrollChangeListener scrollChangeListener;

    /**
     * 设置滚动监听
     *
     * @param l 回调
     */
    public void setScrollChangeListener(ScrollChangeListener l) {
        this.scrollChangeListener = l;
    }

    private int mScrollState;
    public static final int IDLE = 0;//闲置状态
    public static final int SCROLLING = 1;//滚动状态

    /**
     * @return 当前滚动状态
     */
    public int getScrollState() {
        return mScrollState;
    }
}
