package dictionary.controllers;

import dictionary.models.Dao.WordCollectionDao;
import dictionary.models.Entity.Word;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;
import java.net.URL;
import java.util.List;

import java.util.ResourceBundle;


public class ReviewPlayController implements Initializable {
    @FXML
    private ChoiceBox<String> collectionsToStudy = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> studyMode = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> sortBy = new ChoiceBox<>();
    @FXML
    private TextField numCardPerSession = new TextField();
    @FXML
    private Label numWordsInChosenCollection = new Label();
    @FXML
    private Label warningEmptyCollection = new Label();
    @FXML
    private Label warningInvalidNumCards = new Label();
    @FXML
    private Button startGame = new Button();
    public static String currentCollection = new String("");
    public static String currentStudyMode = new String("");
    public static String currentSortBy = new String("");


    public static int numCards;


    @FXML
    public void initialize(URL location, ResourceBundle Resources) {
        int n = WordCollectionDao.queryCollectionName().size();
        for (int i = 0; i < n; i++) {
            collectionsToStudy.getItems().add(WordCollectionDao.queryCollectionName().get(i));
        }
        studyMode.getItems().add("Classic");
        studyMode.getItems().add("Reverse");
        studyMode.getItems().add("Typing");
        studyMode.getItems().add("Listening");
        sortBy.getItems().add("Alphabetically");
        sortBy.getItems().add("Randomly");
        sortBy.getItems().add("History");
        studyMode.setValue("Classic");
        sortBy.setValue("History");
        warningEmptyCollection.setVisible(false);
        warningInvalidNumCards.setVisible(false);
    }
    @FXML
    public void setInfoChosenCollection(){
        currentCollection = collectionsToStudy.getValue();
        numCards = WordCollectionDao.queryWordInCollection(currentCollection).size();
        numWordsInChosenCollection.setText(numCards + " words.");
        numCardPerSession.setText(String.valueOf(numCards));
        if(numCards == 0) warningEmptyCollection.setVisible(true);
        else warningEmptyCollection.setVisible(false);
    }
    @FXML
    public void startGame() {
        if(collectionsToStudy.getValue() == null) return;
        List<Word> checkB4Play = WordCollectionDao.queryWordInCollection(currentCollection);
        if (checkB4Play.isEmpty()) {
            warningEmptyCollection.setVisible(true);
            return;
        } else {
            warningEmptyCollection.setVisible(false);
        }
        numCards = Integer.parseInt(numCardPerSession.getText());
        if (numCards <= 0 || numCards > checkB4Play.size()) {
            warningInvalidNumCards.setVisible(true);
            return;
        } else {
            warningInvalidNumCards.setVisible(false);
        }
        currentCollection = collectionsToStudy.getValue();
        currentStudyMode = studyMode.getValue();
        currentSortBy = sortBy.getValue();
        if (currentStudyMode.equals("Classic")) {
            try {
                Stage stage = new Stage();
                stage.setTitle("Learning mode: Classic");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ReviewProcessClassic.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Scene scene = new Scene(root1, Color.web("FFFFFF"));
                scene.setFill(Color.TRANSPARENT);
                scene.getStylesheets().add(getClass().getResource("/style/review.css").toExternalForm());
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.out.println("Can't create new scene for Learning mode: Classic");
            }
        } else if (currentStudyMode.equals("Typing")) {
            try {
                Stage stage = new Stage();
                stage.setTitle("Learning mode: Typing");
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ReviewProcessTyping.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Scene scene = new Scene(root1, Color.web("FFFFFF"));
                scene.setFill(Color.TRANSPARENT);
                scene.getStylesheets().add(getClass().getResource("/style/review.css").toExternalForm());
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.out.println("Can't create new scene for Learning mode: Typing");
            }
        }
    }
}
