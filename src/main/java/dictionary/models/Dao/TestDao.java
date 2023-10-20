package dictionary.models.Dao;

import dictionary.models.Entity.Word;

import java.sql.SQLException;
import java.util.List;

public class TestDao {
    // convert av table to anhviet table which easier to use.

    public static void main(String[] args) throws SQLException, InterruptedException {
//        WordCollectionDao.addCollection("troll");
        WordCollectionDao.addCollection("yomama");
        WordsDao.addWord(new Word("yomama", "domama", "danh tu", "masfdasdf"),
                "yomama");
        WordsDao.addWord(new Word("yomama2", "domama", "danh tu", "masfdasdf"),
                "yomama");

        List<Word> res = WordCollectionDao.queryWordInCollection("yomama");
        for (Word word : res) System.out.println(word);
    }
}
