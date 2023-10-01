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

    public boolean addWord(Word word) {
        boolean success = false;
        Connection conn = DatabaseConnection.getConnection();
        String stmt = "INSERT INTO WORDS(word,pronunciation,type,meaning) VALUES (?,?,?,?)";
        try {
            preparedStatement = conn.prepareStatement(stmt);
            preparedStatement.setString(1,word.getWord());
            preparedStatement.setString(2,word.getPronunciation());
            preparedStatement.setString(3,word.getType());
            preparedStatement.setString(4,word.getMeaning());

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

    public ArrayList<Word> queryWord(int leftIndex, int rightIndex) {
        ArrayList<Word> wordList = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        int showWords = Math.min(rightIndex - leftIndex + 1, 10);
//        String stmt = String.format("SELECT * FROM av LIMIT %d OFFSET %d", showWords, leftIndex);
        String stmt = "SELECT * FROM av LIMIT ? OFFSET ?";
        try {
            preparedStatement = conn.prepareStatement(stmt);
            preparedStatement.setInt(1, showWords);
            preparedStatement.setInt(2, leftIndex);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String word = resultSet.getString(1);
                ArrayList<String> content = convertFromHTML(resultSet.getString(2));
                String pronunciation = content.get(0);
                String type = content.get(1);
                String meaning = content.get(2);
                wordList.add(new Word(word, pronunciation, type, meaning));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        DatabaseClose.databaseClose(conn, null, null);
        return wordList;
    }


    private static ArrayList<String> convertFromHTML(String initString) {
//        StringBuilder strb = new StringBuilder();
//        StringBuilder current = new StringBuilder();
//        boolean canAdd = true;
//        // first one doesn't count
//        int notFirst = 0;
//        for (int i = 0; i < initString.length(); i++) {
//            if (initString.charAt(i) == '<') {
//                if (notFirst >= 4) {
//                    strb.append(current);
//                    strb.append(" ");
//                    current.setLength(0);
//                } else {
//                    ++notFirst;
//                    if (notFirst == 4) current.setLength(0);
//                }
//                canAdd = false;
//            } else if (initString.charAt(i) == '>') {
//                canAdd = true;
//            } else if (canAdd) {
//                current.append(initString.charAt(i));
//            }
//        }
        Document doc = (org.jsoup.nodes.Document) Jsoup.parse(initString);
        String pronunciation = doc.select("h3 i").text();
        String type = doc.select("h2").text();
        StringBuilder meaning = new StringBuilder();
        Elements allEle = doc.select("ul li");
        for (Element ele : allEle) {
            meaning.append(ele.text());
            meaning.append("\n");
        }

        ArrayList<String> contentList = new ArrayList<>();
        contentList.add(pronunciation);
        contentList.add(type);
        contentList.add(meaning.toString());

        return contentList;
    }


    public static void main(String[] args){
//        Connection conn = DatabaseConnection.getConnection();
//        String query = "SELECT * FROM av LIMIT 50";
//        Statement stmt = null;
//        ResultSet queryResult = null;
//        try {
//            stmt = conn.createStatement();
//            queryResult = stmt.executeQuery(query);
//            while (queryResult.next()) {
//                String word = queryResult.getString(1);
//                ArrayList<String> content = convertFromHTML(queryResult.getString(2));
//
//                System.out.println(word);
//                for (String partition : content) System.out.println(partition);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            DatabaseClose.databaseClose(conn, null, queryResult);
//        }

    }
}
