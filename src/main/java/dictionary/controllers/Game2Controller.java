package dictionary.controllers;

import dictionary.services.WordleService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Game2Controller {
    @FXML
    public GridPane userGuess = new GridPane();
    @FXML
    public GridPane keyboardRow1 = new GridPane();
    @FXML
    public GridPane keyboardRow2 = new GridPane();
    @FXML
    public GridPane keyboardRow3 = new GridPane();
    @FXML
    public Button restart;
    @FXML
    public Button back;
    @FXML
    public Label finishFlag = new Label("");
    @FXML
    public Label information = new Label("");

    private final String[] firstRowLetters = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"};
    private final String[] secondRowLetters = {"A", "S", "D", "F", "G", "H", "J", "K", "L"};
    private final String[] thirdRowLetters = {"↵", "Z", "X", "C", "V", "B", "N", "M", "←"};

    private int CURRENT_ROW = 1;
    private int CURRENT_COLUMN = 1;
    private final int MAX_COLUMN = 5;
    private final int MAX_ROW = 6;
    private String answer;

    public void createUI() {
        // create gridPane user guess
        for (int i = 1; i <= MAX_ROW; i++) {
            for (int j = 1; j <= MAX_COLUMN; j++) {
                Label label = new Label();
                label.getStyleClass().add("default-tile");
                userGuess.add(label, j, i);
            }
        }
        for (int i = 0; i < firstRowLetters.length; i++) {
            Label label = new Label();
            label.getStyleClass().add("keyboardTile");
            label.setText(firstRowLetters[i]);
            keyboardRow1.add(label, i, 1);
        }
        for (int i = 0; i < secondRowLetters.length; i++) {
            Label label = new Label();
            label.getStyleClass().add("keyboardTile");
            label.setText(secondRowLetters[i]);
            keyboardRow2.add(label, i, 2);
        }
        for (int i = 0; i < thirdRowLetters.length; i++) {
            Label label = new Label();
            if (i == 0 || i == thirdRowLetters.length - 1)
                label.getStyleClass().add("keyboardTileSymbol");
            else
                label.getStyleClass().add("keyboardTile");
            label.setText(thirdRowLetters[i]);
            keyboardRow3.add(label, i, 3);
        }
    }

    public void userGuessFocus() {
        userGuess.requestFocus();
    }

    // utils to handle game events
    // get label by index in grid pane
    private Label getLabel(GridPane gridPane, int searchRow, int searchColumn) {
        for (Node child : gridPane.getChildren()) {
            Integer r = GridPane.getRowIndex(child);
            Integer c = GridPane.getColumnIndex(child);
            int row = r == null ? 0 : r;
            int column = c == null ? 0 : c;
            if (row == searchRow && column == searchColumn && (child instanceof Label))
                return (Label) child;
        }
        return null;
    }
    // get label by text
    private Label getLabel(GridPane gridPane, String letter) {
        Label label;
        for (Node child : gridPane.getChildren()) {
            if (child instanceof Label) {
                label = (Label) child;
                if (letter.equalsIgnoreCase(label.getText()))
                    return label;
            }
        }
        return null;
    }
    // set-get text for label
    private void setLabelText(GridPane gridPane, int searchRow, int searchColumn, String input) {
        Label label = getLabel(gridPane, searchRow, searchColumn);
        if (label != null)
            label.setText(input.toUpperCase());
    }
    private String getLabelText(GridPane gridPane, int searchRow, int searchColumn) {
        Label label = getLabel(gridPane, searchRow, searchColumn);
        if (label != null)
            return label.getText().toLowerCase();
        return null;
    }
    // set-clear style for label
    private void setLabelStyleClass(GridPane gridPane, int searchRow, int searchColumn, String styleclass) {
        Label label = getLabel(gridPane, searchRow, searchColumn);
        if (label != null) {
            label.getStyleClass().add(styleclass);
        }
    }

    private void clearLabelStyleClass(GridPane gridPane, int searchRow, int searchColumn) {
        Label label = getLabel(gridPane, searchRow, searchColumn);
        if (label != null) {
            label.getStyleClass().clear();
        }
    }

    // update color while playing
    // userGuess grid
    private void updateUserGuessColors(int searchRow) {

        for (int i = 1; i <= MAX_COLUMN; i++) {
            Label label = getLabel(userGuess, searchRow, i);
            String styleClass;
            if (label != null) {
                String currentCharacter = String.valueOf(label.getText().charAt(0)).toLowerCase();
                if (String.valueOf(answer.charAt(i - 1)).toLowerCase().equals(currentCharacter)) {
                    styleClass = "correct-letter";
                } else if (answer.contains(currentCharacter)) {
                    styleClass = "present-letter";
                } else {
                    styleClass = "wrong-letter";
                }
                label.getStyleClass().clear();
                label.getStyleClass().setAll(styleClass);
            }
        }
    }


    // keyboard
    private String getWordFromCurrentRow() {
        StringBuilder input = new StringBuilder();
        for (int j = 1; j <= MAX_COLUMN; j++)
            input.append(getLabelText(userGuess, CURRENT_ROW, j));
        return input.toString();
    }

    private boolean checkRowContainsLetter(String[] array, String letter) {
        for (String string : array)
            if (string.equalsIgnoreCase(letter))
                return true;
        return false;
    }
    private void updateKeyboardColors() {
        String currentWord = getWordFromCurrentRow().toLowerCase();
        for (int i = 1; i <= MAX_COLUMN; i++) {
            Label keyboardLabel = new Label();
            String styleClass;
            String currentCharacter = String.valueOf(currentWord.charAt(i - 1));
            String winningCharacter = String.valueOf(answer.charAt(i - 1));

            if (checkRowContainsLetter(firstRowLetters, currentCharacter))
                keyboardLabel = getLabel(keyboardRow1, currentCharacter);
            else if (checkRowContainsLetter(secondRowLetters, currentCharacter))
                keyboardLabel = getLabel(keyboardRow2, currentCharacter);
            else if (checkRowContainsLetter(thirdRowLetters, currentCharacter))
                keyboardLabel = getLabel(keyboardRow3, currentCharacter);

            if (currentCharacter.equals(winningCharacter))
                styleClass = "keyboardCorrectColor";
            else if (answer.contains("" + currentCharacter))
                styleClass = "keyboardPresentColor";
            else
                styleClass = "keyboardWrongColor";
            if (keyboardLabel != null) {
                keyboardLabel.getStyleClass().clear();
                keyboardLabel.getStyleClass().add(styleClass);
            }
        }
    }


    // handle user's input
    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        System.out.println("game 2 does work");
        if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
            handleBackspacePressed();
        } else if (keyEvent.getCode().isLetterKey()) {
            handleLetterPressed(keyEvent);
        }
        if (keyEvent.getCode() == KeyCode.ENTER) {
            handleEnterPressed();
        }
    }
    private void handleLetterPressed(KeyEvent keyEvent) {
        if (Objects.equals(getLabelText(userGuess, CURRENT_ROW, CURRENT_COLUMN), "")) {
            setLabelText(userGuess, CURRENT_ROW, CURRENT_COLUMN, keyEvent.getText());
            setLabelStyleClass(userGuess, CURRENT_ROW, CURRENT_COLUMN, "tile-with-letter");
            if (CURRENT_COLUMN < MAX_COLUMN)
                CURRENT_COLUMN++;
        }
    }
    private void handleBackspacePressed() {
        if ((CURRENT_COLUMN == MAX_COLUMN || CURRENT_COLUMN == 1)
                && !Objects.equals(getLabelText(userGuess, CURRENT_ROW, CURRENT_COLUMN), "")) {
            setLabelText(userGuess, CURRENT_ROW, CURRENT_COLUMN, "");
            clearLabelStyleClass(userGuess, CURRENT_ROW, CURRENT_COLUMN);
            setLabelStyleClass(userGuess, CURRENT_ROW, CURRENT_COLUMN, "default-tile");
        } else if ((CURRENT_COLUMN > 1 && CURRENT_COLUMN < MAX_COLUMN)
                || (CURRENT_COLUMN == MAX_COLUMN && Objects.equals(getLabelText(userGuess, CURRENT_ROW, CURRENT_COLUMN), ""))) {
            CURRENT_COLUMN--;
            setLabelText(userGuess, CURRENT_ROW, CURRENT_COLUMN, "");
            clearLabelStyleClass(userGuess, CURRENT_ROW, CURRENT_COLUMN);
            setLabelStyleClass(userGuess, CURRENT_ROW, CURRENT_COLUMN, "default-tile");
        }
    }
    private void handleEnterPressed() {
        if (CURRENT_ROW <= MAX_ROW && CURRENT_COLUMN == MAX_COLUMN) {
            String guess = getWordFromCurrentRow().toLowerCase();
            if (guess.equals(answer)) {
                updateUserGuessColors(CURRENT_ROW);
                updateKeyboardColors();
                finishFlag.setText("you win");
                finishFlag.setVisible(true);
            } else if (WordleService.validGuess(guess)) {
                updateUserGuessColors(CURRENT_ROW);
                updateKeyboardColors();
                finishFlag.setText("you win");
                finishFlag.setVisible(true);
                if (CURRENT_ROW == MAX_ROW) {
                    return;
                }
                CURRENT_ROW++;
                CURRENT_COLUMN = 1;
            } else {
                information.setText("Word does not exist!");
                information.setVisible(true);
            }
        }
    }

    @FXML
    public void handleBack() {
        Stage stage = (Stage) back.getScene().getWindow();
        stage.close();
    }
}
