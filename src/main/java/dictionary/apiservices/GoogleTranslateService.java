package dictionary.apiservices;

import dictionary.apis.ExternalAPIs;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;


public class GoogleTranslateService {

  /**
   * Translate text.
   *
   * @param text           Text to translate.
   * @param sourceLanguage Source language (en or vi).
   * @param targetLanguage Target language (en or vi).
   * @return Translated text.
   */


  public static List<String> TranslateText(String text, String sourceLanguage,
      String targetLanguage) {
    List<String> ret = new ArrayList<>();
    ret.add("");
    ret.add("en");

    Pattern spacePattern = Pattern.compile("^[\\s]+$");
    Matcher matcher = spacePattern.matcher(text);
    if (matcher.matches()) {
      // Text contains only white spaces.
      return ret;
    }

    String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
    System.out.println(encodedText);
    String requestUrl = "https://translate.googleapis.com/translate_a/single";
//        String params = "client=gtx&sl=" + sourceLanguage + "&tl=" + targetLanguage +
//                "&dt=t&text=" +
//                encodedText +
//                "&op=translate";
//        System.out.println(params);
    HashMap<String, String> params = new HashMap<>();
    params.put("client", "gtx");
    params.put("sl", sourceLanguage);
    params.put("tl", targetLanguage);
    params.put("dt", "t");
    params.put("text", encodedText);
    params.put("op", "translate");

    int status = 1000;
    HttpURLConnection connection = null;
    try {
      ExternalAPIs req = new ExternalAPIs(2000, 2000);
      HashMap<String, Object> response = req.requestFormBody(requestUrl,
          "GET", params, null, null);
      if (!response.get("status").equals("OK")) {
        throw new Error(response.get("stderr").toString());
      }
//      System.out.println("Content:" + response.get("content"));
      JSONArray respond = new JSONArray(response.get("content").toString());
      if (respond.get(0).toString().equals("null")) {
          return ret;
      }
      JSONArray sentences = (JSONArray) respond.get(0);
      String lang = respond.get(2).toString();
      ret.set(1, lang);
      for (Object sentenceObject : sentences) {
          JSONArray sentence = new JSONArray(sentenceObject.toString());
          String translatedSentence = sentence.get(0).toString();
          ret.set(0, ret.get(0) + translatedSentence);
      }

    } catch (Exception e) {
      System.out.println("Exception: " + e.toString());
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }

    return ret;
  }
}
