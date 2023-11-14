package dictionary.controllers;

import dictionary.services.TextToSpeech;
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

import javafx.scene.control.Button;
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
    private Button speaker, editBtn, deleteBtn, addBtn, addToCollection;

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
        List<Word> list = WordLookUpService.findWord(searchTerm, "anhviet");
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

    @FXML
    public void handleEdit() {
        // khi nguoi dung chua nhap gi
        if (searchBox.getText().isEmpty() || searchBox.getText().isBlank()) return;
        if (wordToFind.getWord().isBlank() || wordToFind.getWord().isEmpty()) return;
        // khi tu nay khong co trong tu dien
        if (relatedResults.getItems().size() <=0){
            return;
        }
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit word");
        DialogPane tmp = dialog.getDialogPane();
        tmp.getStylesheets().add(getClass().getResource("/style/dialog.css").toExternalForm());
        Label wordLabel = new Label("Word: ");
        Label display = new Label(wordToFind.getWord());

        Label meaningLabel = new Label("Definition:");
        TextArea meaningField = new TextArea(wordToFind.getMeaning());
        meaningField.setWrapText(true);

        Label typeLabel = new Label("Type:");
        TextField typeField = new TextField(wordToFind.getType());

        Label pronunciationLabel = new Label("Pronunciation:");
        TextField pronunciationField = new TextField(wordToFind.getPronunciation());

        GridPane gridPane = new GridPane();
        gridPane.add(display, 2, 1);
        gridPane.add(wordLabel, 1, 1);
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
                String meaning = meaningField.getText();
                String type = typeField.getText();
                String pronunciation = pronunciationField.getText();

                WordsDao.modifyWord(wordToFind.getWord(), "meaning", meaning, "anhviet");
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                DialogPane tmp1 = successAlert.getDialogPane();
                tmp1.getStylesheets().add(getClass().getResource("/style/dialog.css").toExternalForm());
                successAlert.setTitle("Success");
                successAlert.setContentText("Word information updated successfully!");
                successAlert.showAndWait();
                wordDefinition.setText("Type:\n" + wordToFind.getType()+ "\nMeaning:\n" + wordToFind.getMeaning());
                wordDisplay.setText(wordToFind.getWord() + "\n" + wordToFind.getPronunciation());
            }
            else {
                dialog.close();
            }
        });

    }

    @FXML
    public void handleDelete() {
        // khi nguoi dung chua nhap gi
        if (searchBox.getText().isEmpty() || searchBox.getText().isBlank()) return;
        // khi tu nay chua co trong tu dien
        if (relatedResults.getItems().size() <=0) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Add a new word");
        DialogPane tmp = alert.getDialogPane();
        tmp.getStylesheets().add(getClass().getResource("/style/dialog.css").toExternalForm());
        if(WordsDao.deleteWord(wordToFind.getWord(),"anhviet")) {
            alert.setContentText("delete successfully");
            alert.showAndWait();
        }
    }
    @FXML
    public void handleAdd() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setHeaderText("Add a new word");
        DialogPane tmp = dialog.getDialogPane();
        tmp.getStylesheets().add(getClass().getResource("/style/dialog.css").toExternalForm());
        //tmp.getStyleClass().add("dialog");
        dialog.setHeaderText(null);

        Label newWordLabel = new Label("New word: ");
        TextField newWordField = new TextField();
        // newWordField.setWrapText(true);

        Label meaningLabel = new Label("Meaning:");
        TextArea meaningField = new TextArea();
        meaningField.setWrapText(true);
        // meaningField.setWrapText(true);

        Label typeLabel = new Label("Type:");
        TextField typeField = new TextField();
        //typeField.setWrapText(true);

        Label pronunciationLabel = new Label("Pronunciation:");
        TextField pronunciationField = new TextField();
        //pronunciationField.setWrapText(true);

        GridPane gridPane = new GridPane();
        gridPane.add(newWordLabel, 1, 1);
        gridPane.add(newWordField, 2, 1);
        gridPane.add(meaningLabel, 1, 4);
        gridPane.add(meaningField, 2, 4);
        gridPane.add(typeLabel, 1, 2);
        gridPane.add(typeField, 2, 2);
        gridPane.add(pronunciationLabel, 1, 3);
        gridPane.add(pronunciationField, 2, 3);

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
                String word = newWordField.getText();
                String meaning = meaningField.getText();
                String type = typeField.getText();
                String pronunciation = pronunciationField.getText();
                Word ye = new Word(word, pronunciation, type, meaning);
                int id = AllWord.leftMostIndex(word);
                if (id != -1) {
                    System.out.println("This word already in dictionary");
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    DialogPane tmp1 = successAlert.getDialogPane();
                    tmp1.getStylesheets().add(getClass().getResource("/style/dialog.css").toExternalForm());
                    successAlert.setTitle("Success");
                    successAlert.setContentText("This word already in dictionary");
                    successAlert.showAndWait();
                }
                else if (WordsDao.addWord(ye,"anhviet")) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    DialogPane tmp1 = successAlert.getDialogPane();
                    tmp1.getStylesheets().add(getClass().getResource("/style/dialog.css").toExternalForm());
                    successAlert.setTitle("Success");
                    successAlert.setContentText("Word information updated successfully!");
                    successAlert.showAndWait();
                }
            }
            else {
                dialog.close();
            }
        });
    }
    @FXML
    public void handleAddCollection() {
        // khi nguoi dung chua nhap gi
        if (searchBox.getText().isEmpty() || searchBox.getText().isBlank()) return;
        if (wordToFind.getWord().isBlank() || wordToFind.getWord().isEmpty()) return;
        // khi tu nay khong co trong tu dien
        if (relatedResults.getItems().size() <=0){
            return;
        }
        Dialog<String> dialog = new Dialog<>();
        dialog.setHeaderText(null);

        Label INSTRUCTION=new Label("Please choose the collection to add this word.");
        ChoiceBox<String> allCollections=new ChoiceBox<>();
        List<String> collectionNames = WordCollectionDao.queryCollectionName();
        for (String name : collectionNames) {
            allCollections.getItems().add(name);
        }
        GridPane gridPane = new GridPane();
        gridPane.add(INSTRUCTION, 1, 1);
        gridPane.add(allCollections, 1, 2);

        dialog.setHeaderText("Add this word into a collection");
        DialogPane tmp = dialog.getDialogPane();
        tmp.getStylesheets().add(getClass().getResource("/style/dialog.css").toExternalForm());

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
                String collectionToAdd = allCollections.getValue();
                WordCollectionDao.addWordForCollection(wordToFind,collectionToAdd);
            }
            else {
                dialog.close();
            }
        });
    }

    @FXML
    public void handleSpeaker() {
        TextToSpeech.TTS(wordToFind.getWord(),"en");
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
