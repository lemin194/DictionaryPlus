package dictionary.services;

import dictionary.models.Dao.WordsDao;

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

    /**
     * Finding the word with minimum distance from the incorrect word.
     */
    public String getCorrectWord(String incorrectWord) {
        List<String> wordList = WordsDao.getAllWord();

        String replaceWord = null;
        int minDistance = Integer.MAX_VALUE;

        for (String word : wordList) {
            if (minDistance == Integer.MAX_VALUE) {
                replaceWord = word;
                minDistance = LevenshteinDistance(incorrectWord, replaceWord);
                continue;
            }

            int currentDistance = LevenshteinDistance(incorrectWord, word);

            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                replaceWord = word;
            }
        }

        return replaceWord;
    }
}
