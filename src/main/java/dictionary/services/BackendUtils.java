package dictionary.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONObject;

public class BackendUtils {

  private static final int readTimeout = 20000;
  private static final int connectTimeout = 5000;

  public static JSONObject request(String url, String method, JSONObject body) {
    HttpURLConnection connection = null;
    JSONObject ret = new JSONObject();
    ret.put("status", 1000);
    ret.put("content", "");
    try {
      URL _url = new URL(
          url
      );
      connection = (HttpURLConnection) _url.openConnection();
      connection.setRequestMethod(method);

      connection.setRequestProperty("Content-Type", "application/json; utf-8");

      connection.setConnectTimeout(connectTimeout);
      connection.setReadTimeout(readTimeout);
      connection.setDoOutput(true);

      try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
        out.write(body.toString()
            .getBytes("UTF-8"));
      }

      int status = connection.getResponseCode();
      ret.put("status", status);

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
      ret.put("content", content.toString());
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
