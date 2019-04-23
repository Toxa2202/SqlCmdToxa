package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.Console;
import ua.com.juja.sqlcmd.view.View;

public class MainController {
    private Command[] commands;
    private View view;
    private DatabaseManager manager;

    public MainController(View view, DatabaseManager manager) {
        this.view = new Console();
        this.manager = manager;
        this.commands = new Command[] { new Exit(view), new Help(view),
                new List(manager, view), new Find(manager, view) };
    }

    public void run() {
        connectToDb();
        while (true) {
            view.write("Enter command (or 'help' for help)");
            String command = view.read();

            if (commands[2].canProcess(command)) {
                commands[2].process(command);
            } else if (commands[1].canProcess(command)) {
                commands[1].process(command);
            } else if (commands[0].canProcess(command)) {
                commands[0].process(command);
            } else if (commands[3].canProcess(command)) {
                commands[3].process(command);
            } else {
                view.write("Command does not exist: " + command);
            }
        }
    }

    private void connectToDb() {
        view.write("Hello user!");
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
