package dictionary.controllers;

import dictionary.models.Dao.WordsDao;
import dictionary.models.Entity.Word;
import dictionary.services.TextToSpeech;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;


import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

import static dictionary.models.Entity.WordCollectionManagement.collectionNameList;

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

    private String currentCollection;
    private Word displayWord;
    private int idOfDisplayWord;
    private int fakeNumberOfWordsinCollection;

    private int realNumberOfWordsinCollection;

    private int wordsLeft;

    @FXML
    public void initialize(URL location, ResourceBundle Resources) {
        int n = collectionNameList.size();
        for (int i = 0; i < n; i++) {
            collectionsToStudy.getItems().add(collectionNameList.get(i));
        }
    }
    @FXML
    public void startGame() {
        currentCollection = collectionsToStudy.getValue();
        fakeNumberOfWordsinCollection = WordsDao.queryWord("",currentCollection).size();
        realNumberOfWordsinCollection = fakeNumberOfWordsinCollection;
        wordsLeft = fakeNumberOfWordsinCollection;
        idOfDisplayWord = 0;
        displayWord = WordsDao.queryWord("",currentCollection).get(idOfDisplayWord);
        currentWord.setText("");
        pronounciation.setText("");
        infoOfWord.setText("");
        progress.setText("0/" + fakeNumberOfWordsinCollection);
    }
    @FXML
    public void playVoice() {
        TextToSpeech.TTS(displayWord.getWord(),"en");
    }
    @FXML
    public void showAnswer() {
        currentWord.setText(displayWord.getWord());
        pronounciation.setText(displayWord.getPronunciation());
        infoOfWord.setText(displayWord.getMeaning());
    }
    @FXML
    public void easyButton() {
        System.out.println("here it is" + displayWord.getWord());
        System.out.println("here it iso" + currentCollection);


        if(WordsDao.deleteWord(displayWord.getWord(), currentCollection))
            System.out.println("ye this work");
        wordsLeft--;
        realNumberOfWordsinCollection--;
        progress.setText(wordsLeft + "/" + fakeNumberOfWordsinCollection);
        displayWord = WordsDao.queryWord("",currentCollection).get(idOfDisplayWord);
        currentWord.setText(displayWord.getWord());
        pronounciation.setText(displayWord.getPronunciation());
        infoOfWord.setText(displayWord.getMeaning());
    }
    @FXML
    public void normalButton() {
        WordsDao.deleteWord(displayWord.getWord(), currentCollection);
        wordsLeft--;
        realNumberOfWordsinCollection--;
        progress.setText(wordsLeft + "/" + fakeNumberOfWordsinCollection);
        displayWord = WordsDao.queryWord("",currentCollection).get(idOfDisplayWord);
        currentWord.setText(displayWord.getWord());
        pronounciation.setText(displayWord.getPronunciation());
        infoOfWord.setText(displayWord.getMeaning());
    }
    @FXML
    public void hardButton() {
        currentWord.setText("");
        pronounciation.setText("");
        infoOfWord.setText("");
        progress.setText(wordsLeft + "/" + fakeNumberOfWordsinCollection);
        int len = WordsDao.queryWord("",currentCollection).size();
        idOfDisplayWord++;
        if(idOfDisplayWord >= len) idOfDisplayWord = 0;
        displayWord = WordsDao.queryWord("",currentCollection).get(idOfDisplayWord);
        /*currentWord.setText(displayWord.getWord());
        pronounciation.setText(displayWord.getPronunciation());
        infoOfWord.setText(displayWord.getMeaning());*/
    }
}
