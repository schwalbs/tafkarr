package edu.rose_hulman.tafkarr;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by andrewca on 1/17/2015.
 */
public class Course implements Parcelable {
    private String mTitle;
    private double mTargetGrade;
    private double mCourseGrade;
    private long mId;

    public Course() {
    }

    public Course(String title, double targetGrade, double courseGrade) {
        mTitle = title;
        mTargetGrade = targetGrade;
        mCourseGrade = courseGrade;
    }

    public Course(Parcel source) {
        mTitle = source.readString();
        mTargetGrade = source.readDouble();
        mCourseGrade = source.readDouble();
    }

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

    public void setCourseGrade(double grade) {
        mCourseGrade = grade;
    }

    public long getId() {
        return mId;
    }

    public void setId(long newId) {
        mId = newId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeDouble(mTargetGrade);
        dest.writeDouble(mCourseGrade);
    }

    public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {

        @Override
        public Course createFromParcel(Parcel source) {
            return new Course(source);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };
}
