package managers;

import javafx.util.Pair;
import models.*;
import utils.Checkers;
import utils.StudentRecordComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created on 16/06/2016.
 * @author Ricardo Fernández Ameijeiras
 * @author Iago Pallares Tato
 */
public class Manager
{
    private static final String EJECUCION_FILE   = "ejecucion.txt"  ;
    private static final String AVISOS_FILE      = "avisos.txt"     ;
    private static final String PERSONAS_FILE    = "personas.txt"   ;
    private static final String ASIGNATURAS_FILE = "asignaturas.txt";

    enum Commands
    {
        None,
        InsertaPersona,
        AsignaCoordinador,
        AsignaCargaDocente,
        Matricula,
        AsignaGrupo,
        Evalua,
        Expediente,
        ObtenerCalendarioClases,
        OrdenaAlumnosXNota,
        Exit
    }

    private HashMap<String, Subject>    mSubjects   = new HashMap<>();
    private HashMap<String, Professor>  mProfessors = new HashMap<>();
    private HashMap<String, Student>    mStudents   = new HashMap<>();

    public void init()
    {
        loadSubjectsDatabase();
        loadPersonsDatabase();
    }

    private void loadPersonsDatabase()
    {
        mProfessors.clear();

        List<String>       lines        = IOManager.readFile("personas.txt");

        for (int i = 0; i < lines.size(); ++i) lines.set(i, lines.get(i).trim());

        int i = 0;
        while (i < lines.size())
        {
            if (lines.get(i).equals("profesor"))
            {
                Professor professor = Professor.parseProfessor(lines.subList(i, i + 9));
                mProfessors.put(professor.getDni(), professor);
                i += 10;
            }
            else if (lines.get(i).equals("alumno"))
            {
                Student student = Student.parseStudent(lines.subList(i, i + 8));
                mStudents.put(student.getDni(), student);
                i += 9;
            }
            else
            {
                i += 1;
            }
        }
    }

    private void loadSubjectsDatabase()
    {
        mSubjects.clear();

        List<String>       lines        = IOManager.readFile("asignaturas.txt");

        for (int i = 0; i < lines.size(); ++i) lines.set(i, lines.get(i).trim());

        for (int i = 0; i < lines.size(); i += 9)
        {
            Subject subject = Subject.parseSubject(lines.subList(i, i + 8));
            mSubjects.put(subject.getInitials(), subject);
        }
    }

    boolean insertarPersona(List<String> args)
    {
        String perfil   = args.get(0);
        String dni      = args.get(1);
        if(!Checkers.checkDni(dni)) IOManager.writeFile("IP -- Dni incorrecto", AVISOS_FILE);
        String nombre   = args.get(2);
        String apellidos= args.get(3);
        String fechaNace= args.get(4);
        if(!Checkers.checkDate(fechaNace))IOManager.writeFile("IP -- Fecha Incorrecta", AVISOS_FILE);
        if      (perfil.equals("alumno"))
        {
            if(existOnDict(dni, mStudents)) IOManager.writeFile("IP -- Alumno ya existente", AVISOS_FILE);
            String fechaIngreso = args.get(5);
            if(!Checkers.checkDate(fechaIngreso) || Checkers.checkFechasAlumno(fechaNace, fechaIngreso))
                IOManager.writeFile("IP -- Fecha Incorrecta", AVISOS_FILE);
        }
        else if (perfil.equals("profesor"))
        {
            if(existOnDict(dni, mProfessors)) IOManager.writeFile("IP -- Profesor ya existente", AVISOS_FILE);
            String categoria        = args.get(5);
            String departamento     = args.get(6);
            String horasAsignables  = args.get(7);
            //falta checkear el numero de horas
        }
        return true;
    }

    void insertarCoordinador(List<String> args)
    {
        String persona      = args.get(0);
        String asignatura   = args.get(1);
        Professor p = mProfessors.get(persona);
        if(p == null)
        {
            IOManager.writeFile("ACOORD -- Profesor inexistente", AVISOS_FILE);
        }
        else
        {
            if(p.getCategory() == Professor.Category.Asociado)
            {
                IOManager.writeFile("ACOORD -- Profesor no titular", AVISOS_FILE);
            }
            Subject sub = mSubjects.get(asignatura);
            if(sub == null)
            {
                IOManager.writeFile("ACOORD -- Asignatura inexistente", AVISOS_FILE);
            }
            else
            {
                if(!professorOnSubject(persona))
                {
                    IOManager.writeFile("ACOORD -- Profesor ya es coordinador de 2 materias", AVISOS_FILE);
                }
                else sub.setProfessorDni(persona);
            }
        }
    }


    void asignarCargaDocente(List<String> args)
    {
        String persona      = args.get(0);
        Professor p = mProfessors.get(persona);
        if(p == null)
        {
            IOManager.writeFile("ACDOC -- Profesor inexistente", AVISOS_FILE);
        }
        String asignatura   = args.get(1);
        Subject sub = mSubjects.get(asignatura);
        if(sub == null)
        {
            IOManager.writeFile("ACDOC -- Asignatura inexistente", AVISOS_FILE);
        }
        String tipoGrupo    = args.get(2);
        String grupo        = args.get(3);
        if      (tipoGrupo.equals("A"))
        {
            if(!sub.getAGroups().contains(grupo))
            {
                IOManager.writeFile("ACDOC -- Grupo ya asignado", AVISOS_FILE);
            }
        }
        else if (tipoGrupo.equals("B"))
        {
            if(!sub.getAGroups().contains(grupo))
            {
                IOManager.writeFile("ACDOC -- Grupo ya asignado", AVISOS_FILE);
            }
        }
        else
        {
            IOManager.writeFile("ACDOC -- Tipo de grupo incorrecto", AVISOS_FILE);
        }
        if(p.getWeeklyTeachingHours() < p.getSubjectsImparted().size()+1)
        {
            IOManager.writeFile("ACDOC -- Horas asignables superior al maximo", AVISOS_FILE);
        }
        if(!checkSolape( sub,
                        p.getSubjectsImparted()
                                .stream()
                                .map((SubjectInfo si)-> mSubjects.get(si.getSubjectId()))
                                .collect(Collectors.toList())))
        {
            IOManager.writeFile("ACDOC -- Se genera solape", AVISOS_FILE);
        }
        p.addSubjectImparted(new SubjectInfo(sub.getId(), grupo, Integer.parseInt(grupo)));
    }

    void matricularAlumno(List<String> args)
    {
        String dni          = args.get(0);
        String asignatura   = args.get(1);
        if(!existOnDict(dni, mStudents))
        {
            IOManager.writeFile("MAT -- Alumno inexistente", AVISOS_FILE);
        }
        else
        {
            if (!existOnDict(asignatura, mSubjects))
            {
                IOManager.writeFile("MAT -- Asignatura inexistente", AVISOS_FILE);
            }
            else
            {
                Subject sub     = mSubjects.get(asignatura);
                Student student = mStudents.get(dni);
                if(student.getSubjectsTaken().contains(sub))
                {
                    IOManager.writeFile("MAT -- Ya es alumno de esta asignatura", AVISOS_FILE);
                }
                else
                {
                    if(!checkPreRequisites(
                            sub.getPrerequisites()
                                .stream()
                                .collect(Collectors.toList()),
                            student.getPassedSubjects()
                                    .stream().map((Student.PassedSubject ps)->ps.getSubjectId())
                                    .collect(Collectors.toList())))
                    {
                        IOManager.writeFile("MAT -- No cumple requisitos", AVISOS_FILE);
                    }
                    else
                    {
                        student.addSubjectTaken(new SubjectInfo(sub.getId(), null, -1));
                    }
                }
            }
        }
    }

    boolean asignarGrupo(List<String> args)
    {
        System.out.println("AGRUPO -- Called asignar grupo");
        String dni          = args.get(0);
        String asignatura   = args.get(1);
        String tipoGrupo    = args.get(2);
        String grupo        = args.get(3);
        if(!existOnDict(dni, mStudents))
        {
            IOManager.writeFile("AGRUPO -- Alumno inexistente", AVISOS_FILE);
        }
        else
        {
            if (!existOnDict(asignatura, mSubjects))
            {
                IOManager.writeFile("AGRUPO -- Asignatura inexistente", AVISOS_FILE);
            }
            else
            {
                Subject sub = mSubjects.get(asignatura);
                Student s = mStudents.get(dni);
                if      (tipoGrupo.equals("A"))
                {
                    if(!sub.getAGroups().contains(grupo))
                    {
                        IOManager.writeFile("AGRUPO -- Grupo ya asignado", AVISOS_FILE);
                    }
                }
                else if (tipoGrupo.equals("B"))
                {
                    if(!sub.getAGroups().contains(grupo))
                    {
                        IOManager.writeFile("AGRUPO -- Grupo ya asignado", AVISOS_FILE);
                    }
                }
                else
                {
                    IOManager.writeFile("AGRUPO -- Tipo de grupo incorrecto", AVISOS_FILE);
                }
                if(!checkSolape( sub,
                        s.getSubjectsTaken()
                                .stream()
                                .map((SubjectInfo si)-> mSubjects.get(si.getSubjectId()))
                                .collect(Collectors.toList())))
                {
                    IOManager.writeFile("AGRUPO -- Se genera solape", AVISOS_FILE);
                }
                else
                {
                    for(SubjectInfo si: s.getSubjectsTaken())
                    {
                        if(si.getSubjectId() == sub.getId())
                        {
                            si.setGroup(tipoGrupo);
                            si.setGroupId(Integer.parseInt(grupo));
                        }
                    }
                }
            }
        }
        return true;
    }

    boolean evaluarAsignatura(List<String> args)
    {
        System.out.println("Called evaluar asignatura");
        return true;
    }

    void obtenerExpediente(List<String> args)
    {
        String dni  = args.get(0);
        String file = args.get(1);

        Student student = mStudents.get(dni);

        if(!existOnDict(dni, mStudents))
        {
            IOManager.writeFile("Alumno inexistente", AVISOS_FILE);
            return;
        }
        else if(student.getPassedSubjects().size() == 0)
        {
            IOManager.writeFile("Expediente vacio", AVISOS_FILE);
            return;
        }

        List<Student.PassedSubject> passedSubjects = student.getPassedSubjects();
        ArrayList<StudentRecord> studentRecords = new ArrayList<>();

        for (Student.PassedSubject passedSubject : passedSubjects)
        {
            Subject sub = null;

            for(Map.Entry<String, Subject> entry : mSubjects.entrySet())
            {
                Subject value = entry.getValue();

                if (value.getId() == passedSubject.getSubjectId())
                {
                    sub = value;
                }
            }

            studentRecords.add(new StudentRecord(sub.getGrade(), sub.getName(), passedSubject.getMark(), passedSubject.getAcademicGrade()));
        }

        for (StudentRecord studentRecord : studentRecords)
        {
            String str = "";

            str += studentRecord.getmGrade();
            str += studentRecord.getmName();
            str += studentRecord.getmMark();
            str += studentRecord.getmAcademicGrade();

            IOManager.writeFile(str, AVISOS_FILE);
        }
    }

    boolean obtenerCalendarioProfesor(List<String> args)
    {
        String dni = args.get(0);
        String file = args.get(1);
        Professor p = mProfessors.get(dni);
        if(p == null)
        {
            IOManager.writeFile("CALENP -- Profesor inexistente", AVISOS_FILE);
        }
        else
        {
            if(p.getSubjectsImparted().size() == 0)
            {
                IOManager.writeFile("CALENP -- Profesor sin assignaciones", AVISOS_FILE);
            }
            else
            {
                String ret = "Dia; Hora; Asignatura; Tipo grupo; Id grupo\n";
                for(SubjectInfo s : p.getSubjectsImparted())
                {
                    Subject sub = mSubjects.get(s.getSubjectId());
                    if(s.getGroup().equals("B"))
                    {
                        for(Group g: sub.getAGroups())
                        {
                            if(g.getId() == s.getGroupId())
                            {
                                ret += ";" + Group.Day.parseString(g.getDay());
                                ret += ";" + Integer.toString(g.getHourBegin());
                            }
                        }
                    }
                    else if(s.getGroup().equals("B"))
                    {
                        for(Group g: sub.getBGroups())
                        {
                            if(g.getId() == s.getGroupId())
                            {
                                ret += ";" + Group.Day.parseString(g.getDay());
                                ret += ";" + Integer.toString(g.getHourBegin());
                            }
                        }
                    }
                    ret += ";" + sub.getInitials();
                    ret += ";" + s.getGroup();
                    ret += ";" + s.getGroupId();
                    ret += "\n";
                }
                IOManager.writeFile(ret, file);
            }
        }
        return true;
    }

    void obtenerAlumnosNotaExpediente(List<String> args)
    {
        String salida = args.get(0);
        List<Pair<Student, Float>> studentsGrades = mStudents
                .values()
                .stream()
                .map((Student s) -> new Pair<Student, Float>(s, getNotaExpediente(s)))
                .collect(Collectors.toList());
        if(studentsGrades.stream().map((Pair<Student, Float> p)->p.getValue())
                .reduce(0.f, (aFloat, aFloat2) -> aFloat + aFloat2) <= 0)
        {
            IOManager.writeFile("OALNOTA -- No hay alumnos con asignaturas superadas", AVISOS_FILE);
        }
        else
        {
            studentsGrades.sort((Pair<?, Float> a, Pair<?, Float> b) -> a.getValue().compareTo(b.getValue()));
            String ret = "";
            for(Pair<Student, Float> p : studentsGrades)
            {
                Student s = p.getKey();
                Float nota = p.getValue();
                ret +=  s.getName()     + " " +
                        s.getSurname()  + " " +
                        s.getDni()      + " " +
                        nota + "\n";
            }
            IOManager.writeFile(ret, salida);
        }
    }

    public void mainLoop(String command, List<String> args)
    {
        Commands com = Commands.valueOf(command);
        switch (com)
        {
            case None:
                break;
            case InsertaPersona:
                insertarPersona(args);
                break;
            case AsignaCoordinador:
                insertarCoordinador(args);
                break;
            case AsignaCargaDocente:
                asignarCargaDocente(args);
                break;
            case Matricula:
                matricularAlumno(args);
                break;
            case AsignaGrupo:
                asignarGrupo(args);
                break;
            case Evalua:
                evaluarAsignatura(args);
                break;
            case Expediente:
                obtenerExpediente(args);
                break;
            case ObtenerCalendarioClases:
                obtenerCalendarioProfesor(args);
                break;
            case OrdenaAlumnosXNota:
                obtenerAlumnosNotaExpediente(args);
                break;
            case Exit:
                break;
            default:
                System.out.println("Comando incorrecto, vuelva a introducir los datos");
                break;
        }
    }

    private boolean existOnDict(String dni, HashMap<String, ?> dict)
    {
        return dict.containsKey(dni);
    }

    private boolean professorOnSubject(String dni)
    {
        return mProfessors.values().stream()
                .filter((Person p) -> p.getDni().equals(dni))
                .count() < 2;
    }

    private boolean checkSolape(Subject sub, List<Subject> l)
    {
        for(Subject s: l)
        {

            for(Group g1: sub.getAGroups())
            {
                for(Group g11: s.getAGroups())
                {
                    if(g1.getDay() == g11.getDay())
                    {
                        if( g11.getHourBegin() > g1.getHourBegin() &&
                            g11.getHourBegin() < g1.getHourEnd())
                            return true;
                        if( g11.getHourEnd() > g1.getHourBegin() &&
                            g11.getHourEnd() < g1.getHourEnd())
                            return true;
                    }
                }
            }
            for(Group g2: sub.getBGroups())
            {
                for(Group g22: s.getBGroups())
                {
                    if(g2.getDay() == g22.getDay())
                    {
                        if( g22.getHourBegin() > g2.getHourBegin() &&
                                g22.getHourBegin() < g2.getHourEnd())
                            return true;
                        if( g22.getHourEnd() > g2.getHourBegin() &&
                                g22.getHourEnd() < g2.getHourEnd())
                            return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkPreRequisites(List<Integer> l, List<Integer> sl)
    {
        for(int i : l)
        {
            if(!sl.contains(i)) return false;
        }
        return true;
    }

    private float getNotaExpediente(Student s)
    {
        float nota = s.getPassedSubjects()
                .stream()
                .map((Student.PassedSubject ps) ->ps.getMark())
                .reduce(0.f, (aFloat, aFloat2) -> aFloat + aFloat2);
        return nota/s.getPassedSubjects().size();
    }


    public HashMap<String, Subject> getmSubjects() {
        return mSubjects;
    }

    public HashMap<String, Professor> getmProfessors() {
        return mProfessors;
    }

    public HashMap<String, Student> getmStudents() {
        return mStudents;
    }
}
