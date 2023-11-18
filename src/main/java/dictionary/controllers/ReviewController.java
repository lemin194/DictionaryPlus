package dictionary.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ReviewController implements Initializable {
    @FXML
    private AnchorPane reviewContainer = new AnchorPane();
    @FXML
    private Button playBtn, editCollectionBtn;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        playBtn.setOnAction(event -> showComponent("/view/ReviewPlay.fxml"));

        editCollectionBtn.setOnAction(event -> showComponent("/view/ReviewAdd.fxml"));

        showComponent("/view/ReviewPlay.fxml");
    }

    public void setNode(Node node) {
        reviewContainer.getChildren().clear();
        reviewContainer.getChildren().add(node);
    }
    private void setMode(String mode) {

        if (mode.equals("/view/ReviewAdd.fxml")) {
            editCollectionBtn.getStyleClass().clear();
            playBtn.getStyleClass().clear();

            editCollectionBtn.getStyleClass().add("buttonReviewAddSelected");
            playBtn.getStyleClass().add("transparent-button");
        } else if (mode.equals("/view/ReviewPlay.fxml")) {
            editCollectionBtn.getStyleClass().clear();
            playBtn.getStyleClass().clear();

            editCollectionBtn.getStyleClass().add("transparent-button");
            playBtn.getStyleClass().add("buttonReviewAddSelected");
        }
    }
    @FXML
    public void showComponent(String path) {
        try {
            AnchorPane component = FXMLLoader.load(Objects.requireNonNull(MainController.class.getResource(path)));
            setNode(component);
            setMode(path);
            System.out.println("HIIH");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
