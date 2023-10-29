package dictionary.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.control.Button;

import javafx.scene.control.*;

public class GameController implements Initializable {
    @FXML
    private AnchorPane gameContainer = new AnchorPane();
    @FXML
    private Button reviewBtn, quizBtn, quiz2Btn;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        quizBtn.setOnAction(event -> showComponent("/view/GameQuiz.fxml"));

        reviewBtn.setOnAction(event -> showComponent("/view/GameReview.fxml"));

        quiz2Btn.setOnAction(actionEvent -> showComponent("/view/GameQuiz2.fxml"));

        showComponent("/view/GameReview.fxml");
    }

    public void setNode(Node node) {
        gameContainer.getChildren().clear();
        gameContainer.getChildren().add(node);
    }
    @FXML
    public void showComponent(String path) {
        try {
            AnchorPane component = FXMLLoader.load(Objects.requireNonNull(MainController.class.getResource(path)));
            setNode(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
