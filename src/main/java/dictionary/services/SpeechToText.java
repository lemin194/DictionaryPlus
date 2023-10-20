package dictionary.services;

import static org.json.HTTP.CRLF;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat;
import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.entity.mime.MultipartEntityBuilder;

public class SpeechToText {

  private static boolean recording = false;
  private static AudioFormat format;
  private static TargetDataLine line;
  private static final String recordPath = "temp/record_tmp.mp3";

  private static AudioFormat buildAudioFormatInstance() {
    AudioFormat audioFormat = new AudioFormat(
        AudioFormat.Encoding.PCM_SIGNED,
        44100,
        16,
        2,
        4,
        44100,
        false
    );
    return audioFormat;
  }

  private static TargetDataLine getTargetDataLineForRecord(AudioFormat format)
      throws LineUnavailableException {
    TargetDataLine line;
    DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
    System.out.println(Arrays.toString(info.getFormats()));
    if (!AudioSystem.isLineSupported(info)) {
      System.out.println("Line not supported.");
      return null;
    }
    line = (TargetDataLine) AudioSystem.getLine(info);
    return line;
  }

  private static void initAudioLine() throws LineUnavailableException {
    format = buildAudioFormatInstance();
    line = getTargetDataLineForRecord(format);
  }

  public static int beginRecord() {
    if (recording) {
      return 0;
    }
    ExecutorService executor = null;
    final int[] ret = {0};
    try {
      if (line == null) {
        initAudioLine();
      }
      System.out.println("buffer_size: " + line.getBufferSize());
      line.open(format, line.getBufferSize());
      line.start();
      recording = true;
      executor = Executors.newSingleThreadExecutor();
      executor.submit(new Runnable() {
        @Override
        public void run() {
          try {

            System.out.println("Start recording...");
            AudioInputStream recordingStream = new AudioInputStream(line);
            File outputFile = new File(recordPath);
            AudioSystem.write(recordingStream, Type.WAVE, outputFile);
            System.out.println("Stop recording...");
            recording = false;
            ret[0] = 0;
          } catch (Exception e) {
            e.printStackTrace();
            ret[0] = -1;
          } finally {
          }
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      ret[0] = -1;
    } finally {
      if (executor != null) {
        executor.shutdown();
      }
    }
    return ret[0];
  }

  public static int stopRecording() {
    if (recording) {
      try {
        line.stop();
        line.close();
        recording = false;
      } catch (Exception e) {
        recording = false;
        e.printStackTrace();
        return -1;
      }
    }
    return 0;
  }


  public static Map<String, String> STT(String lang) {

    Map<String, String> ret = new HashMap<>();
    ret.put("status", "OK");
    ret.put("stderr", "");
    ret.put("content", "");
    int status = 1000;

    File audioFile = new File(recordPath);
    if (!audioFile.exists()) {
      ret.put("status", "Error");
      ret.put("stderr", "No audio file to translate!");
      ret.put("content", "");
      return ret;
    }
    try {
      if (lang.equals("vi")) {
        HttpURLConnection connection = null;
        URL url = new URL(
            "https://api.fpt.ai/hmi/asr/general");
        System.out.println("ok");
        url = new URL(
            "https://api.fpt.ai/hmi/asr/general"
        );
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        connection.setRequestProperty("api-key", "F3t6WZhg3DGu6FBBDeaXGHceUUZYx4n8");

        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        connection.setDoInput(true);
        connection.setDoOutput(true);

        // Specify the content type as binary
        connection.setRequestProperty("Content-Type", "application/octet-stream");

        FileInputStream fileInputStream = new FileInputStream(recordPath);

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

        status = connection.getResponseCode();

        if (status > 299) {
          ret.put("status", "Error");
          ret.put("stderr", String.format("Http request error, code = %d", status));
          ret.put("content", "");
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
        System.out.println(content);
        System.out.println(new JSONObject(content.toString()).get("hypotheses"));
        ret.put("content",
            new JSONObject(
                new JSONArray(
                    new JSONObject(
                        content.toString()
                    ).get("hypotheses").toString()
                ).get(0).toString()
            ).get("utterance").toString()
        );
        if (connection != null) {
          connection.disconnect();
        }
      } else {
        String apiKey = "PVD42ZJYK89N87FDGNEUCMXN6AY7NTIJ";
        String filePath = recordPath;
        String fileType = "mp3";
        String url = "https://transcribe.whisperapi.com";

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);

        // Set the 'Authorization' header
        httpPost.setHeader("Authorization", "Bearer " + apiKey);

        List<BasicNameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("fileType", fileType));
        formParams.add(new BasicNameValuePair("diarization", "false"));
        formParams.add(new BasicNameValuePair("numSpeakers", "1"));
        formParams.add(new BasicNameValuePair("initialPrompt", ""));
        formParams.add(new BasicNameValuePair("language", lang));
        formParams.add(new BasicNameValuePair("task", "transcribe"));
        formParams.add(new BasicNameValuePair("callbackURL", ""));
        System.out.println(lang);

        // Create a multipart entity with form data

        // Attach the file to the entity
        var builder = MultipartEntityBuilder.create();
        for (var nvp : formParams) {
          builder = builder.addTextBody(nvp.getName(), nvp.getValue());
        }
        HttpEntity entity = builder.addPart("file", new FileBody(new File(filePath)))
            .setCharset(StandardCharsets.UTF_8)
            .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
            .setBoundary("===" + System.currentTimeMillis() + "===")
            .build();

        httpPost.setEntity(entity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();

        if (responseEntity != null) {
          String responseString = EntityUtils.toString(responseEntity);
          System.out.println(responseString);
          ret.put("status", "OK");
          ret.put("stderr", "");
          ret.put("content", new JSONObject(responseString).get("text").toString());
        }
      }
    } catch (Exception e) {
      ret.put("status", "Error");
      ret.put("stderr", "Exception: " + e);
      ret.put("content", "");
    } finally {
      if (audioFile.exists()) {
        if (audioFile.delete()) {
//                                    System.out.println("File deleted successfully.");
        } else {
          System.err.println("Failed to delete file.");
        }
      }
    }
    return ret;
  }
}
