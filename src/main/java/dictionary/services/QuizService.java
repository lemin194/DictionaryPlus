package dictionary.services;

import dictionary.models.Dao.AllWord;
import dictionary.models.Dao.WordsDao;
import dictionary.models.Entity.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizService {
    private final int quizChoice = 4;
    //We just need 4 words
    public List<Word> generateOneQuiz(String collectionName, Word answer) {
        // if collectionName is null then we randomly choose from past searching results
        List<Word> quizWords = new ArrayList<>();
        // case 1: never happened. Theo bon m, co nen cho phep choi ma k can chon collection k?
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
        } //case 2: collection co ton tai va duoc nguoi choi chon
        else {

            List<Word> collectionWords = WordLookUpService.findWord("", collectionName);
            // case 2.1: collection chi co 0,1,2,3 word => khong du de lam 4 dap an (3 word thi 1 tu lam answer roi thieu 1 dap an)
            if (quizChoice > collectionWords.size() - 1) {
                Collections.shuffle(collectionWords);
                // n dap an gio day them thanh n - 1 dap an
                for (int i = 0; i < collectionWords.size(); i++) {
                    if (!collectionWords.get(i).getWord().equals(answer.getWord()))
                      quizWords.add(collectionWords.get(i));
                }
                Random rand = new Random();
                //
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
                        if (randWord.getWord().equals(answer.getWord())) canAdd = false;
                        if (canAdd) {
                            quizWords.add(randWord);
                            break;
                        }
                    }
                }
            } else {
                Collections.shuffle(collectionWords);
                int added = 0;
                for (int i = 0; i < collectionWords.size(); i++) {
                    if (added == quizChoice) break;
                    if (!collectionWords.get(i).getWord().equals(answer.getWord())) {
                        quizWords.add(collectionWords.get(i));
                        added++;
                    }
                }
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
