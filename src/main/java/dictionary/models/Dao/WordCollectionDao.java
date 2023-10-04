package dictionary.models.Dao;

import dictionary.models.Entity.Word;
import dictionary.models.Entity.WordCollection;
import dictionary.models.Entity.WordCollectionManagement;

import java.sql.*;

public class WordCollectionDao {
    private static Connection conn = null;
    private static PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public static void addCollection(String collectionName){
        if (WordCollectionManagement.isExist(collectionName)) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseClose.databaseClose(conn, preparedStatement, null);
        }
    }
    public static void deleteCollection(String collectionName){
        if (!WordCollectionManagement.isExist(collectionName)) {
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
        if (!WordCollectionManagement.isExist(collectionName)) {
            return;
        }
        int tableIndex = WordCollectionManagement.findCollectionByName(collectionName);
        WordCollectionManagement.allCollection.get(tableIndex).addNewWord(word);
        WordsDao.addWord(word, collectionName);
    }

    public static void deleteWordFromCollection(String word, String collectionName) {
        if (!WordCollectionManagement.isExist(collectionName)) {
            return;
        }
        int tableIndex = WordCollectionManagement.findCollectionByName(collectionName);
        WordCollectionManagement.allCollection.get(tableIndex).deleteWord(word);
        WordsDao.deleteWord(word, collectionName);
    }

    public static void modifyWordFromCollection(String word, String modifyType, String modifyStr, String collectionName) {
        if (!WordCollectionManagement.isExist(collectionName)) {
            return;
        }
        int tableIndex = WordCollectionManagement.findCollectionByName(collectionName);
        WordCollectionManagement.allCollection.get(tableIndex).modifyWord(word, modifyType, modifyStr);
    }

}
