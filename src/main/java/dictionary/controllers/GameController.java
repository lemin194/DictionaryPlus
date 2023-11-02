package dictionary.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.control.Button;

public class GameController implements Initializable {
    @FXML
    private AnchorPane gameContainer = new AnchorPane();
    @FXML
    private Button quizBtn, quiz2Btn;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        quizBtn.setOnAction(event -> showComponent("/view/Game1.fxml"));

        quiz2Btn.setOnAction(actionEvent -> showComponent("/view/Game2.fxml"));

        showComponent("/view/Game1.fxml");
    }

    public void setNode(Node node) {
        gameContainer.getChildren().clear();
        gameContainer.getChildren().add(node);
    }
    private void setMode(String mode) {

        if (mode.equals("/view/Game1.fxml")) {
            quizBtn.getStyleClass().clear();
            quiz2Btn.getStyleClass().clear();

            quizBtn.getStyleClass().add("transparent-button-selected");
            quiz2Btn.getStyleClass().add("transparent-button");
        } else if (mode.equals("/view/Game2.fxml")) {
            quizBtn.getStyleClass().clear();
            quiz2Btn.getStyleClass().clear();

            quizBtn.getStyleClass().add("transparent-button");
            quiz2Btn.getStyleClass().add("transparent-button-selected");
        }
    }
    @FXML
    public void showComponent(String path) {
        try {
            AnchorPane component = FXMLLoader.load(Objects.requireNonNull(MainController.class.getResource(path)));
            setNode(component);
            setMode(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
