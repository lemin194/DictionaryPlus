package dictionary.apiservices;

import dictionary.apis.ExternalAPIs;
import dictionary.apis.FlaskAPIs;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import org.json.JSONArray;
import org.json.JSONObject;

public class SpeechService {

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


  public static HashMap<String, String> STT(String lang) {

    HashMap<String, String> ret = new HashMap<>();
    ret.put("status", "OK");
    ret.put("stderr", "");
    ret.put("content", "");
    ret.put("confident", "0");

    File audioFile = new File(recordPath);
    if (!audioFile.exists()) {
      ret.put("status", "Error");
      ret.put("stderr", "No audio file to translate!");
      ret.put("content", "");
      return ret;
    }
    try {
      if (lang.equals("vi")) {
        String responseString = callFptSTT();
        ret.put("content",
            new JSONObject(
                new JSONArray(
                    new JSONObject(
                        responseString
                    ).get("hypotheses").toString()
                ).get(0).toString()
            ).get("utterance").toString()
        );
      } else {
        String responseString = callWhisperAPI(lang);
        ret.put("status", "OK");
        ret.put("stderr", "");
        ret.put("content", new JSONObject(responseString).get("text").toString());
        JSONObject fullRes = new JSONObject(responseString);
        JSONArray segments = new JSONArray(fullRes.get("segments").toString());

      }
    } catch (Exception e) {
      ret.put("status", "Exception");
      ret.put("stderr", "Exception: " + e);
      ret.put("content", "");
    } catch (Error e) {
      ret.put("status", "Error");
      ret.put("stderr", "Error: " + e);
      ret.put("content", "");
    } finally {
      if (audioFile.exists()) {
//        if (audioFile.delete()) {
////                                    System.out.println("File deleted successfully.");
//        } else {
//          System.err.println("Failed to delete file.");
//        }
      }
    }
    return ret;
  }

  private static String callFptSTT() throws IOException {
    String url = "https://api.fpt.ai/hmi/asr/general";

    HashMap<String, String> header = new HashMap<>();
    header.put("api-key", "F3t6WZhg3DGu6FBBDeaXGHceUUZYx4n8");
    header.put("Content-Type", "application/octet-stream");

    File file = new File(recordPath);

    ExternalAPIs req = new ExternalAPIs(10000, 30000);
    HashMap<String, Object> response = req.requestBinaryBody(url, header, file);
    String status = (String) response.get("status");

    if (!status.equals("OK")) {
      throw new Error(String.valueOf(response.get("stderr")));
    }

    String responseString = (String) response.get("content");
    return responseString;
  }

  private static String callWhisperAPI(String lang) throws Exception {
    String apiKey = "PVD42ZJYK89N87FDGNEUCMXN6AY7NTIJ";
    String filePath = recordPath;
    String fileType = "mp3";
    String url = "https://transcribe.whisperapi.com";

    HashMap<String, String> header = new HashMap<>();
    header.put("Authorization", "Bearer " + apiKey);

    HashMap<String, String> body = new HashMap<>();
    body.put("fileType", fileType);
    body.put("diarization", "false");
    body.put("numSpeakers", "1");
    body.put("initialPrompt", "");
    body.put("language", lang);
    body.put("task", "transcribe");
    body.put("callbackURL", "");

    File file = new File(filePath);

    ExternalAPIs req = new ExternalAPIs(20000, 30000);
    HashMap<String, Object> response = req.requestFormBody(url, "POST", null, header, body, file);

    if (!response.get("status").equals("OK")) {
      throw new Error(String.valueOf(response.get("stderr")));
    }

    return (String) response.get("content");
  }

  public static Map<String, Object> SpeechAnalysis(String src) {
    Map<String, Object> ret = new HashMap<>();
    ret.put("content", "");
    ret.put("status", "");
    ret.put("stderr", "Unhandled Exception!");

//
//    JSONObject body = new JSONObject();
//    try {
//      body.put("file", encodeFileToBase64("temp/record_tmp.mp3"));
//      body.put("src", src);
//      JSONObject res = BackendUtils.request("http://0.0.0.0:9876/speechanalysis/", "POST", body);
//      ret.put("status", res.get("status").toString());
//      if ((int)res.get("status") == 200) {
//        JSONArray analysisArr = new JSONArray(new JSONObject(res.get("content").toString()).get("content").toString());
//        List<List<String>> analysis = new ArrayList<>();
//        for (Object wordObj : analysisArr) {
//          JSONArray wordProb = (JSONArray) wordObj;
//          String word = wordProb.get(0).toString();
//          String prob = wordProb.get(1).toString();
//          analysis.add(List.of(word, prob));
//        }
//        ret.put("content", analysis);
//      }
//
//    } catch (IOException e) {
//      throw new RuntimeException(e);
//    }
    String url = "http://0.0.0.0:9876/speechanalysis/";
    try {
      HashMap<String, String> body = new HashMap<>();
      body.put("file", encodeFileToBase64("temp/record_tmp.mp3"));
      body.put("src", src);

      FlaskAPIs req = new FlaskAPIs(30000, 30000);
      HashMap<String, Object> response = req.requestJsonBody(url, "POST", null, body);

      ret.put("status", response.get("status").toString());
      ret.put("stderr", "");
      if (!response.get("status").toString().equals("OK")) {
        throw new Error(response.get("stderr").toString());
      }
        JSONArray analysisArr = new JSONArray(new JSONObject(response.get("content").toString()).get("content").toString());
        List<List<String>> analysis = new ArrayList<>();
        for (Object wordObj : analysisArr) {
          JSONArray wordProb = (JSONArray) wordObj;
          String word = wordProb.get(0).toString();
          String prob = wordProb.get(1).toString();
          analysis.add(List.of(word, prob));
        }
        ret.put("content", analysis);
    } catch (IOException e) {
      ret.put("status", "Exception");
      ret.put("stderr", e.toString());
    } catch (Error e) {
      ret.put("status", "Error");
      ret.put("stderr", e.toString());
    }

    return ret;
  }

  private static String encodeFileToBase64(String fileName) throws IOException {
    try {
      byte[] fileContent = Files.readAllBytes(Path.of(fileName));
      return Base64.getEncoder().encodeToString(fileContent);
    } catch (IOException e) {
      throw new IllegalStateException("could not read file " + fileName, e);
    }
  }
}
