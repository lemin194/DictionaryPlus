package dictionary.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameReviewController implements Initializable {
    @FXML
    private AnchorPane reviewContainer = new AnchorPane();
    @FXML
    private Button playBtn, editCollectionBtn;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playBtn.setOnAction(event -> showComponent("/view/GameReviewPlay.fxml"));

        editCollectionBtn.setOnAction(event -> showComponent("/view/GameReviewAdd.fxml"));

        showComponent("/view/GameReviewPlay.fxml");
    }

    public void setNode(Node node) {
        reviewContainer.getChildren().clear();
        reviewContainer.getChildren().add(node);
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
