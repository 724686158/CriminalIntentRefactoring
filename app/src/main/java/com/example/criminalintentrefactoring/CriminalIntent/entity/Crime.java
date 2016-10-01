package com.example.criminalintentrefactoring.CriminalIntent.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by 离子态狍子 on 2016/9/29.
 */

public class Crime implements Serializable{
    /**
     * 用于识别的唯一id
     */
    private UUID mId;
    /**
     * 标题
     */
    private String mTitle;
    /**
     * 日期
     */
    private Date mDate;
    /**
     * 是否解决
     */
    private boolean mSolved;
    /**
     * 嫌疑人
     */
    private String mSuspect;

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID id) {

        mId = id;
        mTitle = "";
        mDate = new Date();
        mSolved = false;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

}
