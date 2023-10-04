package dictionary.models.Dao;

import dictionary.models.Entity.Word;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class WordCollectionDao {
    public static void adding(Word word, String collectionName) {
        WordsDao.addWord(word, collectionName);
    }

    public static boolean delete(String word, String collectionName) {
        return WordsDao.deleteWord(word, collectionName);
    }

    public static boolean modify(String word, String modifyType, String modifyStr, String collectionName) {
        return WordsDao.modifyWord(word, modifyType, modifyStr, collectionName);
    }

    public static ArrayList<Word> query(String word, String collectionName) {
        return WordsDao.queryWord(word, collectionName);
    }
}
