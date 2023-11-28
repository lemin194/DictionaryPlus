package dictionary.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;


public class Game3Controller extends game {

  @FXML
  public AnchorPane mainAnchorPane = new AnchorPane();

  public static String gameLevel = "A1";
  public static int numSentences = 5;
  public static int correct = 0;
  public static float overallScore = 0;

  @FXML
  public void initialize(URL location, ResourceBundle Resources) {
    openMenuScreen();
  }

  public void openMenuScreen() {
    mainAnchorPane.getChildren().clear();
    try {
      FXMLLoader loader = new FXMLLoader(
          MainController.class.getResource("/view/Game3Menu.fxml"));
      Game3MenuController c = new Game3MenuController();
      loader.setController(c);
      c.setMainController(this);
      mainAnchorPane.getChildren().add(loader.load());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void openPlayScreen() {
    mainAnchorPane.getChildren().clear();
    try {
      FXMLLoader loader = new FXMLLoader(
          (MainController.class.getResource("/view/Game3Play.fxml")));
      Game3PlayController c = new Game3PlayController();
      loader.setController(c);
      c.setMainController(this);
      mainAnchorPane.getChildren().add(loader.load());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void openFinishScreen() {
    mainAnchorPane.getChildren().clear();
    try {
      FXMLLoader loader = new FXMLLoader(
          (MainController.class.getResource("/view/Game3Finish.fxml")));
      Game3FinishController c = new Game3FinishController();
      loader.setController(c);
      c.setMainController(this);
      mainAnchorPane.getChildren().add(loader.load());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


}
