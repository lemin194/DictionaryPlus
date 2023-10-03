package dictionary.models.Dao;

import java.sql.*;
import java.io.Console;
public class DatabaseConnection {
    private static final String DBPATH = "jdbc:sqlite:src/main/resources/data/dict_hh.db";
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DBPATH);
            System.out.println("Connect success");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        Connection conn = getConnection();
        DatabaseClose.databaseClose(conn, null, null);

    }
}
