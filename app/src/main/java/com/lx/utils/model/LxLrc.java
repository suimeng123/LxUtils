package com.lx.utils.model;

/**
 * Created by lixiao2 on 2018/6/21.
 */

public class LxLrc {
    /**
     * 每一行的歌词
     */
    private String content;
    /**
     * 播放时间分
     */
    private int min;
    /**
     * 播放时间秒
     */
    private int second;
    /**
     * 播放时间毫秒
     */
    private int mm;

    /**
     * 判断是否是歌词
     */
    private boolean isLrc;

    /**
     * 时间
     */
    private String time;

    /**
     * 歌词顶部的位置
     */
    private int position;

    /**
     * 这行歌词个高度
     */
    private int height;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getMm() {
        return mm;
    }

    public void setMm(int mm) {
        this.mm = mm;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isLrc() {
        return isLrc;
    }

    public void setLrc(boolean lrc) {
        isLrc = lrc;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
