package dictionary.alerts;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class dialogStyler {
    private Dialog<?> dialog;
    private DialogPane dialogPane;

    private dialogStyler(Dialog<?> dialog) {
        this.dialog = dialog;
        this.dialogPane = dialog.getDialogPane();
    }

    public static dialogStyler on(Dialog<?> dialog) {
        return new dialogStyler(dialog);
    }

    public dialogStyler applyVintageStyle() {
        String style =
                "-fx-background-color: #ffffff; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10px; ";
        dialogPane.setStyle(style);
        return this;
    }

    public dialogStyler setTitle(String title) {
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

    public dialogStyler setWindowTitle(String title) {
        dialog.setTitle(title);
        return this;
    }

    public dialogStyler setMinSize() {
        dialogPane.setMinHeight(Region.USE_PREF_SIZE);
        return this;
    }

    public Dialog<?> build() {
        return dialog;
    }
}
