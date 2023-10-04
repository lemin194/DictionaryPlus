package dictionary.services;

import dictionary.models.Entity.Word;
import dictionary.models.Entity.WordCollection;

import java.util.ArrayList;
import java.util.List;

enum State {
    EASY,
    MEDIUM,
    HARD,
}
class Card {
    private Word word;
    private State currentState;

    public Card(Word word) {
        this.word = word;
        this.currentState = State.HARD;
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public State getCurrentState() {
        return this.currentState;
    }
}
public class CardService {
    private List<Card> cardList;
    public CardService (WordCollection wordCollection) {
        cardList = new ArrayList<>();
        for (Word word : wordCollection.getWordList()) {
            cardList.add(new Card(word));
        }
    }

    // Too easy, no need to revise
    public void checkCard() {
        cardList.removeIf(card -> card.getCurrentState() == State.EASY);
    }
}
