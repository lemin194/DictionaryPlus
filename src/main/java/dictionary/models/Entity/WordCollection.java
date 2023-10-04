package dictionary.models.Entity;
import java.util.*;

public class WordCollection {

    private String collectionName;
    private List<Word> wordList;
    private Set<String> wordSet;
    public WordCollection(String collectionName) {
        wordList = new ArrayList<>();
        wordSet = new HashSet<>();
        this.collectionName = collectionName;
    }

    public List<Word> getWordList() {
        return wordList;
    }

    public int findIndex(String word) {
        for (int i = 0; i < wordList.size(); i++) {
            if (wordList.get(i).getWord().equals(word)) {
                return i;
            }
        }
        return -1;
    }
    public void addNewWord(Word word) {
        if (wordSet.contains(word.getWord())) {
            System.out.printf("Word %s has already appeared in collection\n", word.getWord());
            return;
        }

        wordList.add(word);
        wordSet.add(word.getWord());
    }

    public void deleteWord(String word) {
        if (!wordSet.contains(word)) {
            System.out.printf("Word %s not exist in collection\n", word);
            return;
        }

        wordSet.remove(word);
        wordList.remove(findIndex(word));
    }

    public void modifyWord(String word, String modifyType, String modifyStr) {
        int index = findIndex(word);
        if (index == -1) {
            return;
        }

        switch (modifyType) {
            case "pronunciation":
                wordList.get(index).setPronunciation(modifyStr);
                break;
            case "type":
                wordList.get(index).setType(modifyStr);
                break;
            case "meaning":
                wordList.get(index).setMeaning(modifyStr);
                break;
            default:
                System.out.println("There's no modify type like this");
        }
    }
}


