package ua.com.juja.sqlcmd.model;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.DatabaseManagerTest;
import ua.com.juja.sqlcmd.model.JDBCDatabaseManager;

public class JDBCDatabaseManagerTest extends DatabaseManagerTest {

    @Override
    public DatabaseManager getDatabaseManager() {
        return new JDBCDatabaseManager();
    }
}
