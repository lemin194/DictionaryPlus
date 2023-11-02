package dictionary.controllers;

import dictionary.models.Dao.WordCollectionDao;
import dictionary.models.Entity.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Game2Controller implements Initializable {

    @FXML
    private ChoiceBox<String> collectionsToPlay = new ChoiceBox<>();

    @FXML
    private Label currentResult;
    private StringBuilder currentResultString = new StringBuilder();

    @FXML
    private Label hangMan;

    @FXML
    private Button nextWord;

    @FXML
    private Button restart;

    @FXML
    private Button startPlaying;

    @FXML
    private TextField typeUserGuess;
    @FXML
    private Label currentTrack;
    private int wordLeft;

    private String currentCollection = "";
    private List<Word> current = new ArrayList<>();
    private Word displayWord;
    private int idOfDisplayWord;
    @FXML
    private Button submit;

    private int chanceLeft = 7;
    private String FIGURE[] = {
            "   -------------    \n"+
            "   |                \n"+
            "   |                \n"+
            "   |                \n"+
            "   |                \n"+
            "   |     \n"+
            " -----   \n",
            "   -------------    \n"+
            "   |           |    \n"+
            "   |                \n"+
            "   |                \n"+
            "   |                \n"+
            "   |     \n"+
            " -----   \n",
            "   -------------    \n"+
            "   |           |    \n"+
            "   |           O    \n"+
            "   |                \n"+
            "   |                \n"+
            "   |     \n"+
            " -----   \n",
            "   -------------    \n"+
            "   |           |    \n"+
            "   |           O    \n"+
            "   |           |    \n"+
            "   |                \n"+
            "   |     \n"+
            " -----   \n",
            "   -------------    \n"+
            "   |           |    \n"+
            "   |           O    \n"+
            "   |          /|    \n"+
            "   |                \n"+
            "   |     \n"+
            " -----   \n",
            "   -------------    \n"+
            "   |           |    \n"+
            "   |           O    \n"+
            "   |          /|\\  \n"+
            "   |                \n"+
            "   |     \n"+
            " -----   \n",
            "   -------------    \n"+
            "   |           |    \n"+
            "   |           O    \n"+
            "   |          /|\\  \n"+
            "   |          /     \n"+
            "   |     \n"+
            " -----   \n",
            "   -------------    \n"+
            "   |           |    \n"+
            "   |           O    \n"+
            "   |          /|\\  \n"+
            "   |          / \\  \n"+
            "   |     \n"+
            " -----   \n"
    };

    @FXML
    public void initialize(URL location, ResourceBundle Resources) {
        int n = WordCollectionDao.queryCollectionName().size();
        for (int i = 0; i < n; i++) {
            collectionsToPlay.getItems().add(WordCollectionDao.queryCollectionName().get(i));
        }
    }

    @FXML
    public void startGame() {
        currentCollection = collectionsToPlay.getValue();
        current = WordCollectionDao.queryWordInCollection(currentCollection);
        idOfDisplayWord = 0;
        hangMan.setText(FIGURE[7-chanceLeft]);
        genLabel(idOfDisplayWord);
        currentResult.setText(currentResultString.toString());
        displayWord = current.get(idOfDisplayWord);
    }

    public void genLabel(int idOfDisplayWord) {
        Word tmp = current.get(idOfDisplayWord);
        for (int i = 0; i < tmp.getWord().length(); i++) {
            currentResultString.append('_');
        }
    }

    @FXML
    public void handleGame() {
        String CHAR = typeUserGuess.getText();
        String word = displayWord.getWord();
        if (word.indexOf(CHAR.charAt(0)) >= 0) {
            for (int i = word.length() - 1; i >= 0; i--) {
                if (word.charAt(i) == CHAR.charAt(0)) {
                        currentResultString.setCharAt(i, CHAR.charAt(0));
                }
            }
            currentResult.setText(currentResultString.toString());
        } else {
            chanceLeft--;
            hangMan.setText(FIGURE[7 - chanceLeft]);
        }
    }
}
