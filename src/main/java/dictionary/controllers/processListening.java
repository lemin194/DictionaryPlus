package dictionary.controllers;

import dictionary.models.Dao.WordCollectionDao;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class processListening extends process {
    @FXML
    public AnchorPane tmpAnchor = new AnchorPane();
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
        tmpAnchor.setVisible(true);
        showAnswer.setVisible(true);
        transfer.setVisible(false);
        idOfDisplayWord = 0;
        fakeNumberOfWordsinCollection = current.size();
        wordsLeft = fakeNumberOfWordsinCollection;
        progress.setText(wordsLeft + "/" + fakeNumberOfWordsinCollection);
        displayWord = current.get(idOfDisplayWord);
        currentWord.setText("");
        done.setVisible(false);
    }
    @FXML
    public void showAnswer() {
        if (current.size() != 0) {
            infoOfWord.setText(displayWord.getMeaning());
            currentWord.setText(displayWord.getWord() + "\n" + displayWord.getPronunciation());
            transfer.setVisible(true);
            showAnswer.setVisible(false);
        }
    }
    @FXML
    public void easyButton() {
        try {
            if (!WordCollectionDao.queryWordInCollection(fixedNameCollection).isEmpty()) {
                WordCollectionDao.deleteWordFromCollection(displayWord.getWord(), fixedNameCollection);
                if (idOfDisplayWord == current.size() - 1) {
                    current.remove(idOfDisplayWord);
                    idOfDisplayWord--;
                } else if (idOfDisplayWord < current.size() - 1) {
                    current.remove(idOfDisplayWord);
                }
                wordsLeft = current.size();
                progress.setText(wordsLeft + "/" + fakeNumberOfWordsinCollection);
            }
            if (current.size() > 0) {
                displayWord = current.get(idOfDisplayWord);
                currentWord.setText("");
                infoOfWord.setText("");
                transfer.setVisible(false);
                showAnswer.setVisible(true); }
            else if (current.size() == 0){
                tmpAnchor.setVisible(false);
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
        currentWord.setText("");
        infoOfWord.setText("");
        transfer.setVisible(false);
        showAnswer.setVisible(true);
    }
}


