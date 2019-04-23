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

        view.write("\tlist");
        view.write("\t\tfor getting list of all database tables, able to connect");

        view.write("\tfind|tableName");
        view.write("\t\tfor getting data of the table 'tableName'");

        view.write("\thelp");
        view.write("\t\tfor showing list on the screen");

        view.write("\texit");
        view.write("\t\tFor exit the program");
    }
}
