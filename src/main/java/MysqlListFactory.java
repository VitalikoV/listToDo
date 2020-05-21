public class MysqlListFactory implements DatabaseFactory {
    @Override
    public DatabaseList createDatabase() {
        return new MysqlList();
    }
}
