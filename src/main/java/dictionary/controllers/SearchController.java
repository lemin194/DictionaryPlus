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
    private Button speaker, editBtn, deleteBtn;

    @FXML
    public void initialize(URL location, ResourceBundle Resources) {
        relatedResults.setOnMouseClicked(event -> {
            String selectedWord = relatedResults.getSelectionModel().getSelectedItem();
            if (selectedWord != null) {
                Word english = WordLookUpService.findWord(selectedWord, "anhviet").get(0);
                if (english != null) {
                    wordToFind = english;
                    wordDefinition.setText("Type:\n" + english.getType() + "\n" + "Meaning:\n" + english.getMeaning() );
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
            relatedResults.getItems().add(english.getWord());
        }
    }

    /*
    public void updateWord() {
        if (relatedResults.getItems().size() <=0){
            return;
        }
        Dialog<String> dialog = new Dialog<>();
        dialog.setHeaderText(null);

        Label nameLabel = new Label("Word:");
        TextField nameField = new TextField(wordToFind.getWord());

        Label meaningLabel = new Label("Definition:");
        TextArea meaningField = new TextArea(wordToFind.getMeaning());
        meaningField.setWrapText(true);

        Label typeLabel = new Label("Part of speech:");
        TextField typeField = new TextField(wordToFind.getType());

        Label pronunciationLabel = new Label("Pronunciation:");
        TextField pronunciationField = new TextField(wordToFind.getPronunciation());


        GridPane gridPane = new GridPane();
        gridPane.add(nameLabel, 1, 1);
        gridPane.add(nameField, 2, 1);
        gridPane.add(meaningLabel, 1, 2);
        gridPane.add(meaningField, 2, 2);
        gridPane.add(typeLabel, 1, 3);
        gridPane.add(typeField, 2, 3);
        gridPane.add(pronunciationLabel, 1, 4);
        gridPane.add(pronunciationField, 2, 4);

        dialog.getDialogPane().setContent(gridPane);

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                return "OK";
            } else if (dialogButton == cancelButton) {
                return "Cancel";
            } else {
                return null;
            }
        });

        dialog.showAndWait().ifPresent(response -> {
            if (response.equals("OK")) {
                String word = nameField.getText();
                String meaning = meaningField.getText();
                String type = typeField.getText();
                String pronunciation = pronunciationField.getText();

                WordsDao.modifyWord(word, "meaning", meaning, "anhviet");
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setContentText("Word information updated successfully!");
                successAlert.showAndWait();
                wordDefinition.setText("Type:\n" + wordToFind.getType()+ "\nMeaning:\n" + wordToFind.getMeaning());

            }
            else {
                dialog.close();
            }
        });

    }*/
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
