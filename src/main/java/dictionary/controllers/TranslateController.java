package dictionary.controllers;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class TranslateController implements Initializable {
  private String mode = "Text";
  @FXML
  private AnchorPane translateComponent = new AnchorPane();
  @FXML
  private JFXButton buttonImageMode = new JFXButton(), buttonTextMode = new JFXButton();


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    setMode("Text");

    buttonTextMode.setOnAction(actionEvent -> {
      setMode("Text");
    });
    buttonImageMode.setOnAction(actionEvent -> {
      setMode("Image");
    });

  }
  private void setMode(String mode) {
    try {
      if (mode.equals("Text")) {
        buttonTextMode.getStyleClass().remove("button-translate-service");
        buttonTextMode.getStyleClass().remove("button-translate-service-selected");
        buttonImageMode.getStyleClass().remove("button-translate-service");
        buttonImageMode.getStyleClass().remove("button-translate-service-selected");
        buttonTextMode.getStyleClass().add("button-translate-service-selected");
        buttonImageMode.getStyleClass().add("button-translate-service");
        translateComponent.getChildren().clear();
        translateComponent.getChildren().add(FXMLLoader.load(
            Objects.requireNonNull(MainController.class.getResource("/view/TranslateText.fxml"))));
      } else if (mode.equals("Image")) {
        buttonTextMode.getStyleClass().remove("button-translate-service");
        buttonTextMode.getStyleClass().remove("button-translate-service-selected");
        buttonImageMode.getStyleClass().remove("button-translate-service");
        buttonImageMode.getStyleClass().remove("button-translate-service-selected");
        buttonTextMode.getStyleClass().add("button-translate-service");
        buttonImageMode.getStyleClass().add("button-translate-service-selected");
        translateComponent.getChildren().clear();
        translateComponent.getChildren().add(FXMLLoader.load(
            Objects.requireNonNull(MainController.class.getResource(
                "/view/TranslateImageSelect.fxml"))));
      }
      this.mode = mode;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
