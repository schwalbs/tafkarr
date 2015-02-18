package edu.rose_hulman.tafkarr;

import java.util.ArrayList;

/**
 * Created by andrewca on 1/17/2015.
 */
public class User {
    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public ArrayList<Course> getmCourses() {
        return mCourses;
    }

    public void setmCourses(ArrayList<Course> mCourses) {
        this.mCourses = mCourses;
    }

    private String mEmail;
    private String mPassword;
    private ArrayList<Course> mCourses;


}
