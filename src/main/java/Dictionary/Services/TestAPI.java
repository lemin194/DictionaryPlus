package Dictionary.Services;

public class TestAPI {
    public static void TestTTS() {
        String status = TextToSpeech.TTS("hello", "en");
        if (!status.equals("OK")) {
            System.out.println("TTS Failed: " + status);
        }

        System.out.println("abc");
    }

    public static void TestTranslate() {
        System.out.println("Begin connecting");
        String ret = Translation.TranslateText("Welcome to the Dictionary App, your go-to tool for translating Vietnamese to English and vice versa. This app comes equipped with various features to help you learn and improve your language skills. Whether you need to look up a word, translate a paragraph, or play vocabulary reviewing games, this app has got you covered.\n" +
                        "Table of Contents\n" +
                        "\n" +
                        "    Features\n" +
                        "    Getting Started\n" +
                        "    Usage\n" +
                        "    Screenshots\n" +
                        "    Feedback\n" +
                        "    Contributing\n" +
                        "    License\n" +
                        "\n" +
                        "Features\n" +
                        "Look Up Words\n" +
                        "\n" +
                        "Easily search for word meanings, definitions, and translations in both Vietnamese and English. The app provides detailed information about the words you're interested in, helping you expand your vocabulary.\n" +
                        "Translate Paragraph\n" +
                        "\n" +
                        "Need to translate a paragraph or a block of text? The app offers a robust translation feature that accurately converts text from Vietnamese to English and vice versa. Say goodbye to language barriers!\n" +
                        "Vocabulary Reviewing Games\n" +
                        "\n" +
                        "Learning a new language is fun with our vocabulary reviewing games. Challenge yourself with interactive quizzes and games designed to reinforce your language skills and improve your retention of new words.",
                "en", "vi");
        System.out.println("Translated: \n" + ret);
    }
    public static void main(String[] args) {
        System.out.println("Hello");
        TestTranslate();
        TestTTS();
    }
}
