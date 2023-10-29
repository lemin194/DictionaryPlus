package dictionary.services;

import org.json.JSONObject;

public class Autocorrect {

  public static String correct(String text, String lang) {
    if (!(lang.equals("en") || lang.equals("vi"))) {
      System.out.println(String.format("Language \"%s\" not supported!", lang));
      return "";
    }
    JSONObject body = new JSONObject();
    body.put("text", text);
    body.put("sl", lang);
    JSONObject res = BackendUtils.request("http://127.0.0.1:9876/autocorrect/", "POST", body);
    if ((int)res.get("status") == 200) {
      return new JSONObject(res.get("content").toString()).get("content").toString();
    }
    return "";
  }
}
