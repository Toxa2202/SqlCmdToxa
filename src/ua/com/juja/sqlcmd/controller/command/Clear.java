package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Clear implements Command {
    private final DatabaseManager manager;
    private final View view;

    public Clear(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("clear|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 2) {
            throw new IllegalArgumentException("Format of command " +
                    "'clear|tableName', but you write " + command);
        }
        manager.clear(data[1]);

        view.write(String.format("Table %s was successfully erased.", data[1]));
    }
}
