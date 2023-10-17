package dictionary;

import dictionary.services.WordLookUpService;
import dictionary.views.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class MainApplication extends Application {
    public static Stage mainStage;
    public static void main(String[]args) {
        System.out.println(new File("./src/main/java/Dictionary/Main.fxml").exists());
        launch(args);
    }
//    private static void getAllFiles(File curDir) {
//
//        File[] filesList = curDir.listFiles();
//        for (File f : filesList) {
//            if (f.isDirectory())
//                getAllFiles(f);
//            if (f.isFile()) {
//                System.out.println(f.getPath());
//            }
//        }
//    }
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(HelloApplication.class);
        try {
            WordLookUpService.start();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Main.fxml")));
            Scene scene = new Scene(root, Color.web("1F1F1F"));

            scene.getStylesheets().add(getClass().getResource("/style/main.css").toExternalForm());

            stage.setTitle("Dictionary Plus");
            stage.setScene(scene);
            WordLookUpService.close();

            stage.show();
            mainStage = stage;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
