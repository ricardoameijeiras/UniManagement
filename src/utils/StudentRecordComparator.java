package utils;

import models.StudentRecord;

import java.util.Comparator;

/**
 * Created on 16/06/2016.
 * @author Ricardo Fernández Ameijeiras
 * @author Iago Pallares Tato
 */
public class StudentRecordComparator implements Comparator<StudentRecord>
{
    @Override
    public int compare(StudentRecord o1, StudentRecord o2)
    {
        int academicGradeCompare = Integer.compare(o1.getmGrade(), o2.getmGrade());

        if (academicGradeCompare == 0)
        {
            return o1.getmName().compareTo(o2.getmName());
        }
        else
        {
            return academicGradeCompare;
        }
    }
}
