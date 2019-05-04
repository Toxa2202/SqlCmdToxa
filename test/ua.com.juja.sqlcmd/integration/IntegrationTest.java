package ua.com.juja.sqlcmd.integration;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.Main;

import java.io.*;
import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    @Before
    public void setup() {
        out = new ByteArrayOutputStream();
        in = new ConfigurableInputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testHelp() {
        // given
        in.add("help");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter the name of db, name of user and password in format: \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                // help
                "List of commands: \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                "\t\tfor connecting to database, to work for\r\n" +
                "\tlist\r\n" +
                "\t\tfor getting list of all database tables, able to connect\r\n" +
                "\t\tclear|tableName\r\n" +
                "\t\tfor clearing whole table\r\n" +
                "\tcreate|tableName|column1|value1|column2|value2|...|columnN|valueN\r\n" +
                "\t\tfor creating notation in the table\r\n" +
                "\tfind|tableName\r\n" +
                "\t\tfor getting data of the table 'tableName'\r\n" +
                "\thelp\r\n" +
                "\t\tfor showing list on the screen\r\n" +
                "\texit\r\n" +
                "\t\tFor exit the program\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // exit
                "Have a nice day!\r\n", getData());
    }

    private String getData() {
        try {
            String result = new String(out.toByteArray(), "UTF-8");
            out.reset();
            return result;
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Test
    public void testExit() {
        // given
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter the name of db, name of user and password in format: \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                // exit
                "Have a nice day!\r\n", getData());
    }

    @Test
    public void testListWithoutConnect() {
        // given
        in.add("list");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter the name of db, name of user and password in format: \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                // list
                "You can't use this command 'list' until use command \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // exit
                "Have a nice day!\r\n", getData());
    }

    @Test
    public void testFindWithoutConnect() {
        // given
        in.add("find|user");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter the name of db, name of user and password in format: \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                // find|user
                "You can't use this command 'find|user' until use command \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // exit
                "Have a nice day!\r\n", getData());
    }

    @Test
    public void testUnsupported() {
        // given
        in.add("unsupported");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter the name of db, name of user and password in format: \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                // unsupported
                "You can't use this command 'unsupported' until use command \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // exit
                "Have a nice day!\r\n", getData());
    }

    @Test
    public void testUnsupportedAfterConnect() {
        // given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("unsupported");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter the name of db, name of user and password in format: \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                // connect
                "Success!\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // unsupported
                "Command does not exist: unsupported\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // exit
                "Have a nice day!\r\n", getData());
    }

    @Test
    public void testListAfterConnect() {
        // given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("list");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter the name of db, name of user and password in format: \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                // connect
                "Success!\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // list
                "[user]\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // exit
                "Have a nice day!\r\n", getData());
    }

    @Test
    public void testFindWAfterConnect() {
        // given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("find|user");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter the name of db, name of user and password in format: \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                // connect
                "Success!\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // find|user
                "---------------\r\n" +
                "|name|password|id|\r\n" +
                "---------------\r\n" +
                "|Stiven|*****|13|\r\n" +
                "|Eva|+++++|14|\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // exit
                "Have a nice day!\r\n", getData());
    }

    @Test
    public void testFindWithErrorAfterConnect() {
        // given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("find|non-exist");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter the name of db, name of user and password in format: \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                // connect
                "Success!\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // find|non-exist
                "---------------\r\n" +
                "|\r\n" +
                "---------------\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // exit
                "Have a nice day!\r\n", getData());
    }

    @Test
    public void testConnectAfterConnect() {
        // given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("list");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter the name of db, name of user and password in format: \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                // connect sqlcmd
                "Success!\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // list
                "[user]\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // exit
                "Have a nice day!\r\n", getData());
    }

    @Test
    public void testConnectWithError() {
        // given
        in.add("connect|sqlcmd");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter the name of db, name of user and password in format: \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                // connect sqlcmd
                "We have a problem, because: Wrong number of symbols. Must be 4, but is: 2\r\n" +
                "Repeat please.\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // exit
                "Have a nice day!\r\n", getData());
    }

    @Test
    public void testFindWAfterConnect_withData() {
        // given
        in.add("connect|sqlcmd|postgres|postgres");
        in.add("clear|user");
        in.add("create|user|id|13|name|Stiven|password|*****");
        in.add("create|user|id|14|name|Eva|password|+++++");
        in.add("find|user");
        in.add("exit");

        // when
        Main.main(new String[0]);

        // then
        assertEquals("Hello user!\r\n" +
                "Enter the name of db, name of user and password in format: \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                // connect
                "Success!\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // clear|user
                "Table user was successfully erased.\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // create|user|id|13|name|Stiven|password|*****
                "Notation {\r\n" +
                "names:[id, name, password], values:[13, Stiven, *****]} was successfully created in table 'user'.\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // create|user|id|14|name|Eva|password|+++++
                "Notation {\r\n" +
                "names:[id, name, password], values:[14, Eva, +++++]} was successfully created in table 'user'.\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // find|user
                "---------------\r\n" +
                "|name|password|id|\r\n" +
                "---------------\r\n" +
                "|Stiven|*****|13|\r\n" +
                "|Eva|+++++|14|\r\n" +
                "Enter command (or 'help' for help)\r\n" +
                // exit
                "Have a nice day!\r\n", getData());
    }
}
