package models;

import java.util.List;

/**
 * Created on 16/06/2016.
 * @author Ricardo Fernández Ameijeiras
 * @author Iago Pallares Tato
 */
@SuppressWarnings("unused")
public class Group
{
    private int mId;
    private Day mDay;
    private int mHourBegin; //9-18
    private int mHourEnd; //10-19
    //1 o 2 horas de clase

    public Group(int id, Day day, int hourBegin, int hourEnd)
    {
        mId        = id;
        mDay       = day;
        mHourBegin = hourBegin;
        mHourEnd   = hourEnd;
    }

    public int getId()
    {
        return mId;
    }

    public Day getDay()
    {
        return mDay;
    }

    public int getHourBegin()
    {
        return mHourBegin;
    }

    public int getHourEnd()
    {
        return mHourEnd;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setDay(Day day) {
        mDay = day;
    }

    public void setHourBegin(int hourBegin) {
        mHourBegin = hourBegin;
    }

    public void setHourEnd(int hourEnd) {
        mHourEnd = hourEnd;
    }

    public enum Day
    {
        Lunes,
        Martes,
        Miercoles,
        Jueves,
        Viernes;

        public static Day parseDay(String str)
        {
            switch(str)
            {
                case "L":
                    return Lunes;
                case "M":
                    return Martes;
                case "X":
                    return Miercoles;
                case "J":
                    return Jueves;
                case "V":
                    return Viernes;
                default:
                    return null;
            }
        }

        public static String parseString(Day day)
        {
            switch(day)
            {
                case Lunes:
                    return "L";
                case Martes:
                    return "M";
                case Miercoles:
                    return "X";
                case Jueves:
                    return "J";
                case Viernes:
                    return "V";
                default:
                    return null;
            }
        }
    }

    public static boolean isGroupValid(List<String> groupParameters)
    {
        if (!((Integer.parseInt(groupParameters.get(0)) >= 0)                                                    &&
              (Day.parseDay(groupParameters.get(1)) != null)                                                     &&
              (Integer.parseInt(groupParameters.get(2)) >= 9  && Integer.parseInt(groupParameters.get(2)) <= 18) &&
              (Integer.parseInt(groupParameters.get(3)) >= 10 && Integer.parseInt(groupParameters.get(2)) <= 19) &&
              ((Integer.parseInt(groupParameters.get(3)) - Integer.parseInt(groupParameters.get(2)) == 1) ||
               (Integer.parseInt(groupParameters.get(3)) - Integer.parseInt(groupParameters.get(2)) == 2)))) return false;

        return true;
    }

    public static Group parseGroup(List<String> groupParameters)
    {
        if (!isGroupValid(groupParameters)) return null;

        return new Group(Integer.parseInt(groupParameters.get(0)),
                         Day.parseDay    (groupParameters.get(1)),
                         Integer.parseInt(groupParameters.get(2)),
                         Integer.parseInt(groupParameters.get(3)));
    }

    public String dumpAsDbFormat()
    {
        String str = "";

        str += mId                   + " ";
        str += Day.parseString(mDay) + " ";
        str += mHourBegin + " ";
        str += mHourEnd;

        return str;
    }
}
