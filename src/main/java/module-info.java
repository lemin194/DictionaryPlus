module com.dictionary.dictionaryplus {
    requires javafx.controls;
    requires javafx.fxml;
    requires jlayer;
    requires org.json;
    requires java.sql;
    requires org.jsoup;


    opens dictionary.views to javafx.fxml;
    exports dictionary.views;
    opens dictionary to javafx.fxml;
    exports dictionary;
    exports dictionary.controllers;
    opens dictionary.controllers to javafx.fxml;
}