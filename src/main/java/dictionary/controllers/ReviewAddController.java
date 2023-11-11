package dictionary.controllers;

import dictionary.models.Dao.WordCollectionDao;
import dictionary.models.Dao.WordsDao;
import dictionary.models.Entity.Word;
import dictionary.services.WordLookUpService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class ReviewAddController implements Initializable {
    @FXML
    private ChoiceBox<String> collectionsToEdit = new ChoiceBox<>();
    @FXML
    private TextField collectionToAdd = new TextField();
    @FXML
    private TextField wordToEdit = new TextField();
    @FXML
    private TextField meaningToEdit = new TextField();
    @FXML
    private TextField typeToEdit = new TextField();
    @FXML
    private TextField pronounciationToEdit = new TextField();
    @FXML
    private Button add = new Button();
    @FXML
    private Button edit = new Button();
    @FXML
    private Button delete = new Button();
    @FXML
    private Button addCollection = new Button();
    @FXML
    private Button deleteCollection = new Button();
    @FXML
    private Button renameCollection = new Button();
    public String currentCollection;

    @FXML
    public void initialize(URL location, ResourceBundle Resources) {
        int n = WordCollectionDao.queryCollectionName().size();
        for (int i = 0; i < n; i++) {
            collectionsToEdit.getItems().add(WordCollectionDao.queryCollectionName().get(i));
        }
    }
    @FXML
    public void addColllection() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        String collectionName = collectionToAdd.getText();
        if (collectionName.isEmpty() || collectionName.isBlank()) {
            alert.setContentText("Please type the name of the collection.");
            alert.showAndWait();
        } else if (!WordCollectionDao.findCollectionName(collectionName).isEmpty()) {
            alert.setContentText("This collection has already existed.");
            alert.showAndWait();
        } else {
            WordCollectionDao.addCollection(collectionName);
            collectionsToEdit.getItems().add(collectionName);
            alert.setContentText("");
            alert.showAndWait();
        }
    }

    @FXML
    public void deleteCollection() {
        currentCollection = WordCollectionDao.findCollectionName(collectionsToEdit.getValue()).get(0);
        collectionsToEdit.getItems().remove(currentCollection);
        WordCollectionDao.deleteCollection(currentCollection);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Delete successfully!");
        alert.showAndWait();
        currentCollection = "";
    }

    @FXML
    public void addWordCollection() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        currentCollection = WordCollectionDao.findCollectionName(collectionsToEdit.getValue()).get(0);
        String word = wordToEdit.getText();
        String type = typeToEdit.getText();
        String pronounciation = pronounciationToEdit.getText();
        String meaning = meaningToEdit.getText();
        if (word.isEmpty() || word.isBlank()) {
            alert.setContentText("Word cannot be empty, please try again");
            alert.showAndWait();
        } else if (meaning.isEmpty() || meaning.isBlank()) {
            alert.setContentText("meaning cannot be empty, please try again");
            alert.showAndWait();
        } else {
            Word wordy = new Word(word, pronounciation,type,meaning);
            WordsDao.addWord(wordy,currentCollection);
            alert.setContentText("Word: " + word + " has been added successfully");
            alert.showAndWait();
        }
    }
    public void checkExistentofWordinCollection() {
        String w = wordToEdit.getText();
        if (w.isEmpty() || w.isBlank()) {
            resetText();
            return;
        }
        if(!WordCollectionDao.queryWordInCollection(currentCollection).isEmpty()) {
            List<Word> tmp = WordLookUpService.findWord(w,currentCollection);
            if(!tmp.isEmpty()) {
                Word word = tmp.get(0);
                meaningToEdit.setText(word.getMeaning());
                typeToEdit.setText(word.getType());
                pronounciationToEdit.setText(word.getPronunciation());
            }
        }
    }
    public void resetText() {
        meaningToEdit.setText("");
        typeToEdit.setText("");
        pronounciationToEdit.setText("");
    }

}