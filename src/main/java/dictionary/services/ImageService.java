package dictionary.services;

import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ImageService {
    public static List<String> wordList = new ArrayList<>();
    private static final String filePath = "src/main/resources/data/wordlist.txt";
    static {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                wordList.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Image loadImage(String name) {
        Image res = new Image(getClass().getResourceAsStream("/data/images/" + name + ".jpg"));
        return res;
    }

    public static List<String> randWord() {
        List<String> listRandomWord = new ArrayList<>();
        Set<Integer> alreadyAppearIndex = new HashSet<>();
        Random rand = new Random();
        int wordListSize = wordList.size();
        for (int i = 0; i < 4; i++) {
            while (true) {
                int randIndex = rand.nextInt(wordListSize);
                if (!alreadyAppearIndex.contains(randIndex)) {
                    alreadyAppearIndex.add(randIndex);
                    listRandomWord.add(wordList.get(randIndex));
                    break;
                }
            }
        }
        return listRandomWord;
    }

    public static void main(String[] args) {
        wordList.forEach(System.out::println);
    }


}
