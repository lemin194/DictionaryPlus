package dictionary.services;

import dictionary.models.Entity.Word;
import dictionary.models.Dao.WordsDao;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordLookUpService {
    private static final int cacheSize = 10;
    private static Cache cache = new Cache(cacheSize);

    public static List<Word> retrieveLastSearch() {
        return cache.retrieve();
    }

    public static List<Word> findWord(String prefix, String tableName) {
        prefix = prefix.toLowerCase();
        return WordsDao.queryWord(prefix, tableName);
    }

    public static void addWord(Word word) {
        cache.add(word);
    }
    public static void start() {
        cache.read();
    }

    public static void close() {
        cache.write();
    }

    public static void main(String[] args) {
        WordLookUpService wlkService = new WordLookUpService();
        for (Word word : wlkService.retrieveLastSearch()) {
            System.out.println(word);
        }
        wlkService.close();
    }
}

// cache LIFO
class Cache {
    private final String filePATH = "src/main/resources/data/lastFind.txt";
    private final int maxSize;
    private List<Word> cache;
    public Cache(int maxSize) {
        this.maxSize = maxSize;
        cache = new ArrayList<>();
    }

    void write() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePATH))) {
            for (Word word : cache) {
                String content = word.getWord() + "~" + word.getPronunciation() + "~"
                        + word.getType() + "~" + word.getMeaning() + "$";
                writer.write(content);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    void read() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePATH))) {
            StringBuilder all = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                all.append(line);
            }
            String text = all.toString();
            String[] arr = text.split("\\$");
            for (int i  = 0; i < arr.length; i++) {
                String[] cur = arr[i].split("~");
                List<String> list = new ArrayList<>(Arrays.stream(cur).toList());
                while (list.size() < 4) {
                    list.add("");
                }
                Word word = new Word(list.get(0), list.get(1), list.get(2), list.get(3));
                cache.add(word);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        };
    }
    public void add(Word word) {
        for (Word wd : cache) {
            if (wd.getWord().equals(word.getWord())) {
                cache.remove(wd);
                cache.add(0, wd);
                return;
            }
        }

        if (cache.size() == maxSize) {
            removeOldest();
        }

        if (cache.isEmpty()) {
            cache.add(word);
        } else {
            cache.add(0, word);
        }
    }

    public void removeOldest() {
        cache.remove(maxSize - 1);
    }
    public List<Word> retrieve() {
        return cache;
    }


}