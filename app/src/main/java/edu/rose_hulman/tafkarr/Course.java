package edu.rose_hulman.tafkarr;

import java.util.ArrayList;

/**
 * Created by andrewca on 1/17/2015.
 */
public class Course {
    private String mTitle;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public double getmTargetGrade() {
        return mTargetGrade;
    }

    public void setmTargetGrade(double mTargetGrade) {
        this.mTargetGrade = mTargetGrade;
    }

    public double getCourseGrade() {
        return 0.0;
    }

    public ArrayList<CourseCategory> getmCategories() {
        return mCategories;
    }

    public void setmCategories(ArrayList<CourseCategory> mCategories) {
        this.mCategories = mCategories;
    }
    private double mTargetGrade;
    private ArrayList<CourseCategory> mCategories;

}
