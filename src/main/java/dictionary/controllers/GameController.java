package dictionary.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameController implements Initializable {
    double xOffset = 0;
    double yOffset = 0;
    @FXML
    private AnchorPane gameContainer = new AnchorPane();
    @FXML
    private Button quizBtn = new Button();

    @FXML
    private Button quiz2Btn = new Button();
    @FXML
    private Button quiz3Btn = new Button();
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        quizBtn.setOnAction(event -> handleGame1Button());
        quiz2Btn.setOnAction(event -> handleGame2Button());


        quiz3Btn.setOnAction(actionEvent -> handleGame3Button());
    }

    /*public void setNode(Node node) {
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
    }*/
    @FXML
    public void handleGame1Button() {
        try {
            Stage stage = new Stage();
            stage.setTitle("Game 1: Quiz");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Game1.fxml"));
            Parent root = fxmlLoader.load();
            stage.initStyle(StageStyle.TRANSPARENT);

            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });
            Scene scene = new Scene(root, Color.web("1F1F1F"));
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(getClass().getResource("/style/review.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Can't create new scene for game1");
        }
    }
    @FXML
    public void handleGame2Button() {
        try {
            Stage stage = new Stage();
            stage.setTitle("Game 1: Quiz");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Game2.fxml"));
            Parent root = fxmlLoader.load();
            stage.initStyle(StageStyle.TRANSPARENT);

            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });
            Scene scene = new Scene(root, Color.web("1F1F1F"));
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(getClass().getResource("/style/review.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can't create new scene for game1");
        }
    }
    /* Duc write your code here. Controller file for your code can be written in Game3 and Game3Controller.*/
    @FXML
    public void handleGame3Button() {
        try {
            Stage stage = new Stage();
            stage.setTitle("Game 3: Practice speaking");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Game3.fxml"));
            Parent root1 = fxmlLoader.load();
            Scene scene = new Scene(root1, Color.web("FFFFFF"));
            scene.setFill(Color.TRANSPARENT);
            // Duc can change the css file for his  game here
            // also if you want to throw dialog or use some dialog in your game;
            // you can set style for dialog similarly like this => getStyleSheets to set the css file
            scene.getStylesheets().add(getClass().getResource("/style/review.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
//            System.out.println("Can't create new scene for game3");
        }
    }
}
