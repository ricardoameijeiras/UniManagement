package managers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 * Created on 16/06/2016.
 * @author Ricardo Fernández Ameijeiras
 * @author Iago Pallares Tato
 */
public class IOManager
{
    public static List<String> readFile(String path)
    {
        List<String> lines = null;

        try
        {
            lines = Files.readAllLines(Paths.get(path));
        }
        catch (IOException e)
        {
            System.out.println("Couldn't read from file.");
        }

        return lines;
    }

    public static void writeFile(String text, String path)
    {
        writeFile(Collections.singletonList(text), path);
    }

    public static void writeFile(List<String> textList, String path)
    {
        try
        {
            Files.write(Paths.get(path), textList, UTF_8, APPEND, CREATE);
        }
        catch(IOException e)
        {
            System.out.println("Couldn't write to file.");
        }
    }

    /*
    public static void main(String[] args)
    {
        //writeFile("works", "test.txt");
        //System.out.println(readFile("test.txt"));
    }
    */
}
