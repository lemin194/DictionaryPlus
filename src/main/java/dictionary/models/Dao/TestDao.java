package dictionary.models.Dao;

import dictionary.models.Entity.Word;

import java.sql.SQLException;

public class TestDao {
    // convert av table to anhviet table which easier to use.

    public static void main(String[] args) throws SQLException, InterruptedException {
//        WordCollectionDao.addCollection("troll");
//        WordCollectionDao.addCollection("yomama");
        WordCollectionDao.deleteCollection("yomama");


    }
}
