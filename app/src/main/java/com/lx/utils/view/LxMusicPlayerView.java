package com.lx.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import android.widget.SeekBar;

import com.lx.utils.R;
import com.lx.utils.model.LxLrc;
import com.lx.utils.util.CommUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lixiao2 on 2018/6/21.
 */

public class LxMusicPlayerView extends View {
    private List<LxLrc> lists = null;
    private Context mContext;
    private InputStream lrcInputStream = null;
    private InputStream mediaInputStream = null;
    private String mediaName = "超级英雄.mp3";
    /**
     * 歌词间距
     */
    private int offsetY = 40;
    /**
     * 歌词颜色
     */
    private final static String LRC_NORMAL_COLOR = "#333333";
    /**
     * 选中时歌词颜色
     */
    private final static String LRC_SELECT_COLOR = "#ff0000";
    /**
     * 歌词字体大小
     */
    private final static int LRC_TEXT_SIZE = R.dimen.font_18;
    /**
     * 选中歌词字体大小
     */
    private final static int LRC_SELECT_TEXT_SIZE = R.dimen.font_20;
    /**
     * 缓存用过的StaticLayout
     */
    private HashMap<String, StaticLayout> sMap = new HashMap<>();
    /**
     * 缓存用过的TextPaint
     */
    private HashMap<String, TextPaint> tMap = new HashMap<>();
    /**
     * 每行歌词正则
     */
    private final static String LRC_PATTERN = "(([\\d{2}:\\d{2}.\\d{2}])+)(.*)";
    //最大高度
    private int maxHeight = 0;
    // 每次触摸每根手指都会拥有一个固定PointerId
    private int mPointerId;
    /**
     * 用于完成滚动操作的实例
     */
    private Scroller mScroller;
    /**
     * 判定为拖动的最小移动像素数
     */
    private int mTouchSlop;

    private int mMaxVelocity;//最大速度
    // 速度计算器
    private VelocityTracker mVelocityTracker;

    private MediaPlayer mediaPlayer;

    private Timer timer;

    private long musicTime = 0;

    /**
     * 当前播放到的歌词
     */
    private LxLrc nowLrc = new LxLrc();

    private SeekBar seekBar ;

    public LxMusicPlayerView(Context context) {
        this(context, null);
    }

    public LxMusicPlayerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LxMusicPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LxMusicPlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        // 第一步，创建Scroller的实例
        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获取TouchSlop值
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        // 滚动速度计算器
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
//        init();
    }

    public void setSeekBar(SeekBar sb) {
        seekBar = sb;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                nowCurrentPosition = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mediaPlayer!=null){
                    mediaPlayer.seekTo(nowCurrentPosition);
                    mediaPlayer.start();
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    public void start() {
        initList();
        initMusic();
        if(seekBar != null) {
            seekBar.setProgress(0);
        }
    }

    /**
     * 初始化音乐播放器
     */
    private void initMusic() {
        if(timer != null) {
            return;
        }
        timer = new Timer();
        mediaPlayer = new MediaPlayer();
        try {
            File file = new File(CommUtils.getSDCardUrl()+File.separator+mediaName);
            if(!file.exists()) {
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                InputStream in = mediaInputStream;
                byte[] bytes = new byte[1024*128];
                int len;
                while ((len = in.read(bytes)) != -1) {
                    out.write(bytes);
                }
                in.close();
                out.close();
            }
            mediaPlayer.setDataSource(CommUtils.getSDCardUrl()+File.separator+mediaName);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 通过异步的方式装载媒体资源
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
//                    recycleMediapLayer();
//                    if(timer!=null){
//                        timer.cancel();
//                        timer.purge();
//                    }
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 装载完毕回调
                    mediaPlayer.start();
                    musicTime = mediaPlayer.getDuration();
                    seekBar.setMax((int) (musicTime));
                }
            });
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
//                    Log.i("Lx", "threadName:"+Thread.currentThread().getName());
                    if(mediaPlayer == null || !mediaPlayer.isPlaying()){
                        return;
                    }
                    int position = mediaPlayer.getCurrentPosition();
                    if(seekBar != null){
                        seekBar.setProgress(position);
                    }
                    calculateTime(position/1000);
                    for(int i = 0; i < lists.size(); i++ ) {
                        StaticLayout layout = sMap.get(lists.get(i).getContent()+i);
//                        if(i <= selectI){
//                            if(i < selectI) {
//                                layout.getPaint().setColor(Color.parseColor(LRC_NORMAL_COLOR));
//                                layout.getPaint().setTextSize(mContext.getResources().getDimension(LRC_TEXT_SIZE));
//                                postInvalidate();
//                            }
//                            continue;
//                        }
                        if(layout != null) {
                            if(i < lists.size()-1){
                                if((nowLrc.getMin() * 60 + nowLrc.getSecond()) >= (lists.get(i).getMin() * 60 + lists.get(i).getSecond()) &&(nowLrc.getMin() * 60 + nowLrc.getSecond()) < (lists.get(i+1).getMin() * 60 + lists.get(i+1).getSecond())){
                                    layout.getPaint().setColor(Color.parseColor(LRC_SELECT_COLOR));
                                     layout.getPaint().setTextSize(mContext.getResources().getDimension(LRC_SELECT_TEXT_SIZE));
                                    autoScrollPosition(lists.get(i));
                                    selectI = i;
                                }else {
                                    layout.getPaint().setColor(Color.parseColor(LRC_NORMAL_COLOR));
                                    layout.getPaint().setTextSize(mContext.getResources().getDimension(LRC_TEXT_SIZE));
                                }
                                postInvalidate();
                            }else {
                                if((nowLrc.getMin() * 60 + nowLrc.getSecond()) >= (lists.get(i).getMin() * 60 + lists.get(i).getSecond())){
                                    layout.getPaint().setColor(Color.parseColor(LRC_SELECT_COLOR));
                                    layout.getPaint().setTextSize(mContext.getResources().getDimension(LRC_SELECT_TEXT_SIZE));
                                    postInvalidate();
                                    selectI = i;
                                    autoScrollPosition(lists.get(i));
                                }else {
                                    layout.getPaint().setColor(Color.parseColor(LRC_NORMAL_COLOR));
                                    layout.getPaint().setTextSize(mContext.getResources().getDimension(LRC_TEXT_SIZE));
                                }
                                postInvalidate();
                            }
                        }
                    }
                }
            },0,200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int selectI = -1;

    private void autoScrollPosition(LxLrc lxLrc) {
        if(lxLrc.getPosition() < getHeight()/2){
            // 小于一半屏幕的歌词 滚动到顶部
            mScroller.startScroll(0, getScrollY(), 0, - getScrollY());
        }else if(lxLrc.getPosition() > maxHeight - getHeight()/2){
            // 最底部小于屏幕一半的歌词，滚动到底部
            mScroller.startScroll(0, getScrollY(), 0, maxHeight - getHeight() - getScrollY());
        }else{
            mScroller.startScroll(0, getScrollY(), 0, lxLrc.getPosition() - getHeight()/2 - getScrollY());
        }
    }

    private int nowCurrentPosition = 0;
    public boolean pause() {
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            nowCurrentPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            return true;
        }
        return false;
    }

    public boolean reStart() {
        if(mediaPlayer != null) {
            mediaPlayer.seekTo(nowCurrentPosition);
            mediaPlayer.start();
            return true;
        }
        return false;
    }

    /**
     * 初始化歌词列表
     */
    private void initList() {
        lists = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(lrcInputStream,"utf-8"));
            String readLineStr = "";
            while ((readLineStr = reader.readLine()) != null) {
                LxLrc lxLrc = matcheLrc(readLineStr);
                if (lxLrc != null) {
                    lists.add(lxLrc);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 每行歌词转换为lxlrc对象
     * lineStr: 每行的歌词
     *
     * @return 每行歌词信息
     */
    private LxLrc matcheLrc(String lineStr) {
        if (TextUtils.isEmpty(lineStr)) {
            return null;
        }
        LxLrc lrc = new LxLrc();
        if (lineStr.contains("[ti:") || lineStr.contains("[ar:") || lineStr.contains("[al:")|| lineStr.contains("[by:")) {
            lrc.setContent(lineStr.substring(4, lineStr.length() - 1).replace(":", ""));
            lrc.setLrc(false);
            return lrc;
        }
        Pattern p = Pattern.compile(LRC_PATTERN);
        Matcher m = p.matcher(lineStr);
        while (m.find()) {
            String time = m.group(1);
            if (!TextUtils.isEmpty(time) && time.length() >= 8) {
                String[] strs = time.split(":");
                String sec = strs[1];
                lrc.setMin(Integer.parseInt(strs[0]));
                lrc.setSecond(Integer.parseInt(sec.substring(0, 2)));
                lrc.setMm(Integer.parseInt(sec.substring(3, sec.length())));
            }
            String content = m.group(3).replace("]", "");
            lrc.setContent(content);
            lrc.setTime(time);
            lrc.setLrc(true);
        }
        return lrc;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLrc(canvas);
    }

    /**
     * 将歌词画出来
     */
    private void drawLrc(Canvas canvas) {
        int y = offsetY;
        for (int i = 0; i < lists.size(); i++) {
            LxLrc item = lists.get(i);
            StaticLayout staticLayout = sMap.get(item.getContent()+i);
            TextPaint textPaint = tMap.get(item.getContent()+i);
            if (staticLayout == null) {
                if(textPaint == null) {
                    textPaint = new TextPaint();
                    textPaint.setTextSize(mContext.getResources().getDimension(LRC_TEXT_SIZE));
                    textPaint.setColor(Color.parseColor(LRC_NORMAL_COLOR));
                    textPaint.setAntiAlias(true);
                }
                staticLayout = new StaticLayout(item.getContent(), textPaint, getWidth(), Layout.Alignment.ALIGN_CENTER, 1, 0, false);
                sMap.put(item.getContent()+i, staticLayout);
                tMap.put(item.getContent()+i,textPaint);
            }
            item.setHeight(staticLayout.getHeight());
            item.setPosition(y);
//            setSelectColor(staticLayout,y);
            canvas.save();
            canvas.translate(0, y);
            staticLayout.draw(canvas);
            canvas.restore();
            y += staticLayout.getHeight() + offsetY;
        }
        maxHeight = y;
    }

    /**
     * 设置正中间位置为歌词选中位置
     * @param y
     */
    private void setSelectColor(StaticLayout staticLayout,int y) {
        if((y <= getHeight()/2 + mScroller.getCurrY()) && (getHeight()/2 + mScroller.getCurrY() <= y + staticLayout.getHeight() + offsetY)){
            staticLayout.getPaint().setColor(Color.parseColor(LRC_SELECT_COLOR));
        }else{
            staticLayout.getPaint().setColor(Color.parseColor(LRC_NORMAL_COLOR));
        }
    }

    int downY;
    int moveY;
    // Y方向移动的距离
    int lastMoveY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        obtainVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 获取当前已经移动的距离
                lastMoveY = mScroller.getCurrY();
                downY = (int) event.getY() + lastMoveY;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mPointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = (int) event.getY();
                if(Math.abs(moveY - downY) < mTouchSlop){
                    break;
                }
                if(downY - moveY <= 0) {
                    // 向下滑动超过最小值时 增加阻力
                    moveY = downY - (downY - moveY)/3 ;
                }else if(downY - moveY >= maxHeight-getHeight()){
                    // 向上滑动超过最大值时 增加阻力
                    moveY = moveY + (downY - moveY - maxHeight + getHeight())*2/3;
                }
                scrollTo(0,downY - moveY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                moveY = (int) event.getY();
                lastMoveY = downY - moveY;
                if(lastMoveY <= 0 ) {
                    //向下滑动超过最小值时回到原点
                    lastMoveY = 0;
                    mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                    postInvalidate();
                }else if(lastMoveY >= maxHeight-getHeight()) {
                    //向上滑动超过最大值回到最大值位置
                    lastMoveY = maxHeight - getHeight();
                    mScroller.startScroll(0, getScrollY(), 0, maxHeight - getHeight() - getScrollY());
                    postInvalidate();
                }else {
                    // 在最小值和最大高度之间的惯性滑动
                    //计算1000ms的速度
                    mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                    //获取y在mPointerId上的的速度
                    final float velocityY = mVelocityTracker.getYVelocity(mPointerId);
                    // 滚动一段距离
                    mScroller.fling(0,getScrollY(),0,(int)-velocityY,0,0,0,maxHeight-getHeight()-100);
                    recycleVelocityTracker();
                    postInvalidate();
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        // 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
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

    /**
     * 释放播放器资源
     */
    private void recycleMediapLayer() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * 设置歌词和音乐文件信息
     * @param lrcIn 歌词输入流
     * @param mediaIn 音乐输入流
     * @param mName 音乐文件名称
     */
    public void setFileInfo(InputStream lrcIn,InputStream mediaIn,String mName){
        lrcInputStream = lrcIn;
        mediaInputStream = mediaIn;
        mediaName = mName;
    }

    // 获取音乐总时长
    public long getMusicTime() {
        return musicTime;
    }

    /**
     * 清空信息
     */
    public void onDestory() {
        if(timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        recycleMediapLayer();
        lrcInputStream = null;
        mediaInputStream = null;
        mediaName = null;
        lists.clear();
        sMap.clear();
        tMap.clear();
        selectI = -1;
    }

    /**
     * 计算当前播放时间
     */
    private void calculateTime(int time) {
        int min = 0;
        int sec = 0;
        if (time < 60){
            sec = time;
        }else {
            sec = time % 60;
            min = time/60;
        }
        nowLrc.setMin(min);
        nowLrc.setSecond(sec);
    }
}
