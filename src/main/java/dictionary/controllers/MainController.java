package dictionary.controllers;

import dictionary.models.Dao.Utils;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

import javafx.scene.control.Tooltip;

public class MainController implements Initializable{
    @FXML
    private Label logo = new Label();
    @FXML
    private Button searchBtn, translateBtn, reviewBtn, editBtn, exitBtn;

    @FXML
    public Tooltip searchTooltip, translateTooltip, reviewTooltip, editTooltip, exitTooltip;

    @FXML
    public AnchorPane container = new AnchorPane();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchBtn.setOnAction(event -> showComponent("/view/Search.fxml"));

        reviewBtn.setOnAction(event -> showComponent("/view/Review.fxml"));

        translateBtn.setOnAction(event -> showComponent("/view/Translate.fxml"));

        editBtn.setOnAction(event -> showComponent("/view/Edit.fxml"));

        showComponent("/view/Search.fxml");

        exitBtn.setOnMouseClicked(e -> exit());

    }

    public void exit() {
        Utils utils = new Utils();
        utils.storeCollection();
        utils.storeLastFind();
        System.exit(0);
    }

    public void setNode(Node node) {
        container.getChildren().clear();
        container.getChildren().add(node);
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
