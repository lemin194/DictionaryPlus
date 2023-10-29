package dictionary.controllers;

import dictionary.services.WikFetchService;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.*;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent.*;
public class WikiController implements Initializable {
    @FXML
    private TextField searchBox = new TextField();
    @FXML
    private TextArea renderedText = new TextArea();
    @FXML
    private Button search;
    @FXML
    private WebView webView;
    private WebHistory history;
    @FXML
    private ChoiceBox<String> historyURL = new ChoiceBox<>();
    WebEngine engine = null;
    @FXML
    private Button load = new Button();
    @FXML
    private Button refresh = new Button();
    @FXML
    private Button zoomIn = new Button();
    @FXML
    private Button zoomOut = new Button();
    @FXML
    private Button historyBtn = new Button();
    @FXML
    private Button back = new Button();
    @FXML
    private Button forward = new Button();
    private double webZoom;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        engine = webView.getEngine();
        webZoom = 1;
    }

    @FXML
    public void loadPage(ActionEvent event) {
        //engine.load("https://en.wikipedia.org/wiki/" + searchBox.getText());
        engine.load("https://www.pinterest.com/search/pins/?q="+searchBox.getText()+"&rs=typed");

    }
    @FXML
    public void handleSearch() {
        String word = searchBox.getText();
        if (word.isBlank() || word.isEmpty()) renderedText.setText("");
        renderedText.setWrapText(true);
        WikFetchService wfService = new WikFetchService();
        renderedText.setText(wfService.search(word));
    }
    @FXML
    public void load() {
        engine.load("https://en.wikipedia.org/wiki/" + searchBox.getText());
    }

    @FXML
    public void refreshPage() {
        engine.reload();
    }
    @FXML
    public void zoomIn() {
        webZoom+=0.25;
        webView.setZoom(webZoom);
    }
    @FXML
    public void zoomOut() {
        webZoom-=0.75;
        webView.setZoom(webZoom);
    }

    @FXML
    public void displayHistory() {
        history = engine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        for(WebHistory.Entry entry : entries) {
            System.out.println(entry.getUrl().getClass());
        }
    }
    @FXML
    public void goBack() {
        history = engine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        history.go(-1);
    }
    @FXML
    public void goForward() {
        history = engine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        history.go(1);
    }
}
