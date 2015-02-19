package edu.rose_hulman.tafkarr;

import java.util.ArrayList;

/**
 * Created by andrewca on 1/17/2015.
 */
public class Course {
    private String mTitle;
    private double mTargetGrade;
    private ArrayList<CourseCategory> mCategories;
    private double mCourseGrade;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public double getTargetGrade() {
        return mTargetGrade;
    }

    public void setTargetGrade(double mTargetGrade) {
        this.mTargetGrade = mTargetGrade;
    }

    public double getCourseGrade() {
        return mCourseGrade;
    }
    public void setCourseGrade(double grade){
        mCourseGrade= grade;
    }

    public ArrayList<CourseCategory> getmCategories() {
        return mCategories;
    }

    public void setCategories(ArrayList<CourseCategory> mCategories) {
        this.mCategories = mCategories;
    }
}
