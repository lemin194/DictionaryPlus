package dictionary.controllers;

import dictionary.models.Dao.WordCollectionDao;
import dictionary.models.Entity.Word;
import dictionary.services.QuizService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;


public class Game1Controller extends game {
    /* in game3 it is btnNext.*/
    @FXML
    public Button btnNext;

    /* close the window.*/
    @FXML
    public Button btnClose;

    /* in game 3; it's btnPlay*/
    @FXML
    public Button btnSpeaker;
    @FXML
    private Label question = new Label();
    ToggleGroup group = new ToggleGroup();
    @FXML
    private ToggleButton ansA = new ToggleButton();
    @FXML
    private ToggleButton ansB = new ToggleButton();
    @FXML
    private ToggleButton ansC = new ToggleButton();
    @FXML
    private ToggleButton ansD = new ToggleButton();
    @FXML
    private Label rightOrFalse = new Label();
    private final QuizService quiz = new QuizService();
    private String answer;
    private List<Word> currentCollectionChoice = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle Resources) {
        int n = WordCollectionDao.queryCollectionName().size();
        for (int i = 0; i < n; i++) {
            collectionsToPlay.getItems().add(WordCollectionDao.queryCollectionName().get(i));
        }
        for (ToggleButton button : List.of(ansA, ansB, ansC, ansD)) {
            button.setToggleGroup(group);
        }
        for (ToggleButton button : List.of(ansA, ansB, ansC, ansD)) {
            button.setOnAction(this::whenSelected);
        }
        rightOrFalse.getStylesheets().add(getClass().getResource("/style/review.css").toExternalForm());
        rightOrFalse.setVisible(false);
    }
    @FXML
    public void startGame() {
        currentCollectionName = collectionsToPlay.getValue();

        currentWordList = WordCollectionDao.queryWordInCollection(currentCollectionName);
        currentWord = currentWordList.get(idOfDisplayWord);
        //currentCollectionList = quiz.generateOneQuiz(currentCollectionName, realAnswer);
        currentCollectionChoice = quiz.generateOneQuiz(currentCollectionName, currentWord);
        currentCollectionChoice.add(currentWord);
        Collections.shuffle(currentCollectionChoice);
        setQuestion();
        clearSelected();
        progress.setText("0/"+ currentWordList.size()+" words");
    }

    public void setQuestion() {
        currentWord = currentWordList.get(idOfDisplayWord);
        question.setText("What is the meaning of " + currentWord.getWord() + " ?");
        question.setWrapText(true);
        ansA.setDisable(false);
        ansB.setDisable(false);
        ansC.setDisable(false);
        ansD.setDisable(false);
        int tmp = 0;
        for (ToggleButton button : List.of(ansA, ansB, ansC, ansD)) {
            button.setText(currentCollectionChoice.get(tmp).getMeaning());
            button.setWrapText(true);
            tmp++;
        }
        clearSelected();
    }

    public void clearSelected() {
        for (ToggleButton button : List.of(ansA, ansB, ansC, ansD)) {
            button.setSelected(false);
        }
    }
    @FXML
    public void nextQuestion() {
        rightOrFalse.setVisible(false);
        idOfDisplayWord++;
        if (idOfDisplayWord >= currentWordList.size()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane tmp1 = successAlert.getDialogPane();
            tmp1.getStylesheets().add(getClass().getResource("/style/dialog.css").toExternalForm());
            alert.setContentText("You finished this collection, wanna restart?");
            alert.showAndWait();
            idOfDisplayWord=0;
        }
        currentWord = currentWordList.get(idOfDisplayWord);
        currentCollectionChoice = quiz.generateOneQuiz(currentCollectionName, currentWord);
        currentCollectionChoice.add(currentWord);
        rightOrFalse.setText("");
        progress.setText((idOfDisplayWord+1)+"/"+ currentWordList.size()+" words");
        setQuestion();
    }

    public void whenSelected(ActionEvent e) {
        ToggleButton selectedToggleButton = (ToggleButton) e.getSource();
        System.out.println(selectedToggleButton.getText() + " selected");

        group.getToggles().forEach(toggle -> {
            ToggleButton toggleButton = (ToggleButton) toggle;
            if (!toggleButton.equals(selectedToggleButton)) {
                toggleButton.setDisable(true);
            }
        });
        if (selectedToggleButton.getText().equals(currentWord.getMeaning())) {
            finalScore++;
            Score.setText("Score: " + finalScore + "/" + currentWordList.size());
            rightOrFalse.getStyleClass().clear();
            rightOrFalse.getStyleClass().add("whenTrue");
            rightOrFalse.setText("Correct!");
            rightOrFalse.setVisible(true);
        } else {
            Score.setText("Score: "+ finalScore + "/" + currentWordList.size());
            rightOrFalse.getStyleClass().clear();
            rightOrFalse.getStyleClass().add("whenFalse");
            rightOrFalse.setText("Wrong");
            rightOrFalse.setVisible(true);
        }
    }
    @FXML
    public void handleClose(){
        Stage stage = (Stage) btnClose.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
