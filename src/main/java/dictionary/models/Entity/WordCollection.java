package dictionary.models.Entity;

import dictionary.models.Dao.DatabaseClose;
import dictionary.models.Dao.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class WordCollection {
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private Connection conn = null;
    private String collectionName;
    public WordCollection(String collectionName) {
        this.collectionName = collectionName;
        conn = DatabaseConnection.getConnection();

        StringBuilder createStmt = new StringBuilder("CREATE TABLE ? ( ");
        createStmt.append("id INTEGER PRIMARY KEY,");
        createStmt.append("word TEXT NOT NULL,");
        createStmt.append("pronunciation TEXT,");
        createStmt.append("type TEXT,");
        createStmt.append("meaning TEXT NOT NULL );");
        try {
            preparedStatement = conn.prepareStatement(createStmt.toString());
            preparedStatement.setString(1, collectionName);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DatabaseClose.databaseClose(conn, preparedStatement, resultSet);
    }

    public void adding(String word, String pronunciation, String type, String meaning) {
        conn = DatabaseConnection.getConnection();

        String addStmt = "INSERT INTO ? VALUES(";


    }

    public boolean delete(String word) {
        return true;
    }

}

