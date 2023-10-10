package dictionary.controllers;

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
    private Label wordDisplay = new Label();
    @FXML
    private TextArea wordDefinition = new TextArea();
    @FXML
    public Word wordToFind;
    @FXML
    public void initialize(URL location, ResourceBundle Resources) {
        relatedResults.setOnMouseClicked(event -> {
            String selectedWord = relatedResults.getSelectionModel().getSelectedItem();
            if (selectedWord != null) {
                    Word english = WordsDao.queryWord(selectedWord, "anhviet").get(0);
                    if (english != null) {
                        wordToFind = english;
                        wordDefinition.setText(english.getMeaning());
                        wordDisplay.setText(wordToFind.getWord());
                    } else {
                        wordDefinition.setText("Definition not found for: " + selectedWord);
                    }
            }
        });
    }

    @FXML
    public void handleSearch(KeyEvent keyEvent) {
        String searchTerm = searchBox.getText();
        if (searchTerm.isEmpty() || searchTerm.isBlank()) {
            relatedResults.getItems().clear();
            notAvailable.setText("");
            wordDefinition.setText("");
            wordDisplay.setText("");
            return;
        }

        relatedResults.getItems().clear();
        List<Word> list = WordsDao.queryWord(searchTerm, "anhviet");
        if (list.isEmpty()) {
            clearSearchResultsView();
            notAvailable.setText("Sorry, We don't have this word !");
            return;
        }
        wordToFind = list.get(0);
        wordDisplay.setText(wordToFind.getWord());
        wordDefinition.setText(wordToFind.getMeaning());

        Collections.sort(relatedResults.getItems());


        /** debug purposes.*/
        for (Word english : list) {
            System.out.println(english.getWord());
            relatedResults.getItems().add(english.getWord());
        }
    }

    @FXML
    public void clearSearchResultsView() {
        relatedResults.getItems().clear();
        wordDefinition.setText("");
        notAvailable.setText("");
    }

    public void initializeWithStage(Stage stage) {
        this.initializeWithStage(stage);
    }
}
