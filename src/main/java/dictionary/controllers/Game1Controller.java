package dictionary.controllers;

import dictionary.models.Dao.WordCollectionDao;
import dictionary.models.Entity.Word;
import dictionary.services.QuizService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;


public class Game1Controller implements Initializable {
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
    private Label question = new Label();
    @FXML
    private Button confirmThisCollection = new Button();
    @FXML
    private Button next = new Button();
    @FXML
    ChoiceBox<String> collectionsToPlay = new ChoiceBox<>();
    @FXML
    private Label rightOrFalse = new Label();
    @FXML
    private Label progress=new Label("");

    private List<Word> currentCollectionList = new ArrayList<>();
    private Word realAnswer;
    private final QuizService quiz = new QuizService();
    private String currentCollectionName = "";
    private String answer;
    private int idOfDisplayWord;
    private List<Word> currentCollectionChoice = new ArrayList<>();
    private int score = 0;
    @FXML
    public Label Score = new Label();



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

        currentCollectionList = WordCollectionDao.queryWordInCollection(currentCollectionName);
        realAnswer = currentCollectionList.get(idOfDisplayWord);
        //currentCollectionList = quiz.generateOneQuiz(currentCollectionName, realAnswer);
        currentCollectionChoice = quiz.generateOneQuiz(currentCollectionName, realAnswer);
        currentCollectionChoice.add(realAnswer);
        Collections.shuffle(currentCollectionChoice);
        setQuestion();
        clearSelected();
        progress.setText("0/"+currentCollectionList.size()+" words");
    }

    public void setQuestion() {
        realAnswer = currentCollectionList.get(idOfDisplayWord);
        question.setText("What is the meaning of " + realAnswer.getWord() + " ?");
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
        if (idOfDisplayWord >= currentCollectionList.size()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("You finished this collection, wanna restart?");
            alert.showAndWait();
            idOfDisplayWord=0;
        }
        realAnswer = currentCollectionList.get(idOfDisplayWord);
        currentCollectionChoice = quiz.generateOneQuiz(currentCollectionName, realAnswer);
        currentCollectionChoice.add(realAnswer);
        rightOrFalse.setText("");
        progress.setText((idOfDisplayWord+1)+"/"+currentCollectionList.size()+" words");
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
        if (selectedToggleButton.getText().equals(realAnswer.getMeaning())) {
            score++;
            Score.setText("Score: " + score + "/" + currentCollectionList.size());
            rightOrFalse.getStyleClass().clear();
            rightOrFalse.getStyleClass().add("whenTrue");
            rightOrFalse.setText("Correct!");
            rightOrFalse.setVisible(true);
        } else {
            Score.setText("Score: "+ score + "/" + currentCollectionList.size());
            rightOrFalse.getStyleClass().clear();
            rightOrFalse.getStyleClass().add("whenFalse");
            rightOrFalse.setText("Wrong");
            rightOrFalse.setVisible(true);
        }
    }

}
