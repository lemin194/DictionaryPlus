package dictionary.controllers;

import dictionary.models.Dao.WordCollectionDao;
import dictionary.models.Entity.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class ReviewAddController implements Initializable {
    @FXML
    private AnchorPane collectionSetting = new AnchorPane();
    @FXML
    private ChoiceBox<String> collectionsToEdit = new ChoiceBox<>();
    @FXML
    private Button addCollection = new Button();
    @FXML
    private Button deleteCollection = new Button();
    @FXML
    private Button editCollection = new Button();
    @FXML
    private TextField collectionToAdd = new TextField();
    @FXML
    private AnchorPane wordsSetting = new AnchorPane();
    @FXML
    private Label displayCurrentCollectionSetting = new Label();
    @FXML
    private Button doneEditCollectionSetting = new Button();
    @FXML
    private TextField wordToEdit = new TextField();
    @FXML
    private TextField typeToEdit = new TextField();
    @FXML
    private TextField pronunciationToEdit = new TextField();
    @FXML
    private TextArea meaningToEdit = new TextArea();
    @FXML
    private Button add = new Button();
    @FXML
    private Button edit = new Button();
    @FXML
    private Button delete = new Button();
    public String currentCollection = new String("");
    @FXML
    public void initialize(URL location, ResourceBundle Resources) {
        collectionSetting.setVisible(true);
        wordsSetting.setVisible(false);
        int n = WordCollectionDao.queryCollectionName().size();
        for (int i = 0; i < n; i++) {
            collectionsToEdit.getItems().add(WordCollectionDao.queryCollectionName().get(i));
        }
        collectionsToEdit.setOnAction(e -> setCurrentCollection());
    }
    @FXML
    public void setCurrentCollection(){
        currentCollection = collectionsToEdit.getValue();
    }
    @FXML
    public void changeToCollectionWordEdit(){
        if (collectionsToEdit.getValue() == null) return;
        else {
            currentCollection = collectionsToEdit.getValue();
            displayCurrentCollectionSetting.setText(currentCollection);
            collectionSetting.setVisible(false);
            wordsSetting.setVisible(true);
        }
    }
    @FXML
    public void backToCollectionSetting() {
        wordToEdit.setText("");
        typeToEdit.setText("");
        pronunciationToEdit.setText("");
        meaningToEdit.setText("");
        collectionSetting.setVisible(true);
        wordsSetting.setVisible(false);
    }
    @FXML
    public void addColllection() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        DialogPane tmp1 = alert.getDialogPane();
        tmp1.getStylesheets().add(getClass().getResource("/style/dialog.css").toExternalForm());
        alert.setTitle("Success");
        alert.setHeaderText(null);
        String collectionName = collectionToAdd.getText();
        if (collectionName.isEmpty() || collectionName.isBlank()) {
            return;
        }
        if (!WordCollectionDao.findCollectionName(collectionName).isEmpty()) {
            boolean check = false;
            List<String> res = WordCollectionDao.findCollectionName(collectionName);
            for (String tmp : res) {
                if (tmp.equals(collectionName)) {
                    check = true;
                    break;
                }
            }
            if (check) {
                alert.setTitle("Failed");
                alert.setContentText("This collection has already existed.");
                alert.showAndWait();
                return;
            } else {
                WordCollectionDao.addCollection(collectionName);
                collectionsToEdit.getItems().add(collectionName);
                alert.setContentText("New collection: " + collectionName + " added successfully");
                alert.showAndWait();
            }
        } else {
            WordCollectionDao.addCollection(collectionName);
            collectionsToEdit.getItems().add(collectionName);
            alert.setContentText("New collection: " + collectionName + " added successfully");
            alert.showAndWait();
        }
    }

    @FXML
    public void deleteCollection() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        DialogPane tmp1 = alert.getDialogPane();
        tmp1.getStylesheets().add(getClass().getResource("/style/dialog.css").toExternalForm());
        alert.setTitle("Success");
        if (WordCollectionDao.findCollectionName(collectionsToEdit.getValue()).isEmpty()) {
            alert.setTitle("Failed");
            alert.setContentText("Something wrong happened. Please contact us to fix this bug!");
            alert.showAndWait();
            return;
        }
        currentCollection = WordCollectionDao.findCollectionName(collectionsToEdit.getValue()).get(0);
        collectionsToEdit.getItems().remove(currentCollection);
        WordCollectionDao.deleteCollection(currentCollection);
        for (String x : collectionsToEdit.getItems()) {
            if(x.equals(currentCollection)) {
                collectionsToEdit.getItems().remove(x);
                break;
            }
        }

        alert.setContentText("Delete successfully!");
        alert.setHeaderText(null);
        alert.showAndWait();

        currentCollection = "";
    }

    @FXML
    public void addWordCollection() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        DialogPane tmp1 = alert.getDialogPane();
        tmp1.getStylesheets().add(getClass().getResource("/style/dialog.css").toExternalForm());
        alert.setTitle("Success");
        currentCollection = WordCollectionDao.findCollectionName(collectionsToEdit.getValue()).get(0);
        System.out.println(currentCollection);
        String word = wordToEdit.getText();
        String type = typeToEdit.getText();
        String pronunciation = pronunciationToEdit.getText();
        String meaning = meaningToEdit.getText();
        if (word.isEmpty() || word.isBlank()) {
            return;
        }
        if (!WordCollectionDao.findWordInCollection(word, currentCollection).isEmpty()) {
            List<Word> res = WordCollectionDao.findWordInCollection(word, currentCollection);
            boolean check = false;
            for (Word tmp : res) {
                if (tmp.getWord().equals(word)) {
                    check = true;
                    break;
                }
            }
            if (check) {
                alert.setTitle("Failed");
                alert.setContentText("This word already exists in the collection: " + currentCollection);
                alert.showAndWait();
                return;
            } else {
                Word wordy = new Word(word, pronunciation, type, meaning);
                if (WordCollectionDao.addWordForCollection(wordy, currentCollection)) {
                    alert.setContentText("Word: " + word + " has been added successfully!");
                    alert.showAndWait();
                }
            }
        } else {
            Word wordy = new Word(word, pronunciation, type, meaning);
            if (WordCollectionDao.addWordForCollection(wordy, currentCollection)) {
                alert.setContentText("Word: " + word + " has been added successfully!");
                alert.showAndWait();
            }
        }
    }
    @FXML
    public void editWordCollection() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        DialogPane tmp1 = alert.getDialogPane();
        tmp1.getStylesheets().add(getClass().getResource("/style/dialog.css").toExternalForm());
        alert.setTitle("Success");
        currentCollection = WordCollectionDao.findCollectionName(collectionsToEdit.getValue()).get(0);
        System.out.println(currentCollection);
        String word = wordToEdit.getText();
        String type = typeToEdit.getText();
        String pronunciation = pronunciationToEdit.getText();
        String meaning = meaningToEdit.getText();
        if (word.isEmpty() || word.isBlank()) {
            return;
        } else if (WordCollectionDao.findWordInCollection(word, currentCollection).isEmpty()) {
            alert.setTitle("Failed");
            alert.setContentText("This word does not exist in the collection: " + currentCollection);
            alert.showAndWait();
            return;
        }
        else {
            WordCollectionDao.modifyWordFromCollection(word, "type", type, currentCollection);
            WordCollectionDao.modifyWordFromCollection(word, "pronunciation", pronunciation, currentCollection);
            WordCollectionDao.modifyWordFromCollection(word, "meaning", meaning, currentCollection);
            alert.setContentText("Word: " + word + " in the collection " + currentCollection
                    + " has been edited successfully!");
            alert.showAndWait();
        }
    }
    @FXML
    public void deleteWordCollection() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        DialogPane tmp1 = alert.getDialogPane();
        tmp1.getStylesheets().add(getClass().getResource("/style/dialog.css").toExternalForm());
        alert.setTitle("Success");
        currentCollection = WordCollectionDao.findCollectionName(collectionsToEdit.getValue()).get(0);
        System.out.println(currentCollection);
        String word = wordToEdit.getText();
        String type = typeToEdit.getText();
        String pronunciation = pronunciationToEdit.getText();
        String meaning = meaningToEdit.getText();
        if (word.isEmpty() || word.isBlank()) {
            return;
        } else if (WordCollectionDao.findWordInCollection(word, currentCollection).isEmpty()) {
            alert.setTitle("Failed");
            alert.setContentText("This word does not exist in the collection: " + currentCollection);
            alert.showAndWait();
            return;
        }
        else {
            WordCollectionDao.deleteWordFromCollection(word,currentCollection);
            wordToEdit.setText("");
            typeToEdit.setText("");
            pronunciationToEdit.setText("");
            meaningToEdit.setText("");
            alert.setContentText("Word: " + word + " in the collection " + currentCollection
                    + " has been deleted successfully!");
            alert.showAndWait();
        }
    }
    @FXML
    public void checkExistentofWordinCollection() {
        String w = wordToEdit.getText();
        if (w.isEmpty() || w.isBlank()) {
            resetText();
            return;
        }
        if(!WordCollectionDao.queryWordInCollection(currentCollection).isEmpty()) {
            List<Word> tmp = WordCollectionDao.findWordInCollection(w,currentCollection);
            if(!tmp.isEmpty()) {
                Word word = tmp.get(0);
                for (Word x : tmp) {
                    // this maybe cause to be slow
                    if (x.getWord().equals(w)) {
                        word = x;
                        break;
                    }
                }
                if (word.getWord().equals(w)) {
                    //System.out.println("it works! " + word.getWord() + "/" + word.getMeaning());
                    meaningToEdit.setText(word.getMeaning());
                    typeToEdit.setText(word.getType());
                    pronunciationToEdit.setText(word.getPronunciation());
                }
            } else {
                resetText();
            }
        }
    }
    public void resetText() {
        meaningToEdit.setText("");
        typeToEdit.setText("");
        pronunciationToEdit.setText("");
    }

}