package dictionary.controllers;

import dictionary.models.Dao.WordCollectionDao;
import dictionary.models.Entity.Word;
import dictionary.services.ImageService;
import dictionary.services.QuizService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

import static dictionary.services.StringUtils.getFirstMeaning;


public class Game2Controller extends game {
    /* in game3 it is btnNext.*/
    @FXML
    public AnchorPane question1;
    @FXML
    public Button btnNext;

    /* close the window.*/
    @FXML
    public Button btnClose;

    /* in game 3; it's btnPlay*/
    @FXML
    public Button btnSpeaker;
    @FXML
    public Button btnRestart;
    @FXML
    public Label warning;
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
    @FXML
    private ImageView image = new ImageView();
    private final QuizService quiz = new QuizService();
    private String answer;
    private List<Word> currentChoiceList = new ArrayList<>();
    public ImageService imgService = new ImageService();

    @Override
    public void initialize(URL location, ResourceBundle Resources) {
        for (ToggleButton button : List.of(ansA, ansB, ansC, ansD)) {
            button.setToggleGroup(group);
        }
        for (ToggleButton button : List.of(ansA, ansB, ansC, ansD)) {
            button.setOnAction(this::whenSelected);
        }
        imgService = new ImageService();
        image.setImage(imgService.loadImage("ability"));
       // rightOrFalse.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/review.css")).toExternalForm());
        //rightOrFalse.setVisible(false);
        //warning.setVisible(false);
    }
    @FXML
    public void startGame() {
        currentCollectionName = collectionsToPlay.getValue();
        currentWordList = WordCollectionDao.queryWordInCollection(currentCollectionName);
        if (currentWordList.isEmpty()) {
            warning.setVisible(true);
            question.setText("");
            for (ToggleButton button : List.of(ansA, ansB, ansC, ansD)) {
                button.setText("");
            }
            return;
        } else {
            warning.setVisible(false);
            currentWord = currentWordList.get(idOfDisplayWord);
            //currentCollectionList = quiz.generateOneQuiz(currentCollectionName, realAnswer);
            currentChoiceList = quiz.generateOneQuiz(currentCollectionName, currentWord);
            currentChoiceList.add(currentWord);
            Collections.shuffle(currentChoiceList);
            setQuestion();
            clearSelected();
            progress.setText("0/" + currentWordList.size() + " words");
        }
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
            button.setText(getFirstMeaning(currentChoiceList.get(tmp)));
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
        if (currentWordList.isEmpty()) return;
        rightOrFalse.setVisible(false);
        idOfDisplayWord++;
        if (idOfDisplayWord >= currentWordList.size()) {
            progress.setText((idOfDisplayWord)+"/"+ currentWordList.size()+" words");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            DialogPane tmp1 = alert.getDialogPane();
            tmp1.getStylesheets().add(getClass().getResource("/style/dialog.css").toExternalForm());
            alert.setContentText("You finished this collection with the score: " + (int) finalScore
                    + ". Press the button below to start again");
            alert.showAndWait();
            finalScore = 0;
            idOfDisplayWord=0;
        }
        currentWord = currentWordList.get(idOfDisplayWord);
        currentChoiceList = quiz.generateOneQuiz(currentCollectionName, currentWord);
        currentChoiceList.add(currentWord);
        Collections.shuffle(currentChoiceList);
        rightOrFalse.setText("");
        progress.setText((idOfDisplayWord)+"/"+ currentWordList.size()+" words");
        setQuestion();
    }

    public void whenSelected(ActionEvent e) {
        if (currentWordList.isEmpty()) return;
        System.out.println(currentWord.getMeaning());
        ToggleButton selectedToggleButton = (ToggleButton) e.getSource();
        System.out.println(selectedToggleButton.getText() + " selected");

        group.getToggles().forEach(toggle -> {
            ToggleButton toggleButton = (ToggleButton) toggle;
            if (!toggleButton.equals(selectedToggleButton)) {
                toggleButton.setDisable(true);
            }
        });
        if (selectedToggleButton.getText().equals(getFirstMeaning(currentWord))) {
            finalScore++;
            lblScore.setText("Score: " + (int) finalScore + "/" + currentWordList.size());
            rightOrFalse.getStyleClass().clear();
            rightOrFalse.getStyleClass().add("whenTrue");
            rightOrFalse.setText("Correct!");
            rightOrFalse.setVisible(true);
        } else {
            lblScore.setText("Score: "+ (int) finalScore + "/" + currentWordList.size());
            rightOrFalse.getStyleClass().clear();
            rightOrFalse.getStyleClass().add("whenFalse");
            rightOrFalse.setText("Wrong");
            rightOrFalse.setVisible(true);
        }
    }
    @FXML
    public void restart() {
        if (currentWordList.isEmpty()) return;
        Collections.shuffle(currentWordList);
        idOfDisplayWord = 0;
        currentWord = currentWordList.get(idOfDisplayWord);
        currentChoiceList = quiz.generateOneQuiz(currentCollectionName, currentWord);
        currentChoiceList.add(currentWord);
        setQuestion();
        lblScore.setText("Score: 0");
        progress.setText("0/" + currentWordList.size() + " words");
        System.out.println("restart button does work");
        rightOrFalse.setVisible(false);
    }
    @FXML
    public void handleClose(){
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}
