package dictionary.controllers;

import com.jfoenix.controls.JFXButton;
import dictionary.services.AnimatedGif;
import dictionary.services.Animation;
import dictionary.apiservices.SpeechService;
import dictionary.apiservices.TTSService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;


public class Game3Controller implements Initializable {

  @FXML
  JFXButton btnNext = new JFXButton(), btnPlay = new JFXButton(), btnRecord = new JFXButton();

  @FXML
  TextFlow textFlow = new TextFlow();

  private List<String> sentences = new ArrayList<>();
  private List<Integer> order = new ArrayList<>();
  private int currId = -1;
  private boolean recording = false;
  @FXML
  private ImageView iconRecord = new ImageView();
  private Image imageRecord, imageStop, imageWait;
  private PauseTransition recordingDuration;

  private Thread sttThread;
  private float finalScore = -1;

  @FXML
  private Label lblScore = new Label();
  @FXML
  private VBox gifContainer = new VBox();


  private String sentence = "During the holiday season, my family and"
      + " I love to decorate the house with colorful lights and ornaments.";
  private final String sentencesFileName = "src/main/resources/data/game3_sentences.txt";

  @FXML
  public void initialize(URL location, ResourceBundle Resources) {
    File recordImageFile = new File("src/main/resources/utils/icons/translate/micro.png");
    File stopImageFile = new File("src/main/resources/utils/icons/translate/stop.png");
    File waitImageFile = new File("src/main/resources/utils/icons/translate/wait.png");
    imageRecord = new Image(recordImageFile.toURI().toString());
    imageStop = new Image(stopImageFile.toURI().toString());
    imageWait = new Image(waitImageFile.toURI().toString());
    iconRecord.setImage(imageRecord);
    recordingDuration = new PauseTransition(Duration.seconds(30.0f));
    recordingDuration.setOnFinished(actionEvent -> StopRecord());


    lblScore.setOpacity(0.0);

    loadSentences();
    nextSentence();

    btnNext.setOnAction(e -> nextSentence());
    btnPlay.setOnAction(e -> TTSService.TTS(sentence, "en"));
    btnRecord.setOnAction(e -> ToggleRecord());
  }

  private void speechAnalysisThread() {
    Object res = null;
    System.out.println("Analysing");
    iconRecord.setImage(imageWait);
    res = SpeechService.SpeechAnalysis(sentence).get("content");
    if (res == null) {
      System.out.println("Error analysing speech!");
    }
    Object finalRes = res;
    Platform.runLater(() -> {
      iconRecord.setImage(imageRecord);
      if (finalRes == null) {
        return;
      }
      scoreSpeech((List<List<String>>) finalRes);
    });
  }

  private void createFireworks() {
    try {
      Animation ani = new AnimatedGif(
          getClass().getResource("/utils/imgs/giphy.gif"), 1000);
      ani.setCycleCount(1);
      ani.play();
      gifContainer.getChildren().clear();
      gifContainer.getChildren().add(ani.getView());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static ArrayList<Integer> generateRandomPermutation(int n) {
    ArrayList<Integer> permutation = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      permutation.add(i);
    }

    Collections.shuffle(permutation, new Random(System.currentTimeMillis()));

    return permutation;
  }

  private void loadSentences() {
    try {
      FileReader fr = new FileReader(sentencesFileName);
      BufferedReader br = new BufferedReader(fr);
      String line;
      while ((line = br.readLine()) != null) {
        sentences.add(line);
      }
      order = generateRandomPermutation(sentences.size());
      System.out.printf("Order: %s\n", order);
    } catch (IOException e) {
      System.out.println("Error loading sentences.");
    }
  }

  private void nextSentence() {
    if (!sentences.isEmpty()) {
      currId = (currId % order.size() + 1 + order.size()) % order.size();
      sentence = sentences.get(order.get(currId));
    }
    try {
      textFlow.getChildren().clear();
      List<String> words = List.of(sentence.split("\\s+"));
      for (String word : words) {
        Label wt = new Label(word);
        textFlow.getChildren().addAll(wt, new Label(" "));
      }
      for (Node c : textFlow.getChildren()) {
        Label t = (Label) c;
        t.setStyle("-fx-text-fill: #111111; -fx-font-size: 18px;");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  @FXML
  private void ToggleRecord() {
    if (!recording) {
      recording = true;
      SpeechService.beginRecord();
      iconRecord.setImage(imageStop);
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
    iconRecord.setImage(imageRecord);

    if (sttThread != null) {
      sttThread.interrupt();
    }
    sttThread = new Thread(this::speechAnalysisThread);
    sttThread.start();
  }

  private boolean isLetter(char c) {
    return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
  }

  private void scoreSpeech(List<List<String>> wordproblist) {
    if (wordproblist == null) {
      return;
    }
    float totalScore = 0;
    int n = 0;
    textFlow.getChildren().clear();
    for (List<String> wordprob : wordproblist) {
      String word = wordprob.get(0);
      float prob = Float.parseFloat(wordprob.get(1));
      Label lbl = new Label(word);
      String style = "-fx-font-size: 18px;";
      if (isLetter(word.charAt(0))) {
        totalScore += prob;
        n++;
        style += String.format("-fx-text-fill: hsb(%f, 93%%, 96%%);", prob * 122.0);

      }
      lbl.setStyle(style);
      textFlow.getChildren().add(lbl);
    }

    finalScore = totalScore / n;

    String style = String.format("-fx-text-fill: hsb(%f, 93%%, 96%%);", finalScore * 122.0);
    lblScore.setStyle(style);
    lblScore.setText(String.format("Score: %.0f%%", finalScore * 100));
    lblScore.setOpacity(1.0);
    int lastingDur = 20;
    PauseTransition p = new PauseTransition(Duration.seconds(lastingDur));
    p.setOnFinished(e->{

      FadeTransition ft = new FadeTransition(Duration.seconds(2), lblScore);
      ft.setFromValue(1.0);
      ft.setToValue(0.0);
      ft.play();
    });
    p.play();
    if (finalScore > 0.8) {
      createFireworks();
    }
  }
}
