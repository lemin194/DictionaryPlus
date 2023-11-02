package dictionary.controllers;

import dictionary.models.Dao.WordCollectionDao;
import dictionary.models.Dao.WordsDao;
import dictionary.models.Entity.Word;
import dictionary.services.TextToSpeech;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import java.util.ResourceBundle;


public class ReviewPlayController implements Initializable {
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
    private ChoiceBox<String> studyMode = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> sortBy = new ChoiceBox<>();
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
    @FXML
    private AnchorPane tmpStart = new AnchorPane();
    @FXML
    private AnchorPane tmpEnd = new AnchorPane();
    @FXML
    private AnchorPane tmpContent = new AnchorPane();

    private List<Word> current = new ArrayList<>();

    @FXML
    public void initialize(URL location, ResourceBundle Resources) {
        int n = WordCollectionDao.queryCollectionName().size();
        for (int i = 0; i < n; i++) {
            collectionsToStudy.getItems().add(WordCollectionDao.queryCollectionName().get(i));
        }
        tmpStart.setVisible(true);
        tmpContent.setVisible(false);
        tmpEnd.setVisible(false);
        transfer.setVisible(false);
        answer.setVisible(false);
    }
    @FXML
    public void startGame() {
        try {
            tmpStart.setVisible(false);
            tmpContent.setVisible(true);
            tmpEnd.setVisible(false);
            transfer.setVisible(false);
            answer.setVisible(true);
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
            tmpStart.setVisible(false);
            tmpContent.setVisible(true);
            tmpEnd.setVisible(false);
            transfer.setVisible(true);
            answer.setVisible(false);
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
            if (wordsLeft > 0) {
                displayWord = current.get(idOfDisplayWord);
                tmpStart.setVisible(false);
                tmpContent.setVisible(true);
                tmpEnd.setVisible(false);
                transfer.setVisible(false);
                answer.setVisible(true); }
            else {
                tmpStart.setVisible(false);
                tmpContent.setVisible(false);
                tmpEnd.setVisible(true);
                transfer.setVisible(false);
                answer.setVisible(false);
                progress.setText("done!");
            }
            currentWord.setText("");
            pronounciation.setText("");
            infoOfWord.setText("");

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
        tmpStart.setVisible(false);
        tmpContent.setVisible(true);
        tmpEnd.setVisible(false);
        transfer.setVisible(false);
        answer.setVisible(true);
    }
}
