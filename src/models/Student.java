package models;
import utils.ArrayUtils;
import utils.Checkers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 16/06/2016.
 * @author Ricardo Fernández Ameijeiras
 * @author Iago Pallares Tato
 */
@SuppressWarnings("unused")
public class Student extends Person
{
    private String                   mEntryDate; //dd/mm/aaaa
    private ArrayList<PassedSubject> mPassedSubjects;
    private ArrayList<SubjectInfo>   mSubjectsTaken;

    public Student(String name, String surname, String dni, String birthdayDate, String entryDate, ArrayList<PassedSubject> passedSubjects, ArrayList<SubjectInfo> subjectsTaken)
    {
        super(name, surname, dni, birthdayDate);
        mEntryDate      = entryDate;
        mPassedSubjects = passedSubjects != null ? passedSubjects : new ArrayList<>();
        mSubjectsTaken  = subjectsTaken  != null ? subjectsTaken  : new ArrayList<>();
    }

    public String getEntryDate() {
        return mEntryDate;
    }

    public ArrayList<PassedSubject> getPassedSubjects() {
        return mPassedSubjects;
    }

    public void addPassedSubject(PassedSubject passedSubject)
    {
        mPassedSubjects.add(passedSubject);
    }

    public ArrayList<SubjectInfo> getSubjectsTaken() {
        return mSubjectsTaken;
    }

    public void addSubjectTaken(SubjectInfo subjectTaken)
    {
        mSubjectsTaken.add(subjectTaken);
    }

    public void setEntryDate(String entryDate) {
        mEntryDate = entryDate;
    }

    public void setPassedSubjects(ArrayList<PassedSubject> passedSubjects) {
        mPassedSubjects = passedSubjects;
    }

    public void setSubjectsTaken(ArrayList<SubjectInfo> subjectsTaken) {
        mSubjectsTaken = subjectsTaken;
    }

    private static boolean isStudentValid(List<String> studentParameters)
    {
        if (!Person.isPersonValid(studentParameters)) return false;

        if (!(Checkers.checkDate(studentParameters.get(5)))) return false;

        List<String> passedSubjects = Arrays.asList(studentParameters.get(6).split(";"));
        for (String passedSubject : passedSubjects)
        {
            if (passedSubject.isEmpty()) continue;
            if (!PassedSubject.isPassedSubjectValid(ArrayUtils.stringToStringListTrimmed(passedSubject))) return false;
        }

        List<String> subjectsInfo = Arrays.asList(studentParameters.get(7).split(";"));
        for (String subjectInfo : subjectsInfo)
        {
            if (subjectInfo.isEmpty()) continue;
            if (!SubjectInfo.isSubjectInfoValid(ArrayUtils.stringToStringListTrimmed(subjectInfo), false)) return false;
        }

        return true;
    }

    public static Student parseStudent(List<String> studentParameters)
    {
        if (!isStudentValid(studentParameters)) return null;

        Student student = new Student(studentParameters.get(2),
                                      studentParameters.get(3),
                                      studentParameters.get(1),
                                      studentParameters.get(4),
                                      studentParameters.get(5),
                                      null,
                                      null);

        List<String> passedSubjects = Arrays.asList(studentParameters.get(6).split(";"));
        for (String passedSubject : passedSubjects)
        {
            if (passedSubject.isEmpty()) continue;
            student.addPassedSubject(PassedSubject.parsePassedSubject(ArrayUtils.stringToStringListTrimmed(passedSubject)));
        }

        List<String> subjectsInfo = Arrays.asList(studentParameters.get(7).split(";"));
        for (String subjectInfo : subjectsInfo)
        {
            if (subjectInfo.isEmpty()) continue;
            student.addSubjectTaken(SubjectInfo.parseSubjectInfo(ArrayUtils.stringToStringListTrimmed(subjectInfo), false));
        }

        return student;
    }

    @Override
    public String dumpAsDbFormat()
    {
        String str = "";

        str += "alumno"               + "\n";
        str += super.dumpAsDbFormat() + "\n";
        str += mEntryDate             + "\n";

        if (mPassedSubjects.size() == 0) str += "\n";
        else
        {
            for (PassedSubject passedSubject : mPassedSubjects) str += passedSubject.dumpAsDbFormat() + "; ";
            str = str.substring(0, str.length() - 2);
            str += "\n";
        }

        if (mSubjectsTaken.size() == 0) str += "\n*";
        else
        {
            for (SubjectInfo subjectInfo : mSubjectsTaken) str += subjectInfo.dumpAsDbFormat() + "; ";
            str = str.substring(0, str.length() - 2);
            str += "\n*";
        }

        return str;
    }

    public static class PassedSubject
    {
        private int    mSubjectId;
        private String mAcademicGrade; //aa/aa
        private float  mMark; //5-10

        public PassedSubject(int subjectId, String academicGrade, float mark)
        {
            mSubjectId     = subjectId;
            mAcademicGrade = academicGrade;
            mMark          = mark;
        }

        public int getSubjectId()
        {
            return mSubjectId;
        }

        public String getAcademicGrade()
        {
            return mAcademicGrade;
        }

        public float getMark()
        {
            return mMark;
        }

        public void setSubjectId(int subjectId) {
            mSubjectId = subjectId;
        }

        public void setAcademicGrade(String academicGrade) {
            mAcademicGrade = academicGrade;
        }

        public void setMark(float mark) {
            mMark = mark;
        }

        private static boolean isPassedSubjectValid(List<String> passedSubjectParameters)
        {
            if (!((Integer.parseInt(passedSubjectParameters.get(0)) >= 0) &&
                    (Float.parseFloat(passedSubjectParameters.get(2)) >= 5 && Float.parseFloat(passedSubjectParameters.get(2)) <= 10))) return false;

            List<String> academicGrade = Arrays.asList(passedSubjectParameters.get(1).split("/"));

            if (academicGrade.size() == 2)
            {
                int firstYear  = Integer.parseInt(academicGrade.get(0));
                int secondYear = Integer.parseInt(academicGrade.get(1));

                return (firstYear + 1 == secondYear);
            }

            return true;
        }

        public static PassedSubject parsePassedSubject(List<String> passedSubjectParameters)
        {
            if (!isPassedSubjectValid(passedSubjectParameters)) return null;

            return new PassedSubject(Integer.parseInt(passedSubjectParameters.get(0)),
                    passedSubjectParameters.get(1),
                    Float.parseFloat(passedSubjectParameters.get(2)));
        }

        public String dumpAsDbFormat()
        {
            String str = "";

            str += mSubjectId                                   + " ";
            str += mAcademicGrade == null ? "" : mAcademicGrade + " ";
            str += mMark          == -1   ? "" : mMark;

            return str;
        }
    }
}
