package dictionary.models.Entity;

import java.util.ArrayList;
import java.util.HashMap;

public class WordCollection {
    private ArrayList<Word> wordList;
    private HashMap<String,Integer> index;

    public WordCollection() {
        wordList = new ArrayList<>();
        index = new HashMap<>();
    }

    public int findWordPosition(String word) {
        if (index.containsKey(word)) {
            Integer id = index.get(word);
            return (int)id;
        }
        return -1;
    }

    public void adding(String word, String pronunciation, String type, String meaning) {
        int id = findWordPosition(word);
        if (id != -1) {
            wordList.get(id).modify(meaning);
        } else {
            Word newWord = new Word(word, pronunciation, type, meaning);
            wordList.add(newWord);
            index.put(word, wordList.size() - 1);
        }
    }

    public boolean delete(String word) {
        int id = findWordPosition(word);
        if (id != -1) {
            wordList.remove(id);
            index.remove(word);
            return true;
        } else {
            return false;
        }
    }

}

