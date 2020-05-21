import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;

import java.sql.Time;
import java.util.Objects;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class MongoDBList extends Additional implements DatabaseList {
    MongoCollection<Document> collection;

    JsonWriterSettings prettyPrint = JsonWriterSettings.builder().indent(true).build();

    public void openDatabaseConnection() throws InterruptedException {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        System.out.println("server connection successfully done...");

        MongoDatabase database = mongoClient.getDatabase("toDoList");
        System.out.println("Connecting to the database...");
        System.out.println("Connected to the database: " + database.getName());
        collection = database.getCollection("tasks");
        Thread.sleep(500);
        getToDoFromConsole(collection);


    }

    public void getToDoFromConsole(MongoCollection<Document> collection) {
        Scanner in = new Scanner(System.in);
        String command = getWhatToDo(in);

        if (isCommandExist(command, CREATE_TASK)) {
            String name, task, start_at_string, end_at_string;
            Time start_at, end_at;

            name = setInputParam(in, "name: ");
            task = setInputParam(in, "task: ");
            start_at_string = setInputParam(in, "time start(hh:mm:ss): ");
            start_at = Time.valueOf(start_at_string);
            end_at_string = setInputParam(in, "time end(hh:mm:ss): ");
            end_at = Time.valueOf(end_at_string);

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
            deleteTask(nameOfTask);

        } else {
            throw new RuntimeException(command + " - is unknown command");
        }

        //RECURSION
        getToDoFromConsole(collection);
    }


    @Override
    public void deleteTask(String name) {
        Bson filter = eq("name", name);
        DeleteResult deleteResult = collection.deleteOne(filter);
        System.out.println("Delete executed...");
        System.out.println(deleteResult);
    }

    @Override
    public void selectAll() {
        FindIterable<Document> iterable = collection.find();
        MongoCursor<Document> cursor = iterable.iterator();
        System.out.println("Task list with cursor: ");
        while (cursor.hasNext()) {
            System.out.println(cursor.next().toJson(prettyPrint));
        }
    }

    @Override
    public void selectWhereNameEquals(String nameOfTask) {
        System.out.println(Objects.requireNonNull(collection.find(eq("name", nameOfTask)).first()).toJson(prettyPrint));

    }


    @Override
    public void createTask(String name, String task, Time start_at, Time end_at) {
        Document document = new Document("name", name)
                .append("task", task)
                .append("time start", start_at)
                .append("time end", end_at);

        collection.insertOne(document);
    }

    @Override
    public void updateTask(String name, String paramToUpdate, String valueToUpdate) {
        Bson filter = eq("name", name);
        Bson updateOperation = set(paramToUpdate, valueToUpdate);
        collection.updateOne(filter, updateOperation);
        System.out.println("update performed...");
        System.out.println(Objects.requireNonNull(collection.find(filter).first()).toJson(prettyPrint));
    }

}
