package dictionary.controllers;

import dictionary.services.Autocorrect;
import dictionary.services.SpeechService;
import dictionary.services.TextToSpeech;
import dictionary.services.Translation;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class TranslateTextController implements Initializable {

  @FXML
  private ChoiceBox<String> fromLanguageChoice = new ChoiceBox<>(),
      toLanguageChoice = new ChoiceBox<>();

  @FXML
  private TextArea fromTextArea = new TextArea(), toTextArea = new TextArea();
  private String fromLanguage = "en", toLanguage = "vi";

  private final HashMap<String, String> mapAbbr = new HashMap<>(),
      mapLang = new HashMap<>();
  private boolean recording = false;
  @FXML
  private ImageView recordingButtonIcon = new ImageView();
  private Image recordBtnImage, stopBtnImage;
  private PauseTransition recordingDuration;
  private PauseTransition translateDelay;
  private PauseTransition suggestionDelay;

  private Thread sttThread;


  private void initLanguageMap() {
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
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    fromTextArea.setWrapText(true);

    initLanguageMap();
    File recordImageFile = new File("src/main/resources/utils/icons/translate/micro.png");
    File stopImageFile = new File("src/main/resources/utils/icons/translate/stop.png");
    recordBtnImage = new Image(recordImageFile.toURI().toString());
    stopBtnImage = new Image(stopImageFile.toURI().toString());
    recordingButtonIcon.setImage(recordBtnImage);

    fromLanguageChoice.getItems().addAll("English", "Vietnamese", "French", "German", "Russian");
    toLanguageChoice.getItems().addAll("English", "Vietnamese", "French", "German", "Russian");

    fromLanguage = "en";
    toLanguage = "vi";

    fromLanguageChoice.setValue(mapAbbr.get(fromLanguage));
    toLanguageChoice.setValue(mapAbbr.get(toLanguage));

    recordingDuration = new PauseTransition(Duration.seconds(12.0f));
    recordingDuration.setOnFinished(actionEvent -> StopRecord());
    translateDelay = new PauseTransition(Duration.seconds(0.5f));
    translateDelay.setOnFinished(actionEvent -> handleTranslate());
    suggestionDelay = new PauseTransition(Duration.seconds(0.5f));
    suggestionDelay.setOnFinished(actionEvent -> handleSuggestion());
    fromTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
      waitTranslate();

      waitSuggest();
    });


    sttThread = new Thread(this::sttThreadFunc);


    fromLanguageChoice.getSelectionModel().selectedIndexProperty().addListener(
        (observableValue, number, t1) -> {
          if ((Integer) t1 < 0 || (Integer) t1 > fromLanguageChoice.getItems().size()) {

          } else {
            fromLanguage = mapLang.get(fromLanguageChoice.getItems().get((Integer) t1));
            if (fromLanguage.equals(toLanguage)) {
              if (fromLanguage.equals("vi")) {
                toLanguage = "en";
              } else {
                toLanguage = "vi";
              }
              toLanguageChoice.setValue(mapAbbr.get(toLanguage));
            }
            waitTranslate();
          }
        });

    toLanguageChoice.getSelectionModel().selectedIndexProperty().addListener(
        (observableValue, number, t1) -> {
          if ((Integer) t1 < 0 || (Integer) t1 > toLanguageChoice.getItems().size()) {

          } else {
            toLanguage = mapLang.get(toLanguageChoice.getItems().get((Integer) t1));
            if (fromLanguage.equals(toLanguage)) {
              if (toLanguage.equals("vi")) {
                fromLanguage = "en";
              } else {
                fromLanguage = "vi";
              }
              fromLanguageChoice.setValue(mapAbbr.get(fromLanguage));
            }
            waitTranslate();
          }
        });
  }

  private void sttThreadFunc() {
    var res = SpeechService.STT(fromLanguage);
    Platform.runLater(() -> {
      System.out.println(res.get("status") + res.get("stderr"));
      if (res.get("status").equals("OK")) {
        fromTextArea.setText(res.get("content"));
      }
    });
  }

  private void waitTranslate() {
    translateDelay.stop();
    translateDelay.play();
  }

  private void waitSuggest() {
    suggestionDelay.stop();
    suggestionDelay.play();
  }

  private void handleTranslate() {
    System.out.println("Translating...");
    String t1 = fromTextArea.getText();

    List<String> ret = Translation.TranslateText(t1, fromLanguage, toLanguage);

    String translated = ret.get(0);

    toTextArea.setText(translated);
  }

  private void handleSuggestion() {
    if (!Arrays.asList("en", "vi").contains(fromLanguage)) return;
    String suggestion = Autocorrect.correct(fromTextArea.getText(), fromLanguage);
    if (suggestion.equals(fromTextArea.getText())) return;
    System.out.println("suggesting:\n" + suggestion);
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
      SpeechService.beginRecord();
      recordingButtonIcon.setImage(stopBtnImage);
      recordingDuration.play();
      return;
    }
    StopRecord();
  }

  private void StopRecord() {
    if (!recording) {
      return;
    }
    recording = false;
    SpeechService.stopRecording();
    recordingButtonIcon.setImage(recordBtnImage);

    if (sttThread != null) {
      sttThread.interrupt();
    }
    sttThread = new Thread(this::sttThreadFunc);
    sttThread.start();
  }

  @FXML
  private void ReverseLanguage() {
    String l1 = fromLanguageChoice.getValue();
    String l2 = toLanguageChoice.getValue();
    String t1 = fromTextArea.getText();
    String t2 = toTextArea.getText();
    fromTextArea.setText(t2);
    toTextArea.setText(t1);
    fromLanguageChoice.setValue(l2);
    toLanguageChoice.setValue(l1);
  }

}
