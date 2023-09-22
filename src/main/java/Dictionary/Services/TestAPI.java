package Dictionary.Services;

public class TestAPI {
    private static void testTTS() {
        String status = TextToSpeech.TTS("Testing, attention please.", "en");
        if (!status.equals("OK")) {
            System.out.println("TTS Failed: " + status);
        }

        System.out.println("abc");
    }
    public static void main(String[] args) {
        System.out.println("Hello");
        testTTS();
    }
}
