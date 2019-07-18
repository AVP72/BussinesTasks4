package com.babyartsoft.bussinestasks1;

import android.content.Context;
import android.content.SharedPreferences;

import com.babyartsoft.bussinestasks1.Interface.Constant;

class Setting implements Constant {

    SharedPreferences sp;
    private int sortLiesTask;
    private boolean arrowListTask;
    private String strSort;
    private int filterStatusHead;
    private int positionListTask;
    private int offsetPositionListTask;
    private long filterSrokBegin, filterSrokEnd;
    private String filterSrokString;
    private String filterSlave;
    private String filterSlaveString;
    private int filterDatePeriod;

    Setting(Context context) {
        sp = context.getSharedPreferences(FILE_SETTING, Context.MODE_PRIVATE);
        getSettingFromFile(context);
    }

    public void getSettingFromFile(Context context) {
        sortLiesTask = sp.getInt(POLE_SORT, SORT_INTO);
        strSort = sp.getString(STRING_SORT_NAME, context.getString(R.string.sort_by_into));
        arrowListTask = sp.getBoolean(ARROW_SORT, false);
        filterStatusHead = sp.getInt(FILTER_STATUS, FILTER_STATUS_OFF);
        filterSrokBegin = sp.getLong(FILTER_SROK_BEGIN, Long.MIN_VALUE);
        filterSrokEnd = sp.getLong(FILTER_SROK_END, Long.MAX_VALUE);
        String start = context.getString(R.string.periodStart);
        filterSrokString = sp.getString(FILTER_SROK_STRING, start);
        filterSlave = sp.getString(FILTER_SLAVE, FILTER_SLAVE_ALL);
        filterSlaveString = sp.getString(FILTER_SLAVE_STRING, context.getString(R.string.allSlave));
        filterDatePeriod = sp.getInt(FILTER_DATE_PERIOD, FILTER_DATE_NOT);
    }

    public void setSettingToFile(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(POLE_SORT, sortLiesTask);
        editor.putString(STRING_SORT_NAME, strSort);
        editor.putBoolean(ARROW_SORT, arrowListTask);
        editor.putInt(FILTER_STATUS, filterStatusHead);
        editor.putLong(FILTER_SROK_BEGIN, filterSrokBegin);
        editor.putLong(FILTER_SROK_END, filterSrokEnd);
        editor.putString(FILTER_SROK_STRING, filterSrokString);
        editor.putString(FILTER_SLAVE, filterSlave);
        editor.putString(FILTER_SLAVE_STRING, filterSlaveString);
        editor.putInt(FILTER_DATE_PERIOD, filterDatePeriod);
        editor.apply();
    }

    public int getSortLiesTask() {
        return sortLiesTask;
    }

    public void setSortLiesTask(int sortLiesTask) {
        this.sortLiesTask = sortLiesTask;
    }

    public boolean isArrowListTask() {
        return arrowListTask;
    }

    public void setArrowListTask(boolean arrowListTask) {
        this.arrowListTask = arrowListTask;
    }

    public String getStrSort() {
        return strSort;
    }

    public void setStrSort(String strSort) {
        this.strSort = strSort;
    }

    public int getFilterStatusHead() {
        return filterStatusHead;
    }

    public void setFilterStatusHead(int filterStatusHead) {
        this.filterStatusHead = filterStatusHead;
    }

    public int getPositionListTask() {
        return positionListTask;
    }

    public void setPositionListTask(int positionListTask) {
        this.positionListTask = positionListTask;
    }

    public int getOffsetPositionListTask() {
        return offsetPositionListTask;
    }

    public void setOffsetPositionListTask(int offsetPositionListTask) {
        this.offsetPositionListTask = offsetPositionListTask;
    }

    public long getFilterSrokBegin() {
        return filterSrokBegin;
    }

    public void setFilterSrokBegin(long filterSrokBegin) {
        this.filterSrokBegin = filterSrokBegin;
    }

    public long getFilterSrokEnd() {
        return filterSrokEnd;
    }

    public void setFilterSrokEnd(long filterSrokEnd) {
        this.filterSrokEnd = filterSrokEnd;
    }

    public String getFilterSrokString() {
        return filterSrokString;
    }

    public void setFilterSrokString(String filterSrokString) {
        this.filterSrokString = filterSrokString;
    }

    public String getFilterSlave() {
        return filterSlave;
    }

    public void setFilterSlave(String filterSlave) {
        this.filterSlave = filterSlave;
    }

    public String getFilterSlaveString() {
        return filterSlaveString;
    }

    public void setFilterSlaveString(String filterSlaveString) {
        this.filterSlaveString = filterSlaveString;
    }

    public int getFilterDatePeriod() {
        return filterDatePeriod;
    }

    public void setFilterDatePeriod(int filterDatePeriod) {
        this.filterDatePeriod = filterDatePeriod;
    }
}
