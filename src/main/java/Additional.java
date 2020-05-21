import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Additional {

    public static final String CREATE_TABLE = "create table";
    public static final String DELETE_TABLE = "delete table";
    public static final String CREATE_TASK = "create new task";
    public static final String SELECT_ALL = "show all tasks";
    public static final String SELECT_WHERE_NAME = "show task where name";
    public static final String UPDATE_TASK = "update task where name";
    public static final String DELETE_TASK = "delete task where name";
    public static final String REGEX_FOR_UPDATE = "^.*'([^']*)'.*'([^']*)'$";
    public static final String REGEX_FOR_VALUE = "(.*) = (.*)";
    public static final String REGEX_FOR_NAME = ".*'(.*)'$";

    String setInputParam(Scanner in, String s) {
        pointerForInput();
        System.out.print(s);
        String param = in.nextLine();
        exitFromConsole(param);
        return param;
    }

    String getParams(String command, int indexInGroup, String regex) {
        String param = null;
        Pattern patternForUpdate = Pattern.compile(regex);
        Matcher update = patternForUpdate.matcher(command);
        if (update.matches()) {
            param = update.group(indexInGroup);
        }
        return param;
    }

    String getWhatToDo(Scanner in) {
        String command;
        System.out.println("What to do: ");
        pointerForInput();
        command = in.nextLine();
        exitFromConsole(command);
        return command;
    }

    void exitFromConsole(String command) {
        if (isCommandExist(command, "q") || isCommandExist(command, "quit")) {
            System.exit(0);
        }
    }

    boolean isCommandExist(String actualName, String expectedName) {
        return actualName.trim().equalsIgnoreCase(expectedName);
    }

    void pointerForInput() {
        System.out.print("-> ");
    }


}
