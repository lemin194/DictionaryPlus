package Dictionary.Services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;

public class Translation {
    /**
     * Translate text.
     * @param text              Text to translate.
     * @param sourceLanguage    Source language (en or vi).
     * @param targetLanguage    Target language (en or vi).
     * @return Translated text.
     */
    public static String TranslateText(String text, String sourceLanguage, String targetLanguage) {
        String ret = "";
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
        System.out.println(encodedText);
        String apiUrl  = "https://translate.googleapis.com/translate_a/single?";
        String params = "client=gtx&sl=" + sourceLanguage + "&tl=" + targetLanguage +
                "&dt=t&text=" +
                encodedText +
                "&op=translate";
        System.out.println(params);
        int status = 1000;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(apiUrl + params);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setRequestProperty("Content-Type", "application/json; utf-8");

            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            connection.setDoOutput(true);


            status = connection.getResponseCode();

            Reader streamReader = null;

            if (status > 299) {
                return ret;
            }

            InputStream inputStream = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            JSONArray respond = new JSONArray(content.toString());
            JSONArray sentences = (JSONArray) respond.get(0);
            for (Object sentenceObject : sentences) {
                JSONArray sentence = new JSONArray(sentenceObject.toString());
                String translatedSentence = sentence.get(0).toString();
                ret += translatedSentence;
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
