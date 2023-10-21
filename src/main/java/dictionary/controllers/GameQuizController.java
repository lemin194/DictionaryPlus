package dictionary.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;


public class GameQuizController {
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
    private TextArea question = new TextArea();
    @FXML
    private Button speaker = new Button();
    @FXML
    private Button next = new Button();

}
