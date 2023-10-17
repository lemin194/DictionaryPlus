package dictionary.controllers;

import com.jfoenix.controls.JFXButton;
import dictionary.services.SpeechToText;
import dictionary.services.TextToSpeech;
import dictionary.services.Translation;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class TranslateController implements Initializable {

  @FXML
  private AnchorPane translateFromBox = new AnchorPane(), translateToBox = new AnchorPane();
  @FXML
  private ChoiceBox<String> fromLanguageChoice = new ChoiceBox<>(),
      toLanguageChoice = new ChoiceBox<>();

  @FXML
  private TextArea fromTextArea = new TextArea(), toTextArea = new TextArea();
  private String fromLanguage = "en";

  private HashMap<String, String> mapAbbr = new HashMap<>();
  private HashMap<String, String> mapLang = new HashMap<>();
  private boolean recording = false;
  @FXML
  private ImageView recordingButtonIcon = new ImageView();
  private Image recordBtnImage, stopBtnImage;
  private PauseTransition recordingDuration;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    File recordImageFile = new File("src/main/resources/utils/icons/translate/micro.png");
    File stopImageFile = new File("src/main/resources/utils/icons/translate/attach.png");
    recordBtnImage = new Image(recordImageFile.toURI().toString());
    stopBtnImage = new Image(stopImageFile.toURI().toString());
    recordingButtonIcon.setImage(recordBtnImage);

    fromLanguageChoice.getItems().addAll("English", "Vietnamese", "French", "German", "Russian");
    toLanguageChoice.getItems().addAll("English", "Vietnamese", "French", "German", "Russian");

    fromLanguageChoice.setValue("English");
    toLanguageChoice.setValue("Vietnamese");

    mapAbbr.put("en", "English");
    mapAbbr.put("vi", "Vietnamese");
    mapAbbr.put("fr", "French");
    mapAbbr.put("de", "German");
    mapAbbr.put("ru", "Russian");

    mapLang.put("English", "en");
    mapLang.put("Vietnamese", "vi");
    mapLang.put("French", "fr");
    mapLang.put("German", "de");
    mapLang.put("Russian", "ru");

    PauseTransition inputWait = new PauseTransition(Duration.seconds(0.5f));
    recordingDuration = new PauseTransition(Duration.seconds(12.0f));
    recordingDuration.setOnFinished(actionEvent -> StopRecord());
    fromTextArea.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
//        if (t1.equals("abc")) {
//          fromLanguageChoice.setValue("testing");
//        } else {
//          System.out.println(fromLanguageChoice.getValue());
//        }
        inputWait.stop();
        inputWait.setOnFinished(finishEvent -> {
          handleTranslate();
        });
        // Start the pause transition
        inputWait.play();
      }
    });

    fromLanguageChoice.getSelectionModel().selectedIndexProperty().addListener(
        (observableValue, number, t1) -> {
          if ((Integer) t1 < 0 || (Integer) t1 > fromLanguageChoice.getItems().size()) {

          } else {
            fromLanguage = mapLang.get(fromLanguageChoice.getItems().get((Integer) t1));
            handleTranslate();
          }
        });

    toLanguageChoice.getSelectionModel().selectedIndexProperty().addListener(
        (observableValue, number, t1) -> {
          if ((Integer) t1 < 0 || (Integer) t1 > toLanguageChoice.getItems().size()) {

          } else {
            handleTranslate();
          }
        });
  }

  private void handleTranslate() {
    System.out.println("Translating...");
    String t1 = fromTextArea.getText();

    String fromLanguage = mapLang.get(fromLanguageChoice.getValue());
    String toLanguage = mapLang.get(toLanguageChoice.getValue());
    List<String> ret = Translation.TranslateText(t1, fromLanguage, toLanguage);

    String translated = ret.get(0);

    toTextArea.setText(translated);
  }

  @FXML
  private void PlayTextFrom() {
    TextToSpeech.TTS(fromTextArea.getText(), mapLang.get(fromLanguageChoice.getValue()));
  }

  @FXML
  private void PlayTextTo() {
    TextToSpeech.TTS(toTextArea.getText(), mapLang.get(toLanguageChoice.getValue()));
  }

  @FXML
  private void ToggleRecord() {
    if (!recording) {
      recording = true;
      SpeechToText.beginRecord();
      recordingButtonIcon.setImage(stopBtnImage);
      recordingDuration.play();
      return;
    }
    StopRecord();
  }

  private void StopRecord() {
    if (!recording) return;
    recording = false;
    SpeechToText.stopRecording();
    recordingButtonIcon.setImage(recordBtnImage);
    System.out.println(fromLanguage);
    var res = SpeechToText.STT(fromLanguage);
    System.out.println(res.get("status") + res.get("stderr"));
    if (res.get("status").equals("OK")) {
      fromTextArea.setText(res.get("content"));
    }
  }
}
