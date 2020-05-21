import java.sql.*;


public interface DatabaseList {

    void openDatabaseConnection() throws SQLException, ClassNotFoundException, InterruptedException;

    void createTask(String name, String task, Time start_at, Time end_at) throws SQLException;

    void deleteTask(String name) throws SQLException;

    void updateTask(String name, String paramToUpdate, String valueToUpdate) throws SQLException;

    void selectAll() throws SQLException;

    void selectWhereNameEquals(String name) throws SQLException;


}
