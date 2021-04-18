package models;

/**
 * Created by Strinnityk on 23/06/2016.
 */
public class StudentRecord
{
    private int mGrade;
    private String mName;
    private float mMark;
    private String mAcademicGrade;

    public StudentRecord(int mGrade, String mName, float mMark, String mAcademicGrade)
    {
        this.mGrade = mGrade;
        this.mName = mName;
        this.mMark = mMark;
        this.mAcademicGrade = mAcademicGrade;
    }

    public int getmGrade() {
        return mGrade;
    }

    public void setmGrade(int mGrade) {
        this.mGrade = mGrade;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public float getmMark() {
        return mMark;
    }

    public void setmMark(float mMark) {
        this.mMark = mMark;
    }

    public String getmAcademicGrade() {
        return mAcademicGrade;
    }

    public void setmAcademicGrade(String mAcademicGrade) {
        this.mAcademicGrade = mAcademicGrade;
    }
}
