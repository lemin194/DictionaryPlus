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
    private List<String> currentChoiceList = new ArrayList<>();
    public ImageService imgService = new ImageService();
    public String sol;

    @Override
    public void initialize(URL location, ResourceBundle Resources) {
        for (ToggleButton button : List.of(ansA, ansB, ansC, ansD)) {
            button.setToggleGroup(group);
        }
        for (ToggleButton button : List.of(ansA, ansB, ansC, ansD)) {
            button.setOnAction(this::whenSelected);
        }
        imgService = new ImageService();
        //image.setImage(imgService.loadImage("ability"));
       setQuestion();
        rightOrFalse.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/review.css")).toExternalForm());
        rightOrFalse.setVisible(false);
        warning.setVisible(false);
    }
//    @FXML
//    public void startGame() {
//        currentCollectionName = collectionsToPlay.getValue();
//        currentWordList = WordCollectionDao.queryWordInCollection(currentCollectionName);
//        if (currentWordList.isEmpty()) {
//            warning.setVisible(true);
//            question.setText("");
//            for (ToggleButton button : List.of(ansA, ansB, ansC, ansD)) {
//                button.setText("");
//            }
//            return;
//        } else {
//            warning.setVisible(false);
//            currentWord = currentWordList.get(idOfDisplayWord);
//            //currentCollectionList = quiz.generateOneQuiz(currentCollectionName, realAnswer);
//            currentChoiceList = quiz.generateOneQuiz(currentCollectionName, currentWord);
//            currentChoiceList.add(currentWord);
//            Collections.shuffle(currentChoiceList);
//            setQuestion();
//            clearSelected();
//            progress.setText("0/" + currentWordList.size() + " words");
//        }
//    }

    public void setQuestion() {
        currentChoiceList = ImageService.randWord();
        Random rand = new Random();
        int randIndex = rand.nextInt(4);
        sol = currentChoiceList.get(randIndex);
        image.setImage(imgService.loadImage(sol));
        ansA.setDisable(false);
        ansB.setDisable(false);
        ansC.setDisable(false);
        ansD.setDisable(false);
        int tmp = 0;
        for (ToggleButton button : List.of(ansA, ansB, ansC, ansD)) {
            button.setText(currentChoiceList.get(tmp));
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
        warning.setVisible(false);
        rightOrFalse.setVisible(false);
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
        if (selectedToggleButton.getText().equals(sol)) {
            //finalScore++;
            //lblScore.setText("Score: " + (int) finalScore + "/" + currentWordList.size());
            rightOrFalse.getStyleClass().clear();
            rightOrFalse.getStyleClass().add("whenTrue");
            rightOrFalse.setText("Correct!");
            rightOrFalse.setVisible(true);
            warning.setVisible(false);
        } else {
            //lblScore.setText("Score: "+ (int) finalScore + "/" + currentWordList.size());
            rightOrFalse.getStyleClass().clear();
            rightOrFalse.getStyleClass().add("whenFalse");
            rightOrFalse.setText("Wrong");
            rightOrFalse.setVisible(true);
            warning.setText("The right answer is: " + sol);
            warning.setVisible(true);
        }
    }
    @FXML
    public void handleClose(){
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}
