package dictionary.alerts;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;


public class alertStyler {
    private Alert alert;
    private DialogPane dialogPane;

    public alertStyler(Alert alert) {
        this.alert = alert;
        this.dialogPane = alert.getDialogPane();
    }

    public static alertStyler on(Alert alert) {
        return new alertStyler(alert);
    }

    public alertStyler applyStyle() {
        String style =
                "-fx-background-color: #ffffff; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 10px; ";
        dialogPane.setStyle(style);
        return this;
    }

    public alertStyler setButtonStyle() {
        String buttonStyle =
                "-fx-background-color: #2EC4B6" +
                        "; -fx-text-fill: #ffffff; " +
                        "-fx-font-family: 'Caudex', cursive; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold;"+
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10px; ";
        dialogPane.lookupButton(ButtonType.OK).setStyle(buttonStyle);
        dialogPane.lookupButton(ButtonType.CANCEL).setStyle(buttonStyle);
        dialogPane.lookupButton(ButtonType.CLOSE).setStyle(buttonStyle);
        dialogPane.lookupButton(ButtonType.FINISH).setStyle(buttonStyle);
        return this;
    }
    public alertStyler setTitle(String title) {
        String titleStyle =
                "-fx-font-family: 'Caudex', cursive; " +
                "-fx-font-size: 18px; " +
                "-fx-text-fill: #000000;";

        // Create a new label and apply styling
        Label titleLabel = new Label(title);
        titleLabel.setStyle(titleStyle);

        // Set the title as the header of the dialog pane
        dialogPane.setHeader(titleLabel);

        return this;
    }


    public alertStyler setWindowTitle(String title) {
        alert.setTitle(title);
        return this;
    }



    public alertStyler setMinSize() {
        dialogPane.setMinHeight(Region.USE_PREF_SIZE);
        return this;
    }

    public Alert build() {
        return alert;
    }
    public void applyAllAlertStyle(String title) {

    }
}
