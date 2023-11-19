package dictionary.apis;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class ExternalAPIs implements APIs {

  protected int connectTimeOut = 20000;
  protected int readTimeOut = 30000;

  public ExternalAPIs() {
    super();
  }

  public ExternalAPIs(int connectTimeOut, int readTimeOut) {
    super();
    setConnectTimeOut(connectTimeOut);
    setReadTimeOut(readTimeOut);
  }


  @Override
  public HashMap<String, Object> request(String url) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public HashMap<String, Object> requestJsonBody(String url, String requestMethod,
      HashMap<String, String> header, HashMap<String, String> body)
      throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public HashMap<String, Object> requestFormBody(String requestUrl, String requestMethod,
      HashMap<String, String> params, HashMap<String, String> header, HashMap<String, String> body)
      throws IOException {
    return requestFormBody(requestUrl, requestMethod, params, header, body, null);
  }

  @Override
  public HashMap<String, Object> requestFormBody(String requestUrl, String requestMethod,
      HashMap<String, String> params, HashMap<String, String> header, HashMap<String, String> body,
      File file) throws IOException {

    if (params != null) {
      requestUrl = combineParams(requestUrl, params);
    }
    System.out.println(requestUrl);
    HashMap<String, Object> ret = new HashMap<>();
    ret.put("status", "Error");
    ret.put("stderr", "");
    ret.put("content", "");
    CloseableHttpClient connection = null;
    try {

      connection = HttpClients.createDefault();

      HttpUriRequest httpRequest = null;
      if (requestMethod.isEmpty()) {
        requestMethod = "POST";
      }
      if (requestMethod.equals("GET")) {
        httpRequest = new HttpGet(requestUrl);
      } else if (requestMethod.equals("POST")) {
        httpRequest = new HttpPost(requestUrl);
      } else {
        throw new UnsupportedOperationException("Request type not supported.");
      }

      // Set the 'Authorization' header
      if (header != null) {
        for (String key : header.keySet()) {
          httpRequest.setHeader(key, header.get(key));
        }
      }
      List<BasicNameValuePair> formParams = new ArrayList<>();
      if (body != null) {
        for (String key : body.keySet()) {
          formParams.add(new BasicNameValuePair(key, body.get(key)));
        }
      }

      // Create a multipart entity with form data

      // Attach the file to the entity
      if (requestMethod.equals("POST")) {
      var builder = MultipartEntityBuilder.create();
      for (var nvp : formParams) {
        builder = builder.addTextBody(nvp.getName(), nvp.getValue());
      }
        if (file != null) {
          builder = builder.addPart("file", new FileBody(file))
              .setCharset(StandardCharsets.UTF_8).setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
              .setBoundary("===" + System.currentTimeMillis() + "===");
        }
        HttpEntity entity = builder.build();

        ((HttpEntityEnclosingRequest) httpRequest).setEntity(entity);
      }

      CloseableHttpResponse response = connection.execute(httpRequest);
      HttpEntity responseEntity = response.getEntity();

      String responseString = EntityUtils.toString(responseEntity);
//      System.out.println(responseString);
      ret.put("status", "OK");
      ret.put("stderr", "");
      ret.put("content", responseString);
      return ret;
    } catch (IOException e) {
      ret.put("stderr", e.toString());
    } finally {
      if (connection != null) {
        connection.close();
      }
    }
    return ret;
  }

  @Override
  public HashMap<String, Object> requestBinaryBody(String requestUrl,
      HashMap<String, String> header,
      File file) throws IOException {
    HashMap<String, Object> ret = new HashMap<>();
    ret.put("status", "");
    ret.put("stderr", "Unhandled exception!");
    ret.put("content", "");
    HttpURLConnection connection = null;
    try {
      URL url = new URL(requestUrl);
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");

      for (String key : header.keySet()) {
        connection.setRequestProperty(key, header.get(key));
      }
      connection.setConnectTimeout(connectTimeOut);
      connection.setReadTimeout(readTimeOut);

      connection.setDoInput(true);
      connection.setDoOutput(true);

      // Specify the content type as binary

      FileInputStream fileInputStream = new FileInputStream(file);

      // Get the connection's output stream and write the payload
      DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
      byte[] buffer = new byte[4096];
      int bytesRead;
      while ((bytesRead = fileInputStream.read(buffer)) != -1) {
        wr.write(buffer, 0, bytesRead);
      }
      fileInputStream.close();
      wr.flush();
      wr.close();

      int statusCode = connection.getResponseCode();

      if (statusCode > 299) {
        throw new Error(String.format("Http request error, code = %d", statusCode));
      }

      InputStream inputStream = connection.getInputStream();

      BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
      String inputLine;
      StringBuffer content = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        content.append(inputLine);
      }
      in.close();
      System.out.println(content);
      ret.put("status", "OK");
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

  public HashMap<String, Object> downloadFile(String requestUrl, HashMap<String, String> params,
      String outputFile) throws IOException {

    if (params != null) {
      requestUrl = combineParams(requestUrl, params);
    }
    HashMap<String, Object> ret = new HashMap<>();
    ret.put("status", "Error");
    ret.put("stderr", "Unhandled Exception!");
    CloseableHttpClient connection = null;
    try {

      connection = HttpClients.createDefault();

//      System.out.println("URL: " + requestUrl);
      HttpGet httpget = new HttpGet(requestUrl);
      HttpResponse response = connection.execute(httpget);
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        InputStream is = entity.getContent();
        FileOutputStream fos = new FileOutputStream(outputFile);
        int inByte;
        while ((inByte = is.read()) != -1) {
          fos.write(inByte);
        }
        is.close();
        fos.close();

        ret.put("status", "OK");
        ret.put("stderr", "");
      } else {
        throw new Error("No response.");
      }
      return ret;
    } catch (Exception e) {
      ret.put("status", "Exception");
      ret.put("stderr", e.toString());
    } catch (Error e) {
      ret.put("status", "Error");
      ret.put("stderr", e.toString());
    } finally {
      if (connection != null) {
        connection.close();
      }
    }
    return ret;
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

  private static String combineParams(String url, HashMap<String, String> params) {

    List<String> paramList = new ArrayList<>();
    for (String key : params.keySet()) {
      paramList.add(String.format("%s=%s", key, params.get(key)));
    }
    String paramStr = String.join("&", paramList);
    return url + "?" + paramStr;
  }
}
