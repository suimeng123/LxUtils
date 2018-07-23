package com.lx.utils.model;

/**
 * Created by lixiao2 on 2018/3/15.
 */
public class DateInfo {
    private int day;//日期
    private boolean isThisMonth;//是否属于当前月
    private int month;//当前月份
    private int year;//当前年份
    private boolean isToday;//是否是今天
    private boolean isSelected;//是否被用户选中


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isThisMonth() {
        return isThisMonth;
    }

    public void setThisMonth(boolean thisMonth) {
        isThisMonth = thisMonth;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }
}
