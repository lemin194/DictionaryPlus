package dictionary.models.Dao;

import dictionary.models.Entity.Word;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class TestDao {
    // convert av table to anhviet table which easier to use.
    public static void main(String[] args) throws SQLException {
        WordCollectionDao.addCollection("hi");
    }
}
