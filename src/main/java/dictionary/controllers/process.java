package dictionary.controllers;

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
import java.util.List;
import java.util.ResourceBundle;

public abstract class process implements Initializable {
    @FXML
    public Label currentWord;
    @FXML
    public Button speaker;
    @FXML
    public TextArea infoOfWord;
    @FXML
    public Label progress;
    @FXML
    public Button easy;
    @FXML
    public Button hard;
    @FXML
    public Button showAnswer;
    @FXML
    public AnchorPane transfer;
    @FXML
    public Button back;
    @FXML
    public Label done;
    public String fixedNameCollection;
    public List<Word> current;
    public Word displayWord;
    public int idOfDisplayWord;
    public int fakeNumberOfWordsinCollection;
    public int wordsLeft;
    @FXML
    public abstract void initialize(URL location, ResourceBundle Resources);
    @FXML
    public void playVoice() {
        TTSService.TTS(displayWord.getWord(),"en");
    }
    @FXML
    public abstract void showAnswer();
    @FXML
    public abstract void easyButton();
    @FXML
    public abstract void hardButton();
    @FXML
    public void handleBack(){
        Stage stage = (Stage) back.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
