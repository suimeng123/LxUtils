package com.lx.utils.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lx.utils.R;
import com.lx.utils.util.DisplayUtil;

/**
 * Created by lixiao2 on 2018/3/2.
 */
public class ReflushRecyclerView extends RecyclerView {
    private int topPadding = 0;
    private int deflautTopPadding = 0;
    private boolean isFlush = false, isLoadMore = false;
    private Context mContext;
    private ImageView mHeaderIv;
    private TextView mHeaderTv, mFooterTv;
    private ProgressBar mHeaderPb, mFooterPb;
    private RelativeLayout mHeader, mFooter;

    public ReflushRecyclerView(Context context) {
        this(context, null);
    }

    public ReflushRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReflushRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        deflautTopPadding = DisplayUtil.dip2px(context, -30);
        topPadding = deflautTopPadding;
        setPadding(0, topPadding, 0, 0);

        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //第一个view是否可见
                position = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0));
                int visibleChildCount = getLayoutManager().getChildCount();
                if (visibleChildCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE && !isLoadMore && !isFlush && !isNotFinish) {//滚动到底部
                    //获取最后一个 view 除加载更多view
                    View lastVisibleView = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
                    //返回给定子视图的适配器位置
                    int lastVisiblePosition = recyclerView.getChildLayoutPosition(lastVisibleView);

                    //如果 最后一个可见的位置 大于等于
                    if (lastVisiblePosition >= getLayoutManager().getItemCount() - 1) {
                        //加载更多view 可见
                        setFooterVisible(true);
                        isLoadMore = true;
                        mLoadDataLisener.loadMoreDatas();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isFirstScroll) {
                    isFirstScroll = true;
                    setFooterVisible(false);
                }

            }
        });

        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                initLayout();
            }
        });

    }

    private boolean isFirstScroll = false;//第一次进入滚动

    private void setFooterVisible(boolean isVisible) {
        if (mFooter == null) {
            initLayout();
            if (mFooter == null) return;
        }
        if (isVisible) {
            mFooter.setVisibility(View.VISIBLE);
//            mFooterTv.setText("正在加载...");
//            mFooterPb.setVisibility(View.VISIBLE);
        } else {
            mFooter.setVisibility(View.GONE);
//            mFooterPb.setVisibility(View.GONE);
//            mFooterTv.setText("上拉加载更多");
        }
    }

    private void initLayout() {
        mHeaderIv = (ImageView) findViewById(R.id.header_iv);
        mHeaderTv = (TextView) findViewById(R.id.header_tv);
        mHeaderPb = (ProgressBar) findViewById(R.id.header_pb);
        mFooterTv = (TextView) findViewById(R.id.footer_tv);
        mFooterPb = (ProgressBar) findViewById(R.id.footer_pb);
        mHeader = (RelativeLayout) findViewById(R.id.header);
        mFooter = (RelativeLayout) findViewById(R.id.footer);
    }

    private int position = 0;//当前第一个可见view的position

    private boolean isNotFinish = true;//是否是下拉距离不够回到起点 此时不能让加载更多生效
    private int lasty = 0, y = 0;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        y = (int) e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (lasty == 0) {
                    lasty = y;
                }
                if (topPadding >= deflautTopPadding && position == 0) {
                    topPadding = topPadding + y - lasty;
                    setPadding(0, topPadding, 0, 0);
                    measureState();
                } else {
//                    topPadding =deflautTopPadding;
//                    setPadding(0,topPadding,0,0);
                }
//                if(topPadding < deflautTopPadding){
//                    topPadding = deflautTopPadding;
//                    setPadding(0,topPadding,0,0);
//                }
                lasty = y;
                break;
            case MotionEvent.ACTION_UP:
                lasty = 0;
                if (isFlush || isLoadMore) {
                    topPadding = deflautTopPadding;
                    setPadding(0, topPadding, 0, 0);
                    break;
                }
                if (topPadding > deflautTopPadding && topPadding < Math.abs(deflautTopPadding * 3 / 2) && !isFlush) {
                    topPadding = deflautTopPadding;
                    setPadding(0, topPadding, 0, 0);
                    isNotFinish = true;
                } else if (topPadding >= Math.abs(deflautTopPadding * 3 / 2)) {
                    topPadding = Math.abs(deflautTopPadding / 2);
                    setPadding(0, topPadding, 0, 0);
                    if (mHeaderIv == null) break;
                    mHeaderIv.setVisibility(View.GONE);
                    mHeaderPb.setVisibility(View.VISIBLE);
                    mHeaderTv.setText("正在加载...");
                    isFlush = true;
                    isNotFinish = false;
                    mLoadDataLisener.reflushDatas();
                } else {
                    topPadding = deflautTopPadding;
                    setPadding(0, topPadding, 0, 0);
                    isNotFinish = false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                topPadding = deflautTopPadding;
                setPadding(0, topPadding, 0, 0);
                isFlush = false;
                isLoadMore = false;
                isNotFinish = false;
                lasty = 0;
                break;
            default:
                break;
        }
        return super.onTouchEvent(e);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        try {
            super.onLayout(changed, l, t, r, b);
            initLayout();
        } catch (Exception e) {
        }
    }

    public void reflushComplete() {
        isFlush = false;
        topPadding = deflautTopPadding;
        setPadding(0, topPadding, 0, 0);

        isFirstScroll = false;
    }

    public void loadMoreComplete() {
        isLoadMore = false;
        setFooterVisible(false);
    }

    private void measureState() {
        if (isFlush) return;
        if (topPadding >= Math.abs(deflautTopPadding / 2) && topPadding <= Math.abs(deflautTopPadding * 3 / 2)) {//下拉超过一半
            if (mHeaderIv == null) return;
            mHeaderIv.setImageResource(R.mipmap.ic_pulltorefresh_arrow);
        } else if (topPadding >= Math.abs(deflautTopPadding * 3 / 2)) {
            if (mHeaderTv == null) return;
            mHeaderTv.setText("松开刷新");
        } else if (topPadding > deflautTopPadding && topPadding < Math.abs(deflautTopPadding / 2)) {
            if (mHeaderTv == null) return;
            mHeaderTv.setText("下拉刷新");
            mHeaderIv.setVisibility(View.VISIBLE);
            mHeaderPb.setVisibility(View.GONE);
            mHeaderIv.setImageResource(R.mipmap.ic_pulltorefresh_down);
        }
    }

    public interface LoadDataLisener {
        public void reflushDatas();

        public void loadMoreDatas();
    }

    private LoadDataLisener mLoadDataLisener;

    public void setLoadDataLisener(LoadDataLisener l) {
        this.mLoadDataLisener = l;
    }
}
