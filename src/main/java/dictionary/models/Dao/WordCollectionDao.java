package dictionary.models.Dao;

import dictionary.models.Entity.WordCollection;
import java.sql.*;

public class WordCollectionDao {
    private static Connection conn = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;
    public static void addingWordCollection(String collectionName){
        if (WordCollection.isExist(collectionName)) {
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
    public static void deleteWordCollection(String collectionName){
        WordCollection.collectionNameList.remove(collectionName);
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


}
