package dictionary.controllers;

import com.jfoenix.controls.JFXButton;
import dictionary.apiservices.TranslateImageService;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;

public class TranslateImageController implements Initializable {

  private final HashMap<String, String> mapAbbr = new HashMap<>(), mapLang = new HashMap<>();
  @FXML
  private AnchorPane dragTarget = new AnchorPane();
  @FXML
  private JFXButton btnBrowseFile = new JFXButton();
  @FXML
  private JFXButton btnClipboard = new JFXButton();

  private Stage imageViewWindow = null;
  File imgFile = null;
  Image img = null;

  private double xOffset = 0, yOffset = 0;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {


    dragTarget.setOnDragOver(event -> {
      dragTarget.getStyleClass().remove("drag-box");
      dragTarget.getStyleClass().remove("drag-box-dragged");
      dragTarget.getStyleClass().add("drag-box-dragged");
      if (event.getGestureSource() != dragTarget && event.getDragboard().hasFiles()) {
        /* allow for both copying and moving, whatever user chooses */
        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
      }
      event.consume();
    });

    dragTarget.setOnDragExited(event -> {
      dragTarget.getStyleClass().remove("drag-box");
      dragTarget.getStyleClass().remove("drag-box-dragged");
      dragTarget.getStyleClass().add("drag-box");
    });

    dragTarget.setOnDragDropped(event -> {
      Dragboard db = event.getDragboard();
      boolean success = false;
      if (db.hasFiles()) {
        var files = db.getFiles();
        if (files.size() == 1) {
          imgFile = files.get(0);
          System.out.println(imgFile.getPath());
          try {
            img = new Image("file://" + imgFile.getPath());
          } catch (Exception e) {
            e.printStackTrace();
            imgFile = null;
            img = null;
          }
        }
        success = true;
      }

      if (img != null) {
        translateImage(img);
        img = null;
      }
      event.setDropCompleted(success);
      event.consume();
      System.out.println(imageViewWindow);
    });

    btnBrowseFile.setOnAction(actionEvent -> {

      FileChooser fileChooser = new FileChooser();

      //Set extension filter for text files
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files",
          "*.png", "*.jpg");
      fileChooser.getExtensionFilters().add(extFilter);

      //Show save file dialog
      File file = fileChooser.showOpenDialog(btnBrowseFile.getScene().getWindow());

      if (file != null) {
        Image image = new Image("file://" + file.getPath());
        translateImage(image);
      }
    });

    btnClipboard.setOnAction(actionEvent -> {
        img = null;
        Clipboard clipboard = Clipboard.getSystemClipboard();

        if (clipboard.hasImage()) {
          img = clipboard.getImage();
        }

        if (img != null) {
          translateImage(img);
        }
    });
  }

  private void translateImage(Image img) {
    Thread imageViewThread = new Thread(() -> {
      Image translated = TranslateImageService.translate(img);
      Platform.runLater(() -> {
        createImageViewWindow(translated);
      });
    });
    imageViewThread.start();
  }

  private void createImageViewWindow(Image img) {
    closeImageViewWindow();
    try {
      FXMLLoader loader = new FXMLLoader(
          Objects.requireNonNull(getClass().getResource("/view/TranslateImageView.fxml")));
      Parent root = loader.load();
      ImageView imageView = (ImageView) loader.getNamespace().get("imageView");
      imageView.setImage(img);
      imageView.setPreserveRatio(true); // Preserve the aspect ratio
      imageView.setFitWidth(800); // Set the desired width
      imageView.setFitHeight(600); // Set the desired height

      JFXButton btnClose = (JFXButton) loader.getNamespace().get("btnCloseWindow");
      JFXButton btnTopbarClose = (JFXButton) loader.getNamespace().get("btnClose");
      JFXButton btnMinimize = (JFXButton) loader.getNamespace().get("btnMinimizeWindow");
      JFXButton btnSaveAs = (JFXButton) loader.getNamespace().get("btnSaveAs");

      Pane titlebar = (Pane) loader.getNamespace().get("titlebar");
      titlebar.setOnMousePressed(event -> {
        xOffset = imageViewWindow.getX() - event.getScreenX();
        yOffset = imageViewWindow.getY() - event.getScreenY();
      });

      titlebar.setOnMouseDragged(event -> {
        imageViewWindow.setX(event.getScreenX() + xOffset);
        imageViewWindow.setY(event.getScreenY() + yOffset);
      });

      btnClose.setOnAction(actionEvent -> {
        closeImageViewWindow();
      });

      btnTopbarClose.setOnAction(actionEvent -> {
        closeImageViewWindow();
      });

      btnMinimize.setOnAction(actionEvent -> {
        minimizeImageViewWindow();
      });

      btnSaveAs.setOnAction(actionEvent -> {

        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files",
            "*.png", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(imageViewWindow);

        if (file != null) {
          String fileName = file.getPath();
          System.out.println("Saving: " + fileName);
          String formatName = "";
          if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
          }
          if (formatName.isEmpty() || !(formatName.equals("jpg") || formatName.equals("png"))) {
            formatName = "png";
            fileName += ".png";
          }

          try {
            saveImage(img, fileName, formatName);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      });

      Scene imageViewScene = new Scene(root, Color.TRANSPARENT);
      imageViewWindow = new Stage(StageStyle.TRANSPARENT);
      imageViewWindow.setResizable(false);
      imageViewWindow.setScene(imageViewScene);
      imageViewWindow.initOwner(null);
      imageViewWindow.initModality(Modality.WINDOW_MODAL);
      imageViewWindow.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void saveImage(Image image, String fileName, String formatName) throws IOException {

    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
    File file = new File(fileName);
    ImageIO.write(bufferedImage, formatName, file);
  }

  @FXML
  private void closeImageViewWindow() {
    if (imageViewWindow != null) {
      imageViewWindow.close();
    }
    imageViewWindow = null;
  }

  @FXML
  private void minimizeImageViewWindow() {
    if (imageViewWindow != null) {
      imageViewWindow.setIconified(true); // Minimize the child stage
    }
  }
}
