package edu.rose_hulman.tafkarr;

/**
 * Created by andrewca on 1/17/2015.
 */
public class Assignment {
    private String mTitle;
    private double mGrade;

    public Assignment(String s, int i) {
        mTitle = s ;
        mGrade = i;
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
