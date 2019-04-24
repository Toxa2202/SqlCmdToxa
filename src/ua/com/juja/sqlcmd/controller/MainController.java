package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.Console;
import ua.com.juja.sqlcmd.view.View;

public class MainController {
    private Command[] commands;
    private View view;

    public MainController(View view, DatabaseManager manager) {
        this.view = new Console();
        this.commands = new Command[] {
                new Connect(manager, view),
                new Help(view),
                new Exit(view),
                new IsConnected(manager, view),
                new List(manager, view),
                new Find(manager, view),
                new Unsupported(view) };
    }

    public void run() {
        view.write("Hello user!");
        view.write("Enter the name of db, name of user and password in format: \n\tconnect|databaseName|userName|password");

        while (true) {
            String input = view.read();

            for (Command command : commands) {
                if (command.canProcess(input)) {
                    command.process(input);
                    break;
                }
            }
            view.write("Enter command (or 'help' for help)");
        }
    }
}
