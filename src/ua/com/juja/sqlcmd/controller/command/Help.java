package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;

public class Help implements Command{

    private View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
        view.write("List of commands: ");

        view.write("\tconnect|databaseName|userName|password");
        view.write("\t\tfor connecting to database, to work for");

        view.write("\tlist");
        view.write("\t\tfor getting list of all database tables, able to connect");

        view.write("\tclear|tableName");
        view.write("\t\tfor clearing whole table"); //todo if it was an accident, ask again

        view.write("\tcreate|tableName|column1|value1|column2|value2|...|columnN|valueN");
        view.write("\t\tfor creating notation in the table");

        view.write("\tfind|tableName");
        view.write("\t\tfor getting data of the table 'tableName'");

        view.write("\thelp");
        view.write("\t\tfor showing list on the screen");

        view.write("\texit");
        view.write("\t\tFor exit the program");
    }
}
