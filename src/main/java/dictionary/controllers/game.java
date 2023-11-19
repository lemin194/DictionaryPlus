package dictionary.controllers;

import dictionary.models.Entity.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public abstract class game implements Initializable {
    /* please follow this template and refractor attributes, methods in game3 if possible.
     the scene of game3 if possible please have the elements below; if you don't need them it's still fine
    */

    /* this will contain all collections that users added. Once the user choose a collection in this choice-box;
    the setCollection set function will set the currentCollection = user's choice. Please add that function
    to this choice-box element in the fxml file (onAction = "#setCollection").*/
    @FXML
    ChoiceBox<String> collectionsToPlay = new ChoiceBox<>();

    /* in game3 it is lblScore */
    @FXML
    protected Label lblScore;

    /* in game3 it is finalScore */
    protected float finalScore;

    /*To show how many words user has reviewed in this collection for examples 2/4 words with 4 words
    is the number of word in collection.*/
    @FXML
    public Label progress;

    /*restart playing the collection */
    @FXML
    public Button btnRestart;

    public String currentCollectionName;

    /*tho we only care about the word.getword(); it will be list<word> de
    thao tac de hon voi wordCollectionDao
    . */
    public List<Word> currentWordList;

    /* you may not need to use this if unnecessary. First will load all string word.getword() from all words in the
    chosen collection into List<String> currentWordCollection
    from the choicebox; after the user click btnNext; idOfDisplayWord++ to move to the next word;
     */
    public int idOfDisplayWord;

    /* cung de thao tac de hon voi wordcollectiondao. m chi can .getWord() la duoc :)) hoac k dung cai nay cx duoc*/
    public Word currentWord;
    @FXML
    public abstract void initialize(URL location, ResourceBundle Resources);
}
