package dictionary.models.Dao;

import dictionary.models.Entity.Word;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.sql.*;
import java.util.ArrayList;

public class WordsDao {
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public boolean addWord(Word word, String table) {
        boolean success = false;
        Connection conn = DatabaseConnection.getConnection();
        String stmt = "INSERT INTO ? (word,pronunciation,type,meaning) VALUES (?,?,?,?)";
        try {
            preparedStatement = conn.prepareStatement(stmt);
            preparedStatement.setString(1, table);
            preparedStatement.setString(2,word.getWord());
            preparedStatement.setString(3,word.getPronunciation());
            preparedStatement.setString(4,word.getType());
            preparedStatement.setString(5,word.getMeaning());

            if (preparedStatement.executeUpdate() > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseClose.databaseClose(conn,preparedStatement,null);
        }
        return success;
    }

    public boolean deleteWord(String word, String table) {
        Connection conn = DatabaseConnection.getConnection();
        ArrayList<Integer> couple = AllWord.wordsContainPrefix(word);
        int leftIndex = couple.get(0);
        int rightIndex = couple.get(1);
        if (leftIndex == -1) {
            return false;
        }
        String stmt = "DELETE FROM ? WHERE id BETWEEN ? AND ?";
        try {
            preparedStatement = conn.prepareStatement(stmt);
            preparedStatement.setString(1, table);
            preparedStatement.setInt(2, leftIndex);
            preparedStatement.setInt(3, rightIndex);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseClose.databaseClose(conn, preparedStatement, null);
        }
        return true;
    }
    
    public ArrayList<Word> queryWord(String pref, String table) {
        ArrayList<Integer> bound = AllWord.wordsContainPrefix(pref);
        int leftIndex = bound.get(0);
        int rightIndex = bound.get(1);
        ArrayList<Word> wordList = new ArrayList<>();
        if (leftIndex == -1) {
            return wordList;
        }
        Connection conn = DatabaseConnection.getConnection();
        int showWords = rightIndex - leftIndex + 1;
//        String stmt = String.format("SELECT * FROM av LIMIT %d OFFSET %d", showWords, leftIndex);
        String stmt = "SELECT * FROM ? LIMIT ? OFFSET ?";
        try {
            preparedStatement = conn.prepareStatement(stmt);
            preparedStatement.setString(1, table);
            preparedStatement.setInt(2, showWords);
            preparedStatement.setInt(3, leftIndex);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String word = resultSet.getString(2);
//                ArrayList<String> content = convertFromHTML(resultSet.getString(3));
                String pronunciation = resultSet.getString(3);
                String type = resultSet.getString(4);
                String meaning = resultSet.getString(5);
                wordList.add(new Word(word, pronunciation, type, meaning));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseClose.databaseClose(conn, null, null);
        return wordList;
    }


//    private static ArrayList<String> convertFromHTML(String initString) {
//        Document doc = (org.jsoup.nodes.Document) Jsoup.parse(initString);
//        String pronunciation = doc.select("h3 i").text();
//        String type = doc.select("h2").text();
//        StringBuilder meaning = new StringBuilder();
//        Elements allEle = doc.select("ul li");
//        for (Element ele : allEle) {
//            meaning.append(ele.text());
//            meaning.append("\n");
//        }
//
//        ArrayList<String> contentList = new ArrayList<>();
//        contentList.add(pronunciation);
//        contentList.add(type);
//        contentList.add(meaning.toString());
//
//        return contentList;
//    }
}
