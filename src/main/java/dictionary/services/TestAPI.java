package dictionary.services;

import java.util.List;
import org.json.JSONObject;

public class TestAPI {
    public static void TestTTS() {
        String status = TextToSpeech.TTS("Ich bin Oppenheimer", "de");
        if (!status.equals("OK")) {
            System.out.println("TTS Failed: " + status);
        }

        System.out.println("abc");
    }

    public static void TestTranslate() {
        System.out.println("Begin connecting");
        List<String> ret = Translation.TranslateText(
                "Ich bin ein Idiot.",
                "auto", "fr");
        String detectedLang = ret.get(1);
        String translated = ret.get(0);
        System.out.println("Translated: \n" + translated);
        System.out.println("Detected: \n" + detectedLang);
    }

    public static void main(String[] args) {
        System.out.println("Hello");
        TestTranslate();
        System.out.println(Autocorrect.correct("Đứcc rấttt đẹpp trai", "vi"));
//        TestTTS();
//        SpeechToText.beginRecord();
//        SpeechToText.stopRecording();
//        System.out.println(SpeechToText.STT("ru"));
    }
}
