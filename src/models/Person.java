package models;

import utils.Checkers;

import java.util.List;

/**
 * Created on 16/06/2016.
 * @author Ricardo Fernández Ameijeiras
 * @author Iago Pallares Tato
 */
@SuppressWarnings("unused")
public class Person
{
    private String mName; //50
    private String mSurname; //50
    private String mDni;
    private String mBirthdayDate; //dd/mm/aaaa

    public Person(String name, String surname, String dni, String birthdayDate)
    {
        mName         = name;
        mSurname      = surname;
        mDni          = dni;
        mBirthdayDate = birthdayDate;
    }

    public String getName() {

        return mName;
    }

    public String getSurname()
    {
        return mSurname;
    }

    public String getDni()
    {
        return mDni;
    }

    public String getBirthdayDate()
    {
        return mBirthdayDate;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setSurname(String surname) {
        mSurname = surname;
    }

    public void setDni(String dni) {
        mDni = dni;
    }

    public void setBirthdayDate(String birthdayDate) {
        mBirthdayDate = birthdayDate;
    }

    public static boolean isPersonValid(List<String> personParameters)
    {
        if (!((personParameters.get(0).equals("alumno") || personParameters.get(0).equals("profesor")) &&
              (personParameters.get(1).length() == 9 && Integer.parseInt(personParameters.get(1).substring(0, 8)) >= 0 && personParameters.get(1).substring(8, 9).matches(("[A-Z]"))) &&
              (personParameters.get(2).length() <= 50) &&
              (personParameters.get(3).length() <= 50) &&
              (Checkers.checkDate(personParameters.get(4))))) return false;

        return true;
    }

    public String dumpAsDbFormat()
    {
        String str = "";

        str += mDni          + "\n";
        str += mName         + "\n";
        str += mSurname      + "\n";
        str += mBirthdayDate;

        return str;
    }
}
