import java.sql.*;
import java.util.Scanner;

public class MysqlList extends Additional implements DatabaseList {

    Connection connection;

    public void openDatabaseConnection() throws ClassNotFoundException, SQLException {
        String userName = "root";
        String password = "Vdrapak12";
        String conectionUrl = "jdbc:mysql://localhost:3306/listToDo";
        System.out.println("Registering JDBC driver...");
        Class.forName("com.mysql.jdbc.Driver");

        System.out.println("Creating connection...");
        connection = DriverManager.getConnection(conectionUrl, userName, password);
            getToDoFromConsole(connection);

    }

    public void getToDoFromConsole(Connection connection) throws SQLException {
        Scanner in = new Scanner(System.in);
        String command = getWhatToDo(in);

        if (isCommandExist(command, CREATE_TABLE)) {
            createTable();
        } else if (isCommandExist(command, DELETE_TABLE)) {
            dropTable();
        } else if (isCommandExist(command, CREATE_TASK)) {

            String name = setInputParam(in, "name: ");
            String task = setInputParam(in, "task: ");
            String start_at_string = setInputParam(in, "time start(hh:mm:ss): ");
            Time start_at = Time.valueOf(start_at_string);
            String end_at_string = setInputParam(in, "time end(hh:mm:ss): ");
            Time end_at = Time.valueOf(end_at_string);

            createTask(name, task, start_at, end_at);
        } else if (isCommandExist(command, SELECT_ALL)) {
            selectAll();
        } else if (command.contains(SELECT_WHERE_NAME)) {

            String nameOfTask = getParams(command, 1, REGEX_FOR_NAME);
            selectWhereNameEquals(nameOfTask);

        } else if (command.contains(UPDATE_TASK)) {
            String name, paramToUpdate, valueToUpdate, params;

            name = getParams(command, 1, REGEX_FOR_UPDATE);
            params = getParams(command, 2, REGEX_FOR_UPDATE);
            paramToUpdate = getParams(params, 1, REGEX_FOR_VALUE);
            valueToUpdate = getParams(params, 2, REGEX_FOR_VALUE);

            //delete paramToUpdate
            updateTask(name, paramToUpdate, valueToUpdate);
        } else if (command.contains(DELETE_TASK)) {

            String nameOfTask = getParams(command, 1, REGEX_FOR_NAME);
            selectWhereNameEquals(nameOfTask);

        } else {
            throw new RuntimeException(command + " - is unknown command");
        }

        getToDoFromConsole(connection);

    }



    public void createTable() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("CREATE table if not exists tasks(id mediumint not null auto_increment primary key, name varchar(250), task varchar(250), start_at TIME, end_at TIME);");
        preparedStatement.executeUpdate();
    }

    public void dropTable() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("drop table tasks");
        preparedStatement.executeUpdate();
    }

    @Override
    public void createTask(String name, String task, Time start_at, Time end_at) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT into tasks(name, task, start_at, end_at) values (?, ?, ?, ?);");
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, task);
        preparedStatement.setTime(3, start_at);
        preparedStatement.setTime(4, end_at);
        preparedStatement.executeUpdate();
    }

    @Override
    public void deleteTask(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM tasks where name = ?");
        preparedStatement.setString(1, name);
        preparedStatement.executeUpdate();
    }

    @Override
    public void selectAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from tasks");
        ResultSet resultSet = preparedStatement.executeQuery();
        iterateData(resultSet);
    }

    @Override
    public void selectWhereNameEquals(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from tasks where name = ?");
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        iterateData(resultSet);
    }

    public void iterateData(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            System.out.println(resultSet.getInt("id"));
            System.out.println(resultSet.getString("name"));
            System.out.println(resultSet.getString("task"));
            System.out.println(resultSet.getTime("start_at"));
            System.out.println(resultSet.getTime("end_at"));
        }
    }

    @Override
    public void updateTask(String name, String paramToUpdate, String valueToUpdate) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE tasks SET task = ? WHERE name = ?");
        preparedStatement.setString(1, valueToUpdate);
        preparedStatement.setString(2, name);
        preparedStatement.executeUpdate();


    }

}
