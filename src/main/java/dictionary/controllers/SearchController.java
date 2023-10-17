package dictionary.controllers;

import dictionary.services.WordLookUpService;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.collections.FXCollections;


import dictionary.models.Entity.*;
import dictionary.models.Dao.*;
import dictionary.services.WordLookUpService.*;

import java.sql.SQLException;

import java.util.*;

import static dictionary.MainApplication.mainStage;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    @FXML
    private TextField searchBox = new TextField();
    @FXML
    private ListView<String> relatedResults = new ListView<>();

    @FXML
    private Label notAvailable = new Label();

    @FXML
    private TextArea wordDefinition = new TextArea();

    @FXML
    public Word wordToFind;

    @FXML
    private Label wordDisplay = new Label();

    @FXML
    public void initialize(URL location, ResourceBundle Resources) {
        relatedResults.setOnMouseClicked(event -> {
            String selectedWord = relatedResults.getSelectionModel().getSelectedItem();
            if (selectedWord != null) {
                Word english = WordLookUpService.findWord(selectedWord, "anhviet").get(0);
                if (english != null) {
                    wordToFind = english;
                    wordDefinition.setText("Type:\n" + english.getType() + "\n" + "Meaning:\n" +english.getMeaning() );
                    wordDisplay.setText(english.getWord() + "\n" + english.getPronunciation());
                
                } else {
                    wordDefinition.setText("Definition not found for: " + selectedWord);
                }
                WordLookUpService.addWord(english);
            }
        });
    }

    @FXML
    public void handleSearch(KeyEvent keyEvent) {
        String searchTerm = searchBox.getText();
        if (searchTerm.isEmpty() || searchTerm.isBlank()) {
            clearSearchResultsView();

            List<Word> pastWords = WordLookUpService.retrieveLastSearch();
            for (Word word : pastWords) relatedResults.getItems().add(word.getWord());
            Word tmp = pastWords.get(0);
            wordDefinition.setText("Type:\n" + tmp.getType()+ "\nMeaning:\n" + tmp.getMeaning());
            wordDisplay.setText(tmp.getWord() + "\n" + tmp.getPronunciation());
            return;
        }

        relatedResults.getItems().clear();
        List<Word> list = WordsDao.queryWord(searchTerm, "anhviet");
        if (list.isEmpty()) {
            clearSearchResultsView();
            notAvailable.setText("We don't have this word!");
            return;
        }
        wordToFind = list.get(0);
        wordDefinition.setText("Type:\n" + wordToFind.getType()+ "\nMeaning:\n" + wordToFind.getMeaning());
        wordDisplay.setText(wordToFind.getWord() + "\n" + wordToFind.getPronunciation());
        for (Word english : list) {
            System.out.println(english.getWord());
            relatedResults.getItems().add(english.getWord());
        }
    }

    public void clearSearchResultsView() {
        relatedResults.getItems().clear();
        wordDefinition.setText("");
        wordDisplay.setText("");
        notAvailable.setText("");
    }

    public void initializeWithStage(Stage stage) {
        this.initializeWithStage(stage);
    }






}
