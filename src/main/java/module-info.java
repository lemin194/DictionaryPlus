module com.dictionary.dictionaryplus {
    requires javafx.controls;
    requires javafx.fxml;
    requires jlayer;


    opens com.dictionary.dictionaryplus to javafx.fxml;
    exports com.dictionary.dictionaryplus;
}