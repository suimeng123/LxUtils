package com.lx.utils.util;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.lx.utils.adapter.LeftDeleteRecycleViewAdapter;

/**
 * Created by lixiao2 on 2018/7/12.
 */

public class LeftDeleteItemTouchHelperCallback extends ItemTouchHelper.Callback  {

    LeftDeleteRecycleViewAdapter mAdapter;

    public LeftDeleteItemTouchHelperCallback(LeftDeleteRecycleViewAdapter adapter){
        this.mAdapter = adapter;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; //允许上下的拖动
        //int dragFlags =ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT; //允许左右的拖动
        //int swipeFlags = ItemTouchHelper.LEFT; //只允许从右向左侧滑
        //int swipeFlags = ItemTouchHelper.DOWN; //只允许从上向下侧滑
        //一般使用makeMovementFlags(int,int)或makeFlag(int, int)来构造我们的返回值
        //makeMovementFlags(dragFlags, swipeFlags)

        int flag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        // 第一个参数是拖拽的参数  第二参数是侧滑参数 ； 0是不可拖拽或者不可滑动
        return makeMovementFlags(flag, flag);
    }

    private boolean isOpen = false;
    int maxDx = 0;

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            //仅对侧滑状态下的效果做出改变
            int btnWidth = ((LeftDeleteRecycleViewAdapter.ViewHolder)viewHolder).btn.getMeasuredWidth();
            Log.i("Lx", "btnWidth:"+btnWidth+";dX=" + dX  + ";"  + isCurrentlyActive);


            if(Math.abs(dX) >= btnWidth / 2) {
                viewHolder.itemView.scrollTo(btnWidth,0);
                isOpen = true;
            }

//            if(Math.abs(dX)>=btnWidth){
//                dX = -btnWidth;
//            }
//            if(isCurrentlyActive) {
//                viewHolder.itemView.scrollTo((int) -dX, 0);
//            }else{
//                if(Math.abs(dX) >= btnWidth / 2) {
//                    viewHolder.itemView.scrollTo(btnWidth,0);
//                    isOpen = true;
//                } else {
//                    viewHolder.itemView.scrollTo(0,0);
//                    isOpen = false;
//                }
//            }
        }else{
            //拖拽状态下不做改变，需要调用父类的方法
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setScrollX(0);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder,target);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDissmiss(viewHolder);
    }
}
