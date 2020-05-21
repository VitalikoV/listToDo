public class PostgresQLListFactory implements DatabaseFactory {
    @Override
    public DatabaseList createDatabase() {
        return new PostgresQLList();
    }
}
