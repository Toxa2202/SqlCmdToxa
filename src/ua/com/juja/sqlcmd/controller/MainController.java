package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.sqlcmd.view.Console;
import ua.com.juja.sqlcmd.view.View;

import java.util.Arrays;

public class MainController {
    private View view;
    private DatabaseManager manager;

    public MainController(View view, DatabaseManager manager) {
        this.view = new Console();
        this.manager = new JDBCDatabaseManager();
    }

    public void run() {
        connectToDb();
        while (true) {
            view.write("Enter command (or 'help' for help)");
            String command = view.read();
            if (command.equals("list")) {
                doList();
            } else if (command.equals("help")) {
                doHelp();
            } else if (command.equals("exit")) {
                view.write("Have a nice day!");
                System.exit(0);
            } else {
                view.write("Command does not exist: " + command);
            }
        }
    }

    private void doHelp() {
        view.write("List of commands: ");
        view.write("\tlist");
        view.write("\t\tfor getting list of all database tables, able to connect");
        view.write("\thelp");
        view.write("\t\tfor showing list on the screen");
        view.write("\texit");
        view.write("\t\tFor exit the program");
    }

    private void doList() {
        String[] tableNames = manager.getTableNames();
        String message = Arrays.toString(tableNames);
        view.write(message);
    }

    private void connectToDb() {
        view.write("HEllo user!");
        view.write("Enter the name of db, name of user and password in format: database|userName|password");

        while (true) {
            try {
                String string = view.read();
                String[] data = string.split("\\|");
                if (data.length != 3) {
                    throw new IllegalArgumentException("Wrong number of symbols. Must be 3, but is: " + data.length);
                }
                String databaseName = data[0];
                String userName = data[1];
                String password = data[2];

                manager.connect(databaseName, userName, password);
                break;
            } catch (Exception e) {
                printError(e);
            }
        }
        view.write("Success!");
    }

    private void printError(Exception e) {
        String message = e.getMessage();
        if (e.getCause() != null) {
            message += " " + e.getCause().getMessage();
        }
        view.write("We have a problem, because: " + message);
        view.write("Repeat please.");
    }
}
