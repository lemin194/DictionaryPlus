package dictionary.models.Dao;

import dictionary.models.Entity.Word;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class WordCollectionDao {
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public boolean addWord(Word word) {
        boolean success = false;
        Connection conn = DatabaseConnection.getConnection();

        return success;
    }
}
