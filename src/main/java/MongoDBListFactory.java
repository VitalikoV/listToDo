public class MongoDBListFactory implements DatabaseFactory{

    @Override
    public DatabaseList createDatabase() {
        return new MongoDBList();
    }
}
