package dictionary.models.Dao;

import dictionary.models.Entity.Word;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class TestDao {
    public static void main(String[] args) throws SQLException {
        WordsDao wrd = new WordsDao();
        ArrayList<Word> testList = wrd.queryWord("");
        Connection conn = DatabaseConnection.getConnection();
        String adding = "INSERT INTO anhviet(word,pronunciation,type,meaning) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(adding);
        for (Word word : testList) {
            preparedStatement.setString(1,word.getWord());
            preparedStatement.setString(2,word.getPronunciation());
            preparedStatement.setString(3,word.getType());
            preparedStatement.setString(4,word.getMeaning());
            preparedStatement.execute();
        }

    }
}
