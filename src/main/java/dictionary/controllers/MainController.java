package dictionary.controllers;


import com.jfoenix.controls.JFXButton;
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
    private Button searchBtn, translateBtn, reviewBtn, exitBtn, gameBtn;
    @FXML
    private Button homeBtn;
    @FXML
    public AnchorPane container = new AnchorPane();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchBtn.setOnAction(event -> showComponent("/view/Search.fxml"));

        reviewBtn.setOnAction(event -> showComponent("/view/Review.fxml"));

        translateBtn.setOnAction(event -> showComponent("/view/Translate.fxml"));

        gameBtn.setOnAction(event -> showComponent("/view/Game.fxml"));

        homeBtn.setOnAction(event -> showComponent("/view/UserGuide.fxml"));

        exitBtn.setOnMouseClicked(e -> exit());

        showComponent("/view/UserGuide.fxml");
    }

    private void setMode(String mode) {

            if(mode.equals("/view/Search.fxml")) {
                searchBtn.getStyleClass().clear();
                translateBtn.getStyleClass().clear();
                gameBtn.getStyleClass().clear();
                reviewBtn.getStyleClass().clear();
                exitBtn.getStyleClass().clear();
                homeBtn.getStyleClass().clear();

                searchBtn.getStyleClass().add("transparent-button-selected");
                translateBtn.getStyleClass().add("transparent-button");
                gameBtn.getStyleClass().add("transparent-button");
                reviewBtn.getStyleClass().add("transparent-button");
                exitBtn.getStyleClass().add("transparent-button");
                homeBtn.getStyleClass().add("transparent-button");
            } else if(mode.equals("/view/Translate.fxml")) {
                searchBtn.getStyleClass().clear();
                translateBtn.getStyleClass().clear();
                gameBtn.getStyleClass().clear();
                reviewBtn.getStyleClass().clear();
                exitBtn.getStyleClass().clear();
                homeBtn.getStyleClass().clear();

                searchBtn.getStyleClass().add("transparent-button");
                translateBtn.getStyleClass().add("transparent-button-selected");
                gameBtn.getStyleClass().add("transparent-button");
                reviewBtn.getStyleClass().add("transparent-button");
                exitBtn.getStyleClass().add("transparent-button");
                homeBtn.getStyleClass().add("transparent-button");
            } else if(mode.equals("/view/Game.fxml")) {
                searchBtn.getStyleClass().clear();
                translateBtn.getStyleClass().clear();
                gameBtn.getStyleClass().clear();
                reviewBtn.getStyleClass().clear();
                exitBtn.getStyleClass().clear();
                homeBtn.getStyleClass().clear();

                searchBtn.getStyleClass().add("transparent-button");
                translateBtn.getStyleClass().add("transparent-button");
                gameBtn.getStyleClass().add("transparent-button-selected");
                reviewBtn.getStyleClass().add("transparent-button");
                exitBtn.getStyleClass().add("transparent-button");
                homeBtn.getStyleClass().add("transparent-button");
            }   else if(mode.equals("/view/Review.fxml")) {
                searchBtn.getStyleClass().clear();
                translateBtn.getStyleClass().clear();
                gameBtn.getStyleClass().clear();
                reviewBtn.getStyleClass().clear();
                exitBtn.getStyleClass().clear();
                homeBtn.getStyleClass().clear();

                searchBtn.getStyleClass().add("transparent-button");
                translateBtn.getStyleClass().add("transparent-button");
                gameBtn.getStyleClass().add("transparent-button");
                reviewBtn.getStyleClass().add("transparent-button-selected");
                exitBtn.getStyleClass().add("transparent-button");
                homeBtn.getStyleClass().add("transparent-button");
            } else if(mode.equals("/view/UserGuide.fxml")) {
                searchBtn.getStyleClass().clear();
                translateBtn.getStyleClass().clear();
                gameBtn.getStyleClass().clear();
                reviewBtn.getStyleClass().clear();
                exitBtn.getStyleClass().clear();
                homeBtn.getStyleClass().clear();

                searchBtn.getStyleClass().add("transparent-button");
                translateBtn.getStyleClass().add("transparent-button");
                gameBtn.getStyleClass().add("transparent-button");
                reviewBtn.getStyleClass().add("transparent-button");
                exitBtn.getStyleClass().add("transparent-button");
                homeBtn.getStyleClass().add("transparent-button-selected");
            }
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
            System.out.println("sap het loi roi");
            setMode(path);
            setNode(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
