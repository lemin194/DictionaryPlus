package dictionary.apis;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import org.json.JSONObject;

public class FlaskAPIs implements APIs{

  protected int connectTimeOut = 20000;
  protected int readTimeOut = 30000;

  public FlaskAPIs() {
    super();
  }

  public FlaskAPIs(int connectTimeOut, int readTimeOut) {
    super();
    setConnectTimeOut(connectTimeOut);
    setReadTimeOut(readTimeOut);
  }

  @Override
  public HashMap<String, Object> request(String url) {
    throw new UnsupportedOperationException();
    
  }

  @Override
  public HashMap<String, Object> requestJsonBody(String requestUrl, String requestMethod,
      HashMap<String, String> header, HashMap<String, String> body) {

    HttpURLConnection connection = null;
    HashMap<String, Object> ret = new HashMap<>();
    ret.put("status", "");
    ret.put("stderr", "Unhandled exception!");
    ret.put("content", "");
    try {
      URL _url = new URL(requestUrl);
      connection = (HttpURLConnection) _url.openConnection();
      connection.setRequestMethod(requestMethod);

      connection.setRequestProperty("Content-Type", "application/json; utf-8");
      if (header != null) {
        for (String key : header.keySet()) {
          connection.setRequestProperty(key, header.get(key));
        }
      }

      connection.setConnectTimeout(connectTimeOut);
      connection.setReadTimeout(readTimeOut);
      connection.setDoOutput(true);

      JSONObject jsonBody = new JSONObject(body);

      try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
        out.write(jsonBody.toString()
            .getBytes("UTF-8"));
      }

      int status = connection.getResponseCode();
      if (status > 299) {
        throw new Error("Error code: " + status);
      }

      ret.put("status", "OK");
      InputStream inputStream = connection.getInputStream();
      BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
      String inputLine;
      StringBuilder content = new StringBuilder();
      while ((inputLine = in.readLine()) != null) {
        content.append(inputLine);
      }
      in.close();
      ret.put("stderr", "");
      ret.put("content", content.toString());
    } catch (Exception e) {
      ret.put("status", "Exception");
      ret.put("stderr", e.toString());
      ret.put("content", "");
    } catch (Error e) {
      ret.put("status", "Error");
      ret.put("stderr", e.toString());
      ret.put("content", "");
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
    return ret;
  }

  @Override
  public HashMap<String, Object> requestFormBody(String requestUrl, String requestMethod,
      HashMap<String, String> params, HashMap<String, String> header, HashMap<String, String> body) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public HashMap<String, Object> requestFormBody(String requestUrl, String requestMethod,
      HashMap<String, String> params, HashMap<String, String> header, HashMap<String, String> body, File file) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public HashMap<String, Object> requestBinaryBody(String requestUrl,
      HashMap<String, String> header, File file) throws IOException {
    throw new UnsupportedOperationException();
  }

  public int getConnectTimeOut() {
    return connectTimeOut;
  }

  public void setConnectTimeOut(int connectTimeOut) {
    if (connectTimeOut <= 0) {
      throw new IllegalArgumentException("Time out can't be negative.");
    }
    this.connectTimeOut = connectTimeOut;
  }

  public int getReadTimeOut() {
    return readTimeOut;
  }

  public void setReadTimeOut(int readTimeOut) {
    if (readTimeOut <= 0) {
      throw new IllegalArgumentException("Time out can't be negative.");
    }
    this.readTimeOut = readTimeOut;
  }
}
