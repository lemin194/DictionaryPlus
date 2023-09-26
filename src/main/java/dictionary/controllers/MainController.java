package dictionary.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {
    @FXML
    private Button button;

    @FXML
    protected void onButtonClick() {
        button.setText("Testing");
    }
}