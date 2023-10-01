package dictionary.models.Entity;

import java.util.ArrayList;
import java.util.HashMap;


public class Dictionary {
    private Trie trie;
    private HashMap<String,Word> dictionary;

    /*
     ** Using trie for operation with words.
     * Using dictionary to retrieve word content.
     */
    public Dictionary() {
        trie = new Trie();
        dictionary = new HashMap<>();
    }

    /*
     ** Adding word into dictionary.
     */
    public void addWord(String word, String pronunciation, String type, String meaning) {
        if (!trie.search(word)) {
            trie.add(word);
            dictionary.put(word,new Word(word, pronunciation, type, meaning));
        }
    }

    /*
     ** Deleting word from dictionary.
     */
    public void deleteWord(String word) {
        if (trie.search(word)) {
            trie.delete(word);
            dictionary.remove(word);
        }
    }

    public ArrayList<Word> searchWord(String prefix) {
        ArrayList<String> words = trie.retrieveWords(prefix);
        ArrayList<Word> fullContentWords = new ArrayList<>();
        for (String word : words) {
            fullContentWords.add(dictionary.get(word));
        }
        return fullContentWords;
    }

    public ArrayList<Word> retrieveAllWord() {
        return searchWord("");
    }

    public void modifyWord(String word, operationType type, String changeWord) {
        if (dictionary.containsKey(word)) {
            Word cur = dictionary.get(word);
            switch (type) {
                case WORD -> cur.setWord(changeWord);
                case PRONUNCIATION -> cur.setPronunciation(changeWord);
                case EXPLANATION -> cur.setExplanation(changeWord);
                case MEANING -> cur.setMeaning(changeWord);
            }
        }
    }
    public void loadFromTextFile(String path) {

    }
    public static void main(String[] args) {
        Dictionary dict = new Dictionary();
        dict.addWord("chad","tʃæd","the piece that you remove when you make a hole in a piece of paper or card","vụn");
        dict.addWord("chador","'tʃɑː.də","a large, usually black cloth worn by some Muslim women to cover their heads and bodies","vải che mặt phụ nữ Hồi giáo");
        dict.addWord("cheap","tʃiːp","costing little money or less than is usual or expected","rẻ");
        dict.addWord("close","kləʊz","to change from being open to not being open, or to cause something to do this","đóng");

        dict.modifyWord("close", operationType.MEANING, "gan gui");
        dict.modifyWord("chad",operationType.WORD, "cha't");
        ArrayList<Word> tra = dict.searchWord("");
        for (Word word : tra) {
            System.out.println(word.getWord() + " " + word.getMeaning());
        }
    }
}