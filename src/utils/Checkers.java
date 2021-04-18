package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created on 16/06/2016.
 * @author Ricardo Fernández Ameijeiras
 * @author Iago Pallares Tato
 */
public class Checkers
{
    static public boolean checkDni(String dni)
    {
        if(!(dni.length() == 9))                         return false;
        if(!(Integer.parseInt(dni.substring(0,8)) >= 0)) return false;
        if(!(dni.substring(9,9).matches("[A-Z]")))       return false;
        return true;
    }

    static public boolean checkDate(String date)
    {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date d;
        try
        {
            Date before = df.parse("01/01/1950");
            Date after  = df.parse("01/01/2020");
            d = df.parse(date);
            if(d.after(after) || d.before(before)) return false;
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    static public boolean checkFechasAlumno(String d1, String d2)
    {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try
        {
            Date date1  = df.parse(d1);
            Date date2  = df.parse(d2);
            long diff   = date2.getTime() - date1.getTime();
            long anos15 = TimeUnit.DAYS.toMillis(365*15);
            long anos65 = TimeUnit.DAYS.toMillis(365*65);
            if(diff < anos15 || diff > anos65) return false;
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    static public boolean checkGroups(String grp)
    {
        return grp.equals("A") || grp.equals("B");
    }
}
