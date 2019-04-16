package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.sqlcmd.view.Console;
import ua.com.juja.sqlcmd.view.View;

public class MainController {
    private View view;
    private DatabaseManager manager;

    public MainController(View view, DatabaseManager manager) {
        this.view = new Console();
        this.manager = new JDBCDatabaseManager();
    }

    public void run() {
        connectToDb();
        //
    }

    private void connectToDb() {
        view.write("HEllo user!");
        view.write("Enter the name of db, name of user and password in format: database|userName|password");

        while (true) {
            String string = view.read();
            String[] data = string.split("\\|");
            String databaseName = data[0];
            String userName = data[1];
            String password = data[2];

            try {
                manager.connect(databaseName, userName, password);
                break;
            } catch (Exception e) {
                String message = e.getMessage();
                if (e.getCause() != null) {
                    message += " " + e.getCause().getMessage();
                }
                view.write("We have a problem, because: " + message);
                view.write("Repeat please.");
            }
        }
        view.write("Success!");
    }
}
