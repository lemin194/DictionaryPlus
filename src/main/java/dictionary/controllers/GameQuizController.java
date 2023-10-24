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
import java.util.List;
import java.util.ResourceBundle;


public class GameQuizController implements Initializable {
    ToggleGroup group = new ToggleGroup();

    @FXML
    private RadioButton ansA = new RadioButton();
    @FXML
    private RadioButton ansB = new RadioButton();
    @FXML
    private RadioButton ansC = new RadioButton();
    @FXML
    private RadioButton ansD = new RadioButton();
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


    @Override
    public void initialize(URL location, ResourceBundle Resources) {
        int n = WordCollectionDao.queryCollectionName().size();
        for (int i = 0; i < n; i++) {
            collectionsToPlay.getItems().add(WordCollectionDao.queryCollectionName().get(i));
        }
        for (RadioButton button : List.of(ansA, ansB, ansC, ansD)) {
            button.setToggleGroup(group);
        }
        for (RadioButton button : List.of(ansA, ansB, ansC, ansD)) {
            button.setOnAction(this::whenSelected);
        }
    }
    @FXML
    public void startGame() {
        currentCollectionName = collectionsToPlay.getValue();
        currentCollectionList = WordCollectionDao.queryWordInCollection(currentCollectionName);
        realAnswer = currentCollectionList.get(idOfDisplayWord);
        currentCollectionList = quiz.generateOneQuiz(currentCollectionName, realAnswer);
        currentCollectionList.add(realAnswer);
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
        for (RadioButton button : List.of(ansA, ansB, ansC, ansD)) {
            button.setText(currentCollectionList.get(tmp).getMeaning());
            button.setWrapText(true);
            tmp++;
        }
        clearSelected();
    }

    public void clearSelected() {
        for (RadioButton button : List.of(ansA, ansB, ansC, ansD)) {
            button.setSelected(false);
        }
    }
    @FXML
    public void nextQuestion() {
        idOfDisplayWord++;
        if (idOfDisplayWord >= currentCollectionList.size()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("You finished this collection, wanna restart?");
            alert.showAndWait();
            idOfDisplayWord=0;
        }
        realAnswer = currentCollectionList.get(idOfDisplayWord);
        rightOrFalse.setText("");
        progress.setText((idOfDisplayWord+1)+"/"+currentCollectionList.size()+" words");
        setQuestion();
    }

    public void whenSelected(ActionEvent e) {
        RadioButton selectedRadioButton = (RadioButton) e.getSource();
        System.out.println(selectedRadioButton.getText() + " selected");

        group.getToggles().forEach(toggle -> {
            RadioButton radioButton = (RadioButton) toggle;
            if (!radioButton.equals(selectedRadioButton)) {
                radioButton.setDisable(true);
            }
        });
        if (selectedRadioButton.getText().equals(realAnswer.getMeaning())) {
            rightOrFalse.setText("Correct!");
        } else {
            rightOrFalse.setText("Wrong");
        }
    }

}
