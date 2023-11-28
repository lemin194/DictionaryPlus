package dictionary.models.Dao;

import dictionary.models.Entity.Word;
import dictionary.services.WordOperationService;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* How to use ?
- If you want to add word to specific table, using addWord method
Example: addWord(golang, anhviet) => this will add golang into table anhviet

- If you want to delete word from specific table, using deleteWord method
Example: deleteWord(golang, anhviet) => this will delete golang from table anhviet (in case exist)

- If you want to modify word in specific table, using modifyWord method
Example: modifyWord(golang, meaning, orz, anhviet) => this will modify golang meaning to orz.

- If you want to query word in specific table, using queryWord method
Example: queryWord("he", anhviet) => this will return a list of Word object that contain
prefix "he" like "hello", "help", "height", etc.

Also take care of object type passing in each method
In case object ype is Word => fast passing like this new Word(word,pronunciation,type,meaning)
 */
public class WordsDao {
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;
    private static Connection conn = null;

    /**
     * Check if this word exists in table.
     */
    public static boolean checkWordExist(String word, String table) {
        conn = DatabaseConnection.getConnection();
        String countStmt = String.format("SELECT COUNT(*) FROM %s WHERE word = \"%s\"", table, word);
        int cnt = 0;
        try {
            Statement stmt = conn.createStatement();
            resultSet  = stmt.executeQuery(countStmt);
            cnt = resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println("There's no word like that");
        } finally {
            DatabaseClose.databaseClose(conn, preparedStatement, resultSet);
        }
        return cnt == 1;
    }

    /**
     * Adding word into specific table.
     */
    public static boolean addWord(Word word, String table) {
        boolean isSuccess = false;
        int id = AllWord.leftMostIndex(word.getWord());

        if (checkWordExist(word.getWord(), table)) {
            System.out.println("This word already in dictionary");
            return false;
        }

        conn = DatabaseConnection.getConnection();
        String stmt = "INSERT INTO " + table + "(word, pronunciation, type, meaning) VALUES (?, ?, ?, ?)";
        try {
            preparedStatement = conn.prepareStatement(stmt);
            preparedStatement.setString(1, word.getWord());
            preparedStatement.setString(2, word.getPronunciation());
            preparedStatement.setString(3, word.getType());
            preparedStatement.setString(4, word.getMeaning());
            if (preparedStatement.executeUpdate() > 0) {
                isSuccess = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("add word to collection failed");
        } finally {
            DatabaseClose.databaseClose(conn, preparedStatement, resultSet);
        }

        // just in case anhviet dictionary, no need for collection to use AllWord
        if (table.equals("anhviet")) {
            AllWord.addWord(word.getWord());
        }

        return isSuccess;
    }

    /**
     * Delete word from specific table.
     */
    public static boolean deleteWord(String word, String table) {
        int index = AllWord.leftMostIndex(word);
        if (index == -1) {
            return false;
        }
        int tableID = AllWord.tableID(index);
        conn = DatabaseConnection.getConnection();

        String stmt = "DELETE FROM " + table + " WHERE id = ?;";
        try {
            preparedStatement = conn.prepareStatement(stmt);
            preparedStatement.setInt(1, tableID);
            preparedStatement.execute();
            WordOperationService.delete(word);
            AllWord.deleteWord(index);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseClose.databaseClose(conn, preparedStatement, resultSet);
        }
        return true;
    }

    /**
     * Query word from specific table.
     */
    public static ArrayList<Word> queryWord(String pref, String table, int maxShowWords) {
        ArrayList<Integer> bound = AllWord.wordsContainPrefix(pref);
        int leftIndex = bound.get(0);
        int rightIndex = Math.min(bound.get(1), leftIndex + maxShowWords - 1);
        ArrayList<Word> wordList = new ArrayList<>();
        if (leftIndex == -1) {
            return wordList;
        }
        conn = DatabaseConnection.getConnection();
        String stmt = "SELECT * FROM " + table + " WHERE id = ?";
        try {
            preparedStatement = conn.prepareStatement(stmt);
            for (int i = leftIndex; i <= rightIndex; i++) {
                int tableID = AllWord.tableID(i);
                preparedStatement.setInt(1, tableID);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String word = resultSet.getString(2);
                    String pronunciation = resultSet.getString(3);
                    String type = resultSet.getString(4);
                    String meaning = resultSet.getString(5);
                    wordList.add(new Word(word, pronunciation, type, meaning));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseClose.databaseClose(conn, preparedStatement, resultSet);
        }
        return wordList;
    }

    public static boolean modifyWord(String word, String modifyType, String modifyStr, String table) {
        int index = AllWord.leftMostIndex(word);
        if (index == -1) {
            // this word is not in table
            return false;
        }
        index = AllWord.tableID(index);
        conn = DatabaseConnection.getConnection();
        String modifyStmt = "UPDATE " + table + " SET " + modifyType + " = ? WHERE id = ?";
        try {
            preparedStatement = conn.prepareStatement(modifyStmt);
            preparedStatement.setString(1, modifyStr);
            preparedStatement.setInt(2, index);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseClose.databaseClose(conn, preparedStatement, resultSet);
        }
        return true;
    }

    // this use for query word by index
    public static Word queryWordByIndex(int index, String table) {
        String query = String.format("SELECT * FROM %s WHERE id = %d", table, index);
        conn = DatabaseConnection.getConnection();
        Word result = null;
        try {
            Statement stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                String word = resultSet.getString(2);
                String pronunciation = resultSet.getString(3);
                String type = resultSet.getString(4);
                String meaning = resultSet.getString(5);
                result = new Word(word, pronunciation, type, meaning);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DatabaseClose.databaseClose(conn, preparedStatement, resultSet);
        }
        return result;
    }

    public static List<String> getAllWord() {
        List<String> res = new ArrayList<>();
        for (IndexWord indexWord : AllWord.getWords()) {
            res.add(indexWord.getWord());
        }
        return res;
    }
}
