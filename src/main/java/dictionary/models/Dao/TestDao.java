package dictionary.models.Dao;

import dictionary.models.Entity.Word;

import java.util.ArrayList;

public class TestDao {
    public static void main(String[] args) {
        WordsDao wrd = new WordsDao();
        ArrayList<Word> testList = wrd.queryWord(1, 3);
        for (Word x : testList) {
            System.out.print(x);
        }
    }
}
