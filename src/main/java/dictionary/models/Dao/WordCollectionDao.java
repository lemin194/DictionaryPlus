package dictionary.models.Dao;

import dictionary.models.Entity.Word;
import dictionary.models.Entity.WordCollection;
import dictionary.models.Entity.WordCollectionManagement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class WordCollectionDao {
    private static Connection conn = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    public static void addCollection(String collectionName){
        if (checkCollectionExist(collectionName)) {
            return;
        }
        conn = DatabaseConnection.getConnection();
        StringBuilder createStmt = new StringBuilder(String.format("CREATE TABLE %s ( ", collectionName));
        createStmt.append("id INTEGER PRIMARY KEY,");
        createStmt.append("word TEXT NOT NULL,");
        createStmt.append("pronunciation TEXT,");
        createStmt.append("type TEXT,");
        createStmt.append("meaning TEXT NOT NULL );");
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(createStmt.toString());
            WordCollectionManagement.addCollection(collectionName);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseClose.databaseClose(conn, preparedStatement, null);
        }
    }

    public static void deleteCollection(String collectionName){
        if (!checkCollectionExist(collectionName)) {
            return;
        }
        WordCollectionManagement.deleteCollection(collectionName);
        conn = DatabaseConnection.getConnection();
        String deleteStmt = String.format("DROP TABLE %s;", collectionName);
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(deleteStmt);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseClose.databaseClose(conn, preparedStatement, null);
        }
    }

    public static void addWordForCollection(Word word, String collectionName) {
        if (!checkCollectionExist(collectionName)) {
            return;
        }
        WordsDao.addWord(word, collectionName);
    }

    public static void deleteWordFromCollection(String word, String collectionName) {
        if (!checkCollectionExist(collectionName)) {
            System.out.println("khong ton tai bang nay (trong luc xoa)");
            return;
        }
        conn = DatabaseConnection.getConnection();
        String stmt = "DELETE FROM " + collectionName + " WHERE word = ?;";
        try {
            preparedStatement = conn.prepareStatement(stmt);
            preparedStatement.setString(1, word);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseClose.databaseClose(conn, preparedStatement, resultSet);
        }
        System.out.println("xoa thanh cong");
    }

    public static void modifyWordFromCollection(String word, String modifyType, String modifyStr, String collectionName) {
        if (!checkCollectionExist(collectionName)) {
            return;
        }
        conn = DatabaseConnection.getConnection();
        String stmt = "UPDATE " + collectionName + " SET " + modifyType + " = ? WHERE word = ?";
        try {
            preparedStatement = conn.prepareStatement(stmt);
            preparedStatement.setString(1, modifyStr);
            preparedStatement.setString(2, word);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseClose.databaseClose(conn, preparedStatement, resultSet);
        }
    }

    public static boolean checkCollectionExist(String collectionName) {
        String stmt = String.format("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='%s'", collectionName);
        conn = DatabaseConnection.getConnection();
        try {
            Statement call = conn.createStatement();
            resultSet = call.executeQuery(stmt);
            return resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseClose.databaseClose(conn, preparedStatement, resultSet);
        }
    }

    public static void main(String[] args) {
    }
}
