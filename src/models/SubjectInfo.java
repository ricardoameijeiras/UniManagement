package models;

import java.util.List;

/**
 * Created on 16/06/2016.
 * @author Ricardo Fernández Ameijeiras
 * @author Iago Pallares Tato
 */
@SuppressWarnings("unused")
public class SubjectInfo
{
    private int    mSubjectId;
    private String mGroup;
    private int    mGroupId;

    public SubjectInfo(int subjectId, String group, int groupId)
    {
        mSubjectId = subjectId;
        mGroup     = group;
        mGroupId   = groupId;
    }

    public int getSubjectId()
    {
        return mSubjectId;
    }

    public String getGroup()
    {
        return mGroup;
    }

    public int getGroupId()
    {
        return mGroupId;
    }

    public void setSubjectId(int subjectId) {
        mSubjectId = subjectId;
    }

    public void setGroup(String group) {
        mGroup = group;
    }

    public void setGroupId(int groupId) {
        mGroupId = groupId;
    }

    public static boolean isSubjectInfoValid(List<String> subjectInfoParameters, boolean isProfessor)
    {
        if (!(Integer.parseInt(subjectInfoParameters.get(0)) >= 0))
        {
            if (isProfessor)
            {
                if (!(((subjectInfoParameters.get(1).equals("A")) || (subjectInfoParameters.get(1).equals("B")) &&
                         (Integer.parseInt(subjectInfoParameters.get(2)) >= 0)))) return false;
            }
            else
            {
                return true;
            }
        }

        return true;
    }

    public static SubjectInfo parseSubjectInfo(List<String> subjectInfoParameters, boolean isProfessor)
    {
        if (!isSubjectInfoValid(subjectInfoParameters, isProfessor)) return null;

        if (subjectInfoParameters.size() == 1)
        {
            return new SubjectInfo(Integer.parseInt(subjectInfoParameters.get(0)), null, -1);
        }
        else
        {
            return new SubjectInfo(Integer.parseInt(subjectInfoParameters.get(0)),
                                   subjectInfoParameters.get(1),
                                   Integer.parseInt(subjectInfoParameters.get(2)));
        }
    }

    public String dumpAsDbFormat()
    {
        String str = "";

        str += mSubjectId;
        str += mGroup   == null ? "" : " " + mGroup  + " ";
        str += mGroupId == -1   ? "" :       mGroupId;

        return str;
    }
}
