package models;

import utils.ArrayUtils;

import java.util.*;

/**
 * Created on 16/06/2016.
 * @author Ricardo Fernández Ameijeiras
 * @author Iago Pallares Tato
 */
@SuppressWarnings("unused")
public class Subject
{
    private int              mId;
    private String           mName; //75 chars
    private String           mInitials; //10 chars
    private int              mGrade; //1-4
    private String           mProfessorDni; //debe ser titular
    private HashSet<Integer> mPrerequisites; //entre 0 y 10
    private ArrayList<Group> mAGroups;
    private ArrayList<Group> mBGroups;

    public Subject(int id, String name, String initials, int grade, String professorDni, HashSet<Integer> prerequisites, ArrayList<Group> aGroups, ArrayList<Group> bGroups)
    {
        mId            = id;
        mName          = name;
        mInitials      = initials;
        mGrade         = grade;
        mProfessorDni  = professorDni;
        mPrerequisites = prerequisites != null ? prerequisites : new HashSet  <>();
        mAGroups       = aGroups       != null ? aGroups       : new ArrayList<>();
        mBGroups       = bGroups       != null ? bGroups       : new ArrayList<>();
    }

    public int getId()
    {
        return mId;
    }

    public String getName()
    {
        return mName;
    }

    public String getInitials()
    {
        return mInitials;
    }

    public int getGrade()
    {
        return mGrade;
    }

    public String getProfessorDni()
    {
        return mProfessorDni;
    }

    public Set<Integer> getPrerequisites()
    {
        return mPrerequisites;
    }

    public void addPrerequisite(int prerequisite)
    {
        mPrerequisites.add(prerequisite);
    }

    public ArrayList<Group> getAGroups()
    {
        return mAGroups;
    }

    public void addGroupToA (Group group)
    {
        mAGroups.add(group);
    }

    public ArrayList<Group> getBGroups()
    {
        return mBGroups;
    }

    public void addGroupToB (Group group)
    {
        mBGroups.add(group);
    }

    public void setId(int id) {
        mId = id;
    }

    public void setmName(String mName) {
        mName = mName;
    }

    public void setmInitials(String initials) {
        mInitials = initials;
    }

    public void setGrade(int grade) {
        mGrade = grade;
    }

    public void setProfessorDni(String professorDni) {
        mProfessorDni = professorDni;
    }

    public void setPrerequisites(HashSet<Integer> prerequisites) {
        mPrerequisites = prerequisites;
    }

    public void setAGroups(ArrayList<Group> aGroups) {
        mAGroups = aGroups;
    }

    public void setBGroups(ArrayList<Group> bGroups) {
        mBGroups = bGroups;
    }

    private static boolean isSubjectValid(List<String> subjectParameters)
    {
        if (subjectParameters.size() != 8)
        {
            System.out.println("Not enough parameters.");
            return false;
        }

        if (!((Integer.parseInt(subjectParameters.get(0)) >= 0) &&
            (subjectParameters.get(1).length() <= 75) &&
            (subjectParameters.get(2).length() <= 10 && subjectParameters.get(2).indexOf(' ') == -1) &&
            (Integer.parseInt(subjectParameters.get(3)) >= 1 && Integer.parseInt(subjectParameters.get(3)) <= 4) &&
            ((subjectParameters.get(4).length() == 9 && Integer.parseInt(subjectParameters.get(4).substring(0, 8)) >= 0 && subjectParameters.get(4).substring(8, 9).matches(("[A-Z]")))) ||
             (subjectParameters.get(4).isEmpty()))) return false;

        List<String> prerequisites = Arrays.asList(subjectParameters.get(5).split(","));
        for (String prerequisite : prerequisites)
        {
            if (prerequisite.isEmpty()) continue;
            if (!(Integer.parseInt(prerequisite) >= 0)) return false;
        }

        List<String> aGroups       = Arrays.asList(subjectParameters.get(6).split(";"));
        for (String group : aGroups)
        {
            if (group.isEmpty()) continue;
            if (!Group.isGroupValid(ArrayUtils.stringToStringListTrimmed(group))) return false;
        }

        List<String> bGroups       = Arrays.asList(subjectParameters.get(7).split(";"));
        for (String group : bGroups)
        {
            if (group.isEmpty()) continue;
            if (!Group.isGroupValid(ArrayUtils.stringToStringListTrimmed(group))) return false;
        }

        return true;
    }

    public static Subject parseSubject(List<String> subjectParameters)
    {
        if (!isSubjectValid(subjectParameters)) return null;

        Subject subject =  new Subject(Integer.parseInt(subjectParameters.get(0)),
                                                        subjectParameters.get(1),
                                                        subjectParameters.get(2),
                                       Integer.parseInt(subjectParameters.get(3)),
                                       subjectParameters.get(4).isEmpty() ? null : subjectParameters.get(4),
                                       null,
                                       null,
                                       null);

        List<String> prerequisites = Arrays.asList(subjectParameters.get(5).split(","));
        for (String prerequisite : prerequisites)
        {
            if (prerequisite.isEmpty()) continue;
            subject.addPrerequisite(Integer.parseInt(prerequisite));
        }

        List<String> aGroups       = Arrays.asList(subjectParameters.get(6).split(";"));
        for (String group : aGroups)
        {
            subject.addGroupToA(Group.parseGroup(ArrayUtils.stringToStringListTrimmed(group)));
        }

        List<String> bGroups       = Arrays.asList(subjectParameters.get(7).split(";"));
        for (String group : bGroups)
        {
            subject.addGroupToB(Group.parseGroup(ArrayUtils.stringToStringListTrimmed(group)));
        }

        return subject;
    }

    public String dumpAsDbFormat()
    {
        String str = "";

        str += mId                                           + "\n";
        str += mName                                         + "\n";
        str += mInitials                                     + "\n";
        str += mGrade                                        + "\n";
        str += mProfessorDni  == null ? "\n" : mProfessorDni + "\n";

        if (mPrerequisites.size() == 0) str += "\n";
        else
        {
            for (Integer i : mPrerequisites)str += i + ", ";
            str = str.substring(0, str.length() - 2);
            str += "\n";
        }

        if (mAGroups.size() == 0) str += "\n";
        else
        {
            for (Group group : mAGroups) str += group.dumpAsDbFormat() + "; ";
            str = str.substring(0, str.length() - 2);
            str += "\n";
        }

        if (mBGroups.size() == 0) str += "\n";
        else
        {
            for (Group group : mBGroups) str += group.dumpAsDbFormat() + "; ";
            str = str.substring(0, str.length() - 2);
            str += "\n*";
        }

        return str;
    }
}
