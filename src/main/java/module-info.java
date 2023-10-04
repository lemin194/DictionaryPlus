module com.dictionary.dictionaryplus {
    requires javafx.controls;
    requires javafx.fxml;
    requires jlayer;
    requires org.json;
    requires java.sql;
    requires org.jsoup;


    exports dictionary;
    exports dictionary.controllers;

    opens dictionary to javafx.fxml;
    opens dictionary.controllers to javafx.fxml;

}