package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Create implements Command{
    private final DatabaseManager manager;
    private final View view;

    public Create(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("create|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length % 2 != 0) {
            throw new IllegalArgumentException("Must be even number of parameters in format " +
                    "'create|tableName|column1|value1|column2|value2|...|columnN|valueN', " +
                    "but you sent: " + command);
        }

        String tableName = data[1];

        DataSet dataSet = new DataSet();
        for (int index = 1; index < data.length / 2; index++) {
            String columnName = data[index * 2];
            String value = data[index * 2 + 1];

            dataSet.put(columnName, value);
        }
        manager.create(tableName, dataSet);

        view.write(String.format("Notation %s was successfully created in table '%s'.", dataSet, tableName));
    }
}
