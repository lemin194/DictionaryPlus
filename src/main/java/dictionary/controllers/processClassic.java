package dictionary.controllers;

import dictionary.models.Dao.WordCollectionDao;
import dictionary.models.Dao.WordsDao;
import dictionary.models.Entity.Word;
import dictionary.apiservices.TTSService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class processClassic extends process {
    @FXML
    public void initialize(URL location, ResourceBundle Resources) {
        fixedNameCollection = ReviewPlayController.currentCollection;
        current = WordCollectionDao.queryWordInCollection(fixedNameCollection);
        if(ReviewPlayController.currentSortBy.equals("Alphabetically")) {
            WordCollectionDao.sortWord(current);
        } else if (ReviewPlayController.currentSortBy.equals("History")) {

        } else if (ReviewPlayController.currentSortBy.equals("Randomly")) {
            Collections.shuffle(current);
        }
        int tmp = current.size();
        for (int i = 0; i < tmp - ReviewPlayController.numCards; i++) {
            current.remove(tmp - 1 - i);
        }
        showAnswer.setVisible(true);
        transfer.setVisible(false);
        idOfDisplayWord = 0;
        fakeNumberOfWordsinCollection = current.size();
        wordsLeft = fakeNumberOfWordsinCollection;
        progress.setText(wordsLeft + "/" + fakeNumberOfWordsinCollection);
        displayWord = current.get(idOfDisplayWord);
        currentWord.setText(displayWord.getWord() + "\n" + displayWord.getPronunciation());
        done.setVisible(false);
    }
    @FXML
    public void showAnswer() {
        if (current.size() != 0) {
            infoOfWord.setText(displayWord.getMeaning());
            transfer.setVisible(true);
            showAnswer.setVisible(false);
        }
    }
    @FXML
    public void easyButton() {
        try {
            if (!WordCollectionDao.queryWordInCollection(fixedNameCollection).isEmpty()) {
                WordCollectionDao.deleteWordFromCollection(displayWord.getWord(), fixedNameCollection);
                current.remove(idOfDisplayWord);
                wordsLeft = current.size();
                progress.setText(wordsLeft + "/" + fakeNumberOfWordsinCollection);
            }
            if (wordsLeft > 0) {
                displayWord = current.get(idOfDisplayWord);
                currentWord.setText(displayWord.getWord() + "\n" + displayWord.getPronunciation());
                infoOfWord.setText("");
                transfer.setVisible(false);
                showAnswer.setVisible(true); }
            else {
                speaker.setVisible(false);
                currentWord.setVisible(false);
                infoOfWord.setVisible(false);
                progress.setVisible(false);
                transfer.setVisible(false);
                showAnswer.setVisible(false);
                done.setVisible(true);
            }
            //currentWord.setText("");
            //infoOfWord.setText("");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void hardButton() {
        progress.setText(wordsLeft + "/" + fakeNumberOfWordsinCollection);
        int len = current.size();
        idOfDisplayWord++;
        if(idOfDisplayWord >= len) idOfDisplayWord = 0;
        displayWord = current.get(idOfDisplayWord);
        currentWord.setText(displayWord.getWord() + "\n" + displayWord.getPronunciation());
        infoOfWord.setText("");
        transfer.setVisible(false);
        showAnswer.setVisible(true);
    }
}
