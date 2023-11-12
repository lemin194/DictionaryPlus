package dictionary.controllers;

import dictionary.models.Dao.WordCollectionDao;
import dictionary.models.Dao.WordsDao;
import dictionary.models.Entity.Word;
import dictionary.services.TextToSpeech;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private TextField numCardPerSessions = new TextField();

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
    }
    @FXML
    public void startGame() {
        if(collectionsToStudy.getValue() == null) return;
        currentCollection = collectionsToStudy.getValue();
        currentStudyMode = studyMode.getValue();
        //currentSortBy = sortBy.getValue();
        //numCards = Integer.parseInt(numCardPerSessions.getText());
        System.out.println(currentStudyMode);

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
        }
    }
}
