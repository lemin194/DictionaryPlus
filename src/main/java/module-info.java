module com.dictionary.dictionaryplus {
    requires javafx.controls;
    requires javafx.fxml;
    requires jlayer;
    requires org.json;


    opens Dictionary.Views to javafx.fxml;
    exports Dictionary.Views;
}