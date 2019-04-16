package ua.com.juja.sqlcmd.model;

import java.sql.*;
import java.util.Random;

public class Main {
    public static void main(String[] argv) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5434/sqlcmd", "postgres",
                    "postgres");
        // INSERT
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO public.user (name, password) " +
                "VALUES ('Stiven', 'Pupkin')");
        stmt.close();

        // SELECT
        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM public.user WHERE id > 10");
        while (rs.next()) {
            System.out.println("id:" + rs.getString("id"));
            System.out.println("name: " + rs.getString("name"));
            System.out.println("password: " + rs.getString("password"));
            System.out.println("-------");
        }
        rs.close();
        stmt.close();

        // TABLE NAMES
        stmt = connection.createStatement();
        rs = stmt.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='public'");
        while (rs.next()) {
            System.out.println(rs.getString("TABLE_NAME"));
        }
        rs.close();
        stmt.close();

        // DELETE
        stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM public.user " +
                "WHERE id > 50 AND id < 100");
        stmt.close();

        // UPDATE
        PreparedStatement ps = connection.prepareStatement(
                "UPDATE public.user SET password = ? WHERE id > 3");
        ps.setString(1, "password_" + new Random().nextInt());
        ps.executeUpdate();
        ps.close();

        connection.close();
    }
}