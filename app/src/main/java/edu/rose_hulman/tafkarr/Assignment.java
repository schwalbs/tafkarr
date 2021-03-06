package edu.rose_hulman.tafkarr;

/**
 * Created by andrewca on 1/17/2015.
 */
public class Assignment {
    private String mTitle;
    private double mGrade;
    private long mId;
    private String mCatName;

    public Assignment(String s, double i) {
        mTitle = s;
        mGrade = i;
    }

    public String getCatId() {
        return mCatName;
    }

    public void setCatId(String mCatId) {
        this.mCatName = mCatId;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public double getGrade() {
        return mGrade;
    }

    public void setGrade(double mGrade) {
        this.mGrade = mGrade;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

}
