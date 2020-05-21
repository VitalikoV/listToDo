import java.sql.SQLException;
import java.util.Scanner;

public class Facade {

    public static void startApplication() throws SQLException, ClassNotFoundException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String name = getNameOfDatabase(scanner);
        DatabaseFactory databaseFactory = openDatabaseByName(name);
        DatabaseList database = databaseFactory.createDatabase();
        database.openDatabaseConnection();

    }

    private static String getNameOfDatabase(Scanner scanner) {
        System.out.println("""
                Which database you want to open now:
                1. MySql
                2. MongoDB
                3. PostgresQl""");
        pointerForInput();
        return scanner.nextLine();
    }


    static DatabaseFactory openDatabaseByName(String name){
        if (isNameExist(name, "MySql") || isNameExist(name, "1")){
            return new MysqlListFactory();
        }else if (isNameExist(name, "MongoDB") || isNameExist(name, "2")){
            return new MongoDBListFactory();
        }else if (isNameExist(name, "PostgresQL") || isNameExist(name, "3")){
            return new PostgresQLListFactory();
        }else{
            throw new RuntimeException(name + "- is unknown name");
        }
    }

    public static boolean isNameExist(String actualName, String expectedName) {
        return actualName.trim().equalsIgnoreCase(expectedName);
    }

    static void pointerForInput() {
        System.out.print("-> ");
    }

}
