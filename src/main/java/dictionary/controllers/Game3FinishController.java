package dictionary.controllers;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class Game3FinishController extends game {
  @FXML
  public Label lblLevel = new Label(), lblCorrect = new Label(), lblOverall = new Label();
  @FXML
  public JFXButton btnDone = new JFXButton(), btnRestart = new JFXButton();

  public Game3Controller mainController;
  @FXML
  public void initialize(URL location, ResourceBundle Resources) {
    lblLevel.setText("Level: " + Game3Controller.gameLevel);
    lblCorrect.setText(String.format("Correct: %d/%d", Game3Controller.correct, Game3Controller.numSentences));
    lblOverall.setText(String.format("Overall Score: %.2f%%", Game3Controller.overallScore));

    btnDone.setOnAction(e -> {
      mainController.openMenuScreen();
    });
    btnRestart.setOnAction(e -> {
      mainController.openPlayScreen();
    });
  }

  public void setMainController(Game3Controller c) {
    mainController = c;
  }


}
