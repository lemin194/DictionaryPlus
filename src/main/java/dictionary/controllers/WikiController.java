package dictionary.controllers;

import dictionary.services.WikFetchService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class WikiController {
    @FXML
    private TextField searchBox = new TextField();
    @FXML
    private TextArea renderedText = new TextArea();
    @FXML
    private Button search;
    @FXML
    public void handleSearch() {
        String word = searchBox.getText();
        if (word.isBlank() || word.isEmpty()) renderedText.setText("");
        renderedText.setWrapText(true);
        WikFetchService wfService = new WikFetchService();
        renderedText.setText(wfService.search(word));
    }
}
