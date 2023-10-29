package dictionary.services;

import dictionary.models.Dao.AllWord;
import dictionary.models.Dao.WordsDao;
import dictionary.models.Entity.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizService {
    private final int quizChoice = 3;
    //We just need 4 words
    public List<Word> generateOneQuiz(String collectionName, Word answer) {
        // if collectionName is null then we randomly choose from past searching results
        List<Word> quizWords = new ArrayList<>();
        if (collectionName == null) {
            Random rand = new Random();
            for (int i = 0; i < quizChoice; i++) {
                while (true) {
                    int randIndex = rand.nextInt(AllWord.amountWord());
                    if (randIndex == 0) randIndex++;
                    Word randWord = WordsDao.queryWordByIndex(randIndex, "anhviet");
                    boolean canAdd = true;
                    for (Word word : quizWords) {
                        if (word.getWord().equals(randWord.getWord())
                                || word.getWord().equals(answer.getWord())) {
                            canAdd = false;
                            break;
                        }
                    }
                    if (canAdd) {
                        quizWords.add(randWord);
                        break;
                    }
                }
            }
        } else {
            List<Word> collectionWords = WordLookUpService.findWord("", collectionName);
            if (quizChoice > collectionWords.size()) {
                for (int i = 0; i < Math.min(collectionWords.size(), quizChoice); i++) {
                    if (!collectionWords.get(i).equals(answer.getWord()))
                      quizWords.add(collectionWords.get(i));
                }
                Random rand = new Random();
                for (int i = 0; i < quizChoice - collectionWords.size(); i++) {
                    while (true) {
                        int randIndex = rand.nextInt(AllWord.amountWord());
                        if (randIndex == 0) randIndex++;
                        Word randWord = WordsDao.queryWordByIndex(randIndex, "anhviet");
                        boolean canAdd = true;
                        for (Word word : quizWords) {
                            if (word.getWord().equals(randWord.getWord())) {
                                canAdd = false;
                                break;
                            }
                        }
                        if (canAdd) {
                            quizWords.add(randWord);
                            break;
                        }
                    }
                }
            }
            Collections.shuffle(collectionWords);
            for (int i = 0; i < Math.min(collectionWords.size(), quizChoice); i++) {
                quizWords.add(collectionWords.get(i));
            }
        }
        return quizWords;
    }

    /*public List<List<Word>> generateManyQuizs(String collectionName, int amountQuiz) {
        List<List<Word>> quizList = new ArrayList<>();
        for (int i = 0; i < amountQuiz; i++) {
            quizList.add(generateOneQuiz(collectionName));
        }
        return quizList;
    }*/
    public static void main(String[] args) {
        QuizService quizSer = new QuizService();
        /*List<List<Word>> testQuizList = quizSer.generateManyQuizs(null, 2);
        for (List<Word> quiz : testQuizList) {
            for (Word word : quiz) {
                System.out.println(word);
            }
        }*/
    }
}
