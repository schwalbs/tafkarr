package edu.rose_hulman.tafkarr;

import java.util.ArrayList;

/**
 * Created by andrewca on 1/17/2015.
 */
public class CourseCategory {
    private String mTitle;
    private double mWeight;
    private ArrayList<Assignment> mAssignments;

    public ArrayList<Assignment> getmAssignments() {
        return mAssignments;
    }

    public void setmAssignments(ArrayList<Assignment> mAssignments) {
        this.mAssignments = mAssignments;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public double getmWeight() {
        return mWeight;
    }

    public void setmWeight(double mWeight) {
        this.mWeight = mWeight;
    }

    public double getCategoryGrade() {
        return 0.0;
    }

}
