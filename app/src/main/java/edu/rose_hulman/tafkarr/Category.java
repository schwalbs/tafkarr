package edu.rose_hulman.tafkarr;

/**
 * Created by andrewca on 1/17/2015.
 */
public class Category {
    private String mTitle;
    private double mWeight;
    private long mId;
    private long mClassId;

    public Category(String s, double d, long l) {
        mTitle = s ;
        mClassId = l;
        mWeight = d;
    }
    public double getWeight(){
        return mWeight;
    }
    public void setWeight(double newWeight){
        this.mWeight = newWeight;
    }

    public long getClassId() {
        return mClassId;
    }

    public void setClassId(long mClassId) {
        this.mClassId = mClassId;
    }
    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

}
