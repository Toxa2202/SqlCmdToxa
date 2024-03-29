package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ClearTest {
    private DatabaseManager manager;
    private View view;
    private Command command;


    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Clear(manager, view);
    }

    @Test
    public void testClearTable() {
        // given

        // when
        command.process("clear|user");

        // then
        verify(manager).clear("user");
        verify(view).write("Table user was successfully erased.");
    }

    @Test
    public void testCanProcessClearWithParametersString() {
        // when
        boolean canProcess = command.canProcess("clear|user");

        // then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessClearWithoutParametersString() {
        // when
        boolean canProcess = command.canProcess("clear");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testCantProcessQweString() {
        // when
        boolean canProcess = command.canProcess("qwe|user");

        // then
        assertFalse(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThen2() {
        // when
        try {
            command.process("clear");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Format of command " +
                    "'clear|tableName', but you write: clear", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersIsMoreThen2() {
        // when
        try {
            command.process("clear|table|qwe");
            fail();
        } catch (IllegalArgumentException e) {
            // then
            assertEquals("Format of command " +
                    "'clear|tableName', but you write: clear|table|qwe", e.getMessage());
        }
    }
}
