package dictionary.models.Entity;

public class Word {
    private String word;
    private String pronunciation;
    private String type;
    private String meaning;

    /**
     *
     * @param word
     * @param pronunciation
     * @param type
     * @param meaning
     */
    public Word(String word, String pronunciation, String type, String meaning) {
        this.word = word;
        this.pronunciation = pronunciation;
        this.type = type;
        this.meaning = meaning;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getType() {
        return type;
    }

    public void setExplanation(String type) {
        this.type = type;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void modify(String meaning) {
        this.meaning += meaning;
    }

    @Override
    public String toString() {
        return word + " " + pronunciation + " " + type + " " + meaning;
    }
}
