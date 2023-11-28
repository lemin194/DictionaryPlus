package dictionary.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;


public class Game3MenuController extends game {

  @FXML
  private ChoiceBox<String> choiceBoxLevel = new ChoiceBox<>();
  @FXML
  private ChoiceBox<Integer> choiceBoxNumSentences = new ChoiceBox<>();
  private Game3Controller mainController;
  @FXML
  public void initialize(URL location, ResourceBundle Resources) {
    initMainMenu();
    System.out.println("menu");
  }

  private void initMainMenu() {
    choiceBoxLevel.getItems().addAll("A1", "A2", "B1", "B2", "C1", "C2");
    choiceBoxLevel.setValue("B1");
    choiceBoxNumSentences.getItems().addAll(2, 5, 10, 15);
    choiceBoxNumSentences.setValue(5);
    Game3Controller.gameLevel = choiceBoxLevel.getValue();
    Game3Controller.numSentences = choiceBoxNumSentences.getValue();
    choiceBoxLevel.setOnAction(e -> {
      Game3Controller.gameLevel = choiceBoxLevel.getValue();
    });
    choiceBoxNumSentences.setOnAction(e -> {
      Game3Controller.numSentences = choiceBoxNumSentences.getValue();
    });
  }

  @FXML
  private void startGame() {
    this.mainController.openPlayScreen();
  }

  public void setMainController(Game3Controller c) {
    this.mainController = c;
  }

}
