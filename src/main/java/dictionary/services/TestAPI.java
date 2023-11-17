package dictionary.services;

import dictionary.models.Entity.Word;
import java.util.List;

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
    List<String> ret = Translation.TranslateText("Ich bin ein Idiot.", "auto", "fr");
    String detectedLang = ret.get(1);
    String translated = ret.get(0);
    System.out.println("Translated: \n" + translated);
    System.out.println("Detected: \n" + detectedLang);
  }

  public static void main(String[] args) {
    System.out.println("Hello");
    TestTranslate();
//    System.out.println(SpeechService.SpeechAnalysis(
//        "Embarking on a winter holiday, my friends and I are planning a ski "
//            + "trip to the mountains to make the most of the snowy weather.").get("content"));
    List<Word> words = WordLookUpService.findWord("kit", "anhviet");
    int id = 12;
    System.out.println(words.get(id).getMeaning());
    System.out.println(StringUtils.getFirstMeaning(words.get(id)));

  }
}
