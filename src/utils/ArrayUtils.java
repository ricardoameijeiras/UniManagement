package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 16/06/2016.
 * @author Ricardo Fernández Ameijeiras
 * @author Iago Pallares Tato
 */
public class ArrayUtils
{
    public static List<String> stringToStringListTrimmed(String list)
    {
        if (list.isEmpty()) return null;

        List<String> subjectInfoParametersList = new ArrayList<>(Arrays.asList(list.split(" ")));

        for (int i = 0; i < subjectInfoParametersList.size(); ++i)
        {
            if (subjectInfoParametersList.get(i).isEmpty())
            {
                subjectInfoParametersList.remove(0);
                --i;
                continue;
            }
            subjectInfoParametersList.set(i, subjectInfoParametersList.get(i).trim());
        }

        return subjectInfoParametersList;
    }
}
