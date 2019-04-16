package ua.com.juja.sqlcmd.model;

import java.sql.*;
import java.util.Arrays;

public class JDBCDatabaseManager implements DatabaseManager {
    private Connection connection;

    @Override
    public DataSet[] getTableData(String tableName) {
        try {

            int size = getSize(tableName);

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM public." + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            DataSet[] result = new DataSet[size];
            int index = 0;
            while (rs.next()) {
                DataSet dataSet = new DataSet();
                result[index++] = dataSet;
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    dataSet.put(rsmd.getColumnName(i), rs.getObject(i));
                }
            }
            rs.close();
            stmt.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new DataSet[0];
        }
    }

    private int getSize(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM public." + tableName);
        rsCount.next();
        int size = rsCount.getInt(1);
        rsCount.close();
        return size;
    }

    @Override
    public String[] getTableNames(){
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='public'");
            String[] tables = new String[100];
            int index = 0;
            while (rs.next()) {
                tables[index++] = rs.getString("TABLE_NAME");
            }
            tables = Arrays.copyOf(tables, index, String[].class);
            rs.close();
            stmt.close();
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    @Override
    public void connect(String database, String userName, String password){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Please add jdbc jar to project.", e);
        }

        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5434/" + database, userName,
                    password);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException(
                    String.format("Can't get connection for model:%s user:%s",
                            database, userName),
                    e);
        }
    }

    @Override
    public void clear(String tableName) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM public." + tableName);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(String tableName, DataSet input) {
        try {
            Statement stmt = connection.createStatement();

            String tableNames = getNamesFormatted(input, "%s,");
            String values = getValuesFormatted(input, "'%s',");

            stmt.executeUpdate("INSERT INTO public." + tableName + " (" + tableNames + ") " +
                    "VALUES (" + values + ")");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getValuesFormatted(DataSet input, String format) {
        String values = "";
        for (Object value : input.getValues()) {
            values += String.format(format, value);
        }
        values = values.substring(0, values.length() - 1);
        return values;
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
        try {
            String tableNames = getNamesFormatted(newValue, "%s = ?,");

            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE public." + tableName + " SET " + tableNames + " WHERE id = ?");
            int index = 1;
            for (Object value : newValue.getValues()) {
                ps.setObject(index, value);
                index++;
            }
            ps.setInt(index, id);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getNamesFormatted(DataSet newValue, String format) {
        String string = "";
        for (String name : newValue.getNames()) {
            string += String.format(format, name);
        }
        string = string.substring(0, string.length() - 1);
        return string;
    }
}