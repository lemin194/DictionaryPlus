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
        String ret = Translation.TranslateText(
                "",
                "en", "vi");
        System.out.println("Translated: \n" + ret);
    }
    public static void main(String[] args) {
        System.out.println("Hello");
        TestTranslate();
        TestTTS();
    }
}
