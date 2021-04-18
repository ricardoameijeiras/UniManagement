package models;

import utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 16/06/2016.
 * @author Ricardo Fernández Ameijeiras
 * @author Iago Pallares Tato
 */
@SuppressWarnings("unused")
public class Professor extends Person
{
    private Category               mCategory;
    private String                 mDepartment; //40
    private int                    mWeeklyTeachingHours; //20 titular, 15 asociado
    private ArrayList<SubjectInfo> mSubjectsImparted;

    public Professor(String name, String surname, String dni, String birthdayDate, Category category, String department, int weeklyTeachingHours, ArrayList<SubjectInfo> subjectsImparted)
    {
        super(name, surname, dni, birthdayDate);
        mCategory            = category;
        mDepartment          = department;
        mWeeklyTeachingHours = weeklyTeachingHours;
        mSubjectsImparted    = subjectsImparted != null ? subjectsImparted : new ArrayList<>();
    }

    public Category getCategory() {
        return mCategory;
    }

    public String getDepartment() {
        return mDepartment;
    }

    public int getWeeklyTeachingHours() {
        return mWeeklyTeachingHours;
    }

    public ArrayList<SubjectInfo> getSubjectsImparted() {
        return mSubjectsImparted;
    }

    public void addSubjectImparted(SubjectInfo subjectImparted)
    {
        mSubjectsImparted.add(subjectImparted);
    }

    public void setCategory(Category category) {
        mCategory = category;
    }

    public void setDepartment(String department) {
        mDepartment = department;
    }

    public void setWeeklyTeachingHours(int weeklyTeachingHours) {
        mWeeklyTeachingHours = weeklyTeachingHours;
    }

    public void setSubjectsImparted(ArrayList<SubjectInfo> subjectsImparted) {
        mSubjectsImparted = subjectsImparted;
    }

    public enum Category
    {
        Titular,
        Asociado;

        public static Category parseCategory(String str)
        {
            switch(str)
            {
                case "titular":
                    return Titular;
                case "asociado":
                    return Asociado;
                default:
                    return null;
            }
        }

        public static String parseString(Category category)
        {
            switch(category)
            {
                case Titular:
                    return "titular";
                case Asociado:
                    return "asociado";
                default:
                    return null;
            }
        }
    }

    private static boolean isProfessorValid(List<String> professorParameters)
    {
        if (!Person.isPersonValid(professorParameters)) return false;

        if (!((Category.parseCategory(professorParameters.get(5)) != null) &&
              (professorParameters.get(6).length() <= 40) &&
              (Integer.parseInt(professorParameters.get(7)) >= 0))) return false;

        List<String> subjectsInfo = Arrays.asList(professorParameters.get(8).split(";"));
        for (String subjectInfo : subjectsInfo)
        {
            if (subjectInfo.isEmpty()) continue;
            if (!SubjectInfo.isSubjectInfoValid(ArrayUtils.stringToStringListTrimmed(subjectInfo), true)) return false;
        }

        return true;
    }

    public static Professor parseProfessor(List<String> professorParameters)
    {
        if (!isProfessorValid(professorParameters)) return null;

        Professor professor = new Professor(professorParameters.get(2),
            professorParameters.get(3),
            professorParameters.get(1),
            professorParameters.get(4),
            Category.parseCategory(professorParameters.get(5)),
            professorParameters.get(6),
            Integer.parseInt(professorParameters.get(7)),
            null);

        List<String> subjectsInfo = Arrays.asList(professorParameters.get(8).split(";"));
        for (String subjectInfo : subjectsInfo)
        {
            if (subjectInfo.isEmpty()) continue;
            professor.addSubjectImparted(SubjectInfo.parseSubjectInfo(ArrayUtils.stringToStringListTrimmed(subjectInfo), true));
        }

        return professor;
    }

    @Override
    public String dumpAsDbFormat()
    {
        String str = "";

        str += "profesor"                      + "\n";
        str += super.dumpAsDbFormat()          + "\n";
        str += Category.parseString(mCategory) + "\n";
        str += mDepartment                     + "\n";
        str += mWeeklyTeachingHours            + "\n";


        if (mSubjectsImparted.size() == 0) str += "\n";
        else
        {
            for (SubjectInfo subjectInfo : mSubjectsImparted) str += subjectInfo.dumpAsDbFormat() + "; ";
            str = str.substring(0, str.length() - 2);
            str += "\n*";
        }

        return str;
    }
}
