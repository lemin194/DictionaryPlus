package dictionary.services;

import dictionary.models.Dao.WordsDao;

import java.io.*;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

public class AutoCorrectWordService {
    /**
     * Calculate Levenshtein Distance between two word to calculate similarity.
     * Formula from Wikipedia: https://en.wikipedia.org/wiki/Levenshtein_distance#Definition
     */
    public int LevenshteinDistance(String a, String b) {
        int n = a.length();
        int m = b.length();
        int[][] f = new int[n+1][m+1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                if (i == 0) {
                    f[i][j] = j;
                } else if (j == 0) {
                    f[i][j] = i;
                } else {
                    f[i][j] = Math.min(f[i][j-1] + 1, f[i-1][j] + 1);
                    if (a.charAt(i-1) == b.charAt(j-1)) {
                        f[i][j] = Math.min(f[i][j], f[i-1][j-1]);
                    }
                }
            }
        }
        return f[n][m];
    }

    public int lengthDiff(String a, String b) {
        int dif = a.length() - b.length();
        if (dif < 0) {
            dif *= -1;
        }
        return dif;
    }

    public double value(double fre, int dis, int lenDif) {
        return (fre + 1) * (dis + lenDif);
    }
    /**
     * Finding the word with minimum distance from the incorrect word.
     */
    public String getCorrectWord(String incorrectWord) {
        List<String> wordList = WordsDao.getAllWord();
        double[] freq = new double[wordList.size()];
        FileReader fileReader = null;
        try {
            int id = 0;
            fileReader = new FileReader(new File("src/main/resources/data/frequencyword.txt"));
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                freq[id] = Float.parseFloat(line);
//                System.out.println(freq[id]);
            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String replaceWord = null;
        double minDistance = Float.MAX_VALUE;
        int i = 0;
        for (String word : wordList) {
            if (minDistance == Integer.MAX_VALUE) {
                replaceWord = word;
                minDistance = value(freq[i],
                        LevenshteinDistance(incorrectWord, replaceWord),
                        lengthDiff(word, incorrectWord));
                continue;
            }

            double currentDistance = value(freq[i],
                    LevenshteinDistance(incorrectWord, word),
                    lengthDiff(incorrectWord, word));

            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                replaceWord = word;
            }
            i++;
        }

        return replaceWord;
    }

    public static void main(String[] args) {
        AutoCorrectWordService autoCorrectWordService = new AutoCorrectWordService();
        System.out.println(autoCorrectWordService.getCorrectWord("dgo"));
    }
}
