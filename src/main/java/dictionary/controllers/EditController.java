package dictionary.controllers;

import dictionary.models.Dao.WordsDao;
import dictionary.models.Entity.Word;
import dictionary.services.WordLookUpService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import dictionary.services.WordLookUpService.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;



public class EditController {
    @FXML
    private Button addBtn;
    @FXML
    private Label had = new Label();
    @FXML
    private TextField getWord = new TextField(), getType, getPronounciation, getMeaning;
    public void resetText() {
        getType.setText("");
        getMeaning.setText("");
        getPronounciation.setText("");
        had.setText("");
    }


    public void checkExistent() {
        String wordinBox = getWord.getText();
        if (wordinBox.isEmpty() || wordinBox.isBlank()) {
            resetText();
            return;

        }
        ArrayList<Word> tmp = WordsDao.queryWord(wordinBox,"anhviet");

        if (tmp.isEmpty()) {
            resetText();
            return;
        }
        Word word = tmp.get(0);
        if (word != null) {
            ////
            getMeaning.setText(word.getMeaning());
            getType.setText(word.getType());
            getPronounciation.setText(word.getPronunciation());
            had.setText("bro we had this, move to search section to edit");
        }
    }

    @FXML
    protected void handleAddBtn() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String word = getWord.getText();
        String type = getType.getText();
        String pronounciation = getPronounciation.getText();
        String meaning = getMeaning.getText();
        if (word.isEmpty() || word.isBlank()) {
            alert.setContentText("Word cannot be empty, please try again");
            alert.showAndWait();
        } else if (meaning.isEmpty() || meaning.isBlank()) {
            alert.setContentText("meaning cannot be empty, please try again");
            alert.showAndWait();
        } else {
            Word ye = new Word(word, pronounciation, type, meaning);

            if (WordsDao.addWord(ye,"anhviet")) {
                alert.setContentText("Word: " + word + " has been added successfully");
                alert.showAndWait();
                resetText();
            }
        }
    }
}
