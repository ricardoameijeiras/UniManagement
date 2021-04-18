package entry_point;

import managers.IOManager;
import managers.Manager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Strinnityk on 23/06/2016.
 */
public class Proyecto44
{
    public static void main(String[] args)
    {
        Manager manager = new Manager();
        manager.init();

        List<String> commands = IOManager.readFile("ejecucion.txt");

        for (String command : commands)
        {
            command = command.trim();
            if (command.substring(0, 1).equals("*")) continue;

            List<String> executeCommands = Arrays.asList(command.split(" "));





        }

        manager.mainLoop


    }
}
