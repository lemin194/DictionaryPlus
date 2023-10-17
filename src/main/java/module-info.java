module com.dictionary.dictionaryplus {
  requires javafx.controls;
  requires javafx.fxml;
  requires jlayer;
  requires org.json;
  requires java.sql;
  requires org.jsoup;
  requires java.net.http;
  requires com.fasterxml.jackson.databind;
  requires java.desktop;
  requires com.jfoenix;
  requires org.apache.httpcomponents.httpcore;
  requires org.apache.httpcomponents.httpclient;
  requires org.apache.httpcomponents.httpmime;

  exports dictionary;
  exports dictionary.controllers;

  opens dictionary to javafx.fxml;
  opens dictionary.controllers to javafx.fxml;

}