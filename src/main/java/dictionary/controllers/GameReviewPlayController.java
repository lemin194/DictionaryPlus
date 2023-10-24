package dictionary.controllers;

import dictionary.models.Dao.WordCollectionDao;
import dictionary.models.Dao.WordsDao;
import dictionary.models.Entity.Word;
import dictionary.services.TextToSpeech;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import dictionary.models.Dao.WordCollectionDao.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;


import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import java.util.ResourceBundle;

import dictionary.models.Entity.Word;


public class GameReviewPlayController implements Initializable {
    @FXML
    private Label currentWord = new Label();
    @FXML
    private Label pronounciation = new Label();
    @FXML
    private Button speaker = new Button();
    @FXML
    private TextArea infoOfWord = new TextArea();
    @FXML
    private ChoiceBox<String> collectionsToStudy = new ChoiceBox<>();
    @FXML
    private Button startGame = new Button();
    @FXML
    private Label progress = new Label();
    @FXML
    private Button easy = new Button();
    @FXML
    private Button hard = new Button();
    @FXML
    private Button medium = new Button();
    @FXML
    private Button answer = new Button();

    private String currentCollection = "";
    private Word displayWord;
    private int idOfDisplayWord;
    private int fakeNumberOfWordsinCollection;

    private int wordsLeft;
    @FXML
    private AnchorPane transfer = new AnchorPane();

    private List<Word> current = new ArrayList<>();

    @FXML
    public void initialize(URL location, ResourceBundle Resources) {
        int n = WordCollectionDao.queryCollectionName().size();
        for (int i = 0; i < n; i++) {
            collectionsToStudy.getItems().add(WordCollectionDao.queryCollectionName().get(i));
        }
        transfer.setVisible(false);
        answer.setVisible(true);
    }
    @FXML
    public void startGame() {
        try {
            currentCollection = collectionsToStudy.getValue();
            current = WordCollectionDao.queryWordInCollection(currentCollection);
            fakeNumberOfWordsinCollection = current.size();
            wordsLeft = fakeNumberOfWordsinCollection;
            if (current.size() == 0) {
                Alert pleaseAddWord = new Alert(Alert.AlertType.INFORMATION);
                pleaseAddWord.setContentText("bro this collection is empty; add word to this to play");
                pleaseAddWord.showAndWait();
            } else {
                idOfDisplayWord = 0;
                displayWord = current.get(idOfDisplayWord);
                currentWord.setText("");
                pronounciation.setText("");
                infoOfWord.setText("");
                progress.setText(wordsLeft + "/" + fakeNumberOfWordsinCollection);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    public void playVoice() {
        TextToSpeech.TTS(displayWord.getWord(),"en");
    }
    @FXML
    public void showAnswer() {
        if (current.size() != 0) {
            currentWord.setText(displayWord.getWord());
            pronounciation.setText(displayWord.getPronunciation());
            infoOfWord.setText(displayWord.getMeaning());
            answer.setVisible(false);
            transfer.setVisible(true);
        }
    }
    @FXML
    public void easyButton() {
        try {
            if (!WordCollectionDao.queryWordInCollection(currentCollection).isEmpty()) {
                WordsDao.deleteWord(displayWord.getWord(), currentCollection);
                wordsLeft--;
                progress.setText(wordsLeft + "/" + fakeNumberOfWordsinCollection);
                current.remove(idOfDisplayWord);
            }
            if (wordsLeft > 0)
                displayWord = current.get(idOfDisplayWord);
            else {
                progress.setText("done!");
            }
            currentWord.setText("");
            pronounciation.setText("");
            infoOfWord.setText("");
            answer.setVisible(true);
            transfer.setVisible(false);
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
        pronounciation.setText("");
        infoOfWord.setText("");
        answer.setVisible(true);
        transfer.setVisible(false);
    }
}
