package dictionary.apiservices;

import dictionary.apis.ExternalAPIs;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TTSService {
    private static final String tts_audio_path = "temp/speech_tmp.txt";
    private static boolean playing = false;
    private static int playVoice() {
        final int[] ret = {0};
        if (playing) return ret[0];
        ExecutorService executor = null;
        try {
            executor = Executors.newSingleThreadExecutor();
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("Playing");
                        FileInputStream fis = new FileInputStream(tts_audio_path);
                        AdvancedPlayer player = new AdvancedPlayer(fis, FactoryRegistry.systemRegistry().createAudioDevice());
                        playing = true;
                        player.play();
                        playing = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                        ret[0] = -1;
                    } finally {
                        // Delete the file after playing the audio
//                        File file_to_delete = new File(tts_audio_path);
//                        if (file_to_delete.exists()) {
//                                if (file_to_delete.delete()) {
////                                    System.out.println("File deleted successfully.");
//                                } else {
//                                    System.err.println("Failed to delete file.");
//                                }
//                        }
                    }
                }
            });
        } finally {
            if (executor != null) {
                executor.shutdown();
            }
        }
        return ret[0];
    }

    private static HashMap<String, Object> getVoice(String text, String lang) {
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("status", "");
        ret.put("stderr", "Unhandled Exception!");
        ret.put("content", "");
        try {
            String url = "http://translate.google.com/translate_tts";
            HashMap<String, String> params = new HashMap<>();
            params.put("tl", lang);
            params.put("q", URLEncoder.encode(text, StandardCharsets.UTF_8));
            params.put("client", "tw-ob");

            HashMap<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/json; utf-8");

            ExternalAPIs req = new ExternalAPIs(4000, 4000);
            HashMap<String, Object> response = req.downloadFile(url, params, tts_audio_path);

            if (!response.get("status").equals("OK")) {
                throw new Error(String.valueOf(response.get("stderr")));
            }
            ret.put("status", "OK");
            ret.put("stderr", "");
        } catch (Exception e) {
            ret.put("status", "Exception");
            ret.put("stderr", e.toString());
        } catch (Error e) {
            ret.put("status", "Error");
            ret.put("stderr", e.toString());
        }
        return ret;
    }

    /**
     * Speak input text.
     * @param lang  Language ("en" or "vi").
     * @param text  Input text.
     */
    public static String TTS(String text, String lang) {

        Pattern spacePattern = Pattern.compile("^[\\s]+$");
        Matcher matcher = spacePattern.matcher(text);
        if (matcher.matches()) {
            // Text contains only white spaces.
            return "OK";
        }

        HashMap<String, Object> response = getVoice(text, lang);
        if (!response.get("status").equals("OK")) {
            return "Error when connecting to Translate server." + response.get("stderr");
        }
        int code = playVoice();
        if (code != 0) {
            return "Error when playing voice.";
        }
        return "OK";
    }
}
