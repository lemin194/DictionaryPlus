package dictionary.services;

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


public class TextToSpeech {
    private static final String tts_audio_path = "temp/speech_tmp.mp3";
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
                        File file_to_delete = new File(tts_audio_path);
                        if (file_to_delete.exists()) {
                                if (file_to_delete.delete()) {
//                                    System.out.println("File deleted successfully.");
                                } else {
                                    System.err.println("Failed to delete file.");
                                }
                        }
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

    private static int getVoice(String text, String lang) {
        int status = 1000;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(
                    "http://translate.google.com/translate_tts?tl=" + lang +
                            "&q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) +
                            "&client=tw-ob"
            );
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setRequestProperty("Content-Type", "application/json; utf-8");

            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            connection.setDoOutput(true);


            status = connection.getResponseCode();

            Reader streamReader = null;

            if (status > 299) {
                return status;
            }

            InputStream inputStream = connection.getInputStream();


            Files.createDirectories(Paths.get(tts_audio_path).getParent());
            FileOutputStream outputStream = new FileOutputStream(tts_audio_path);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return status;
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

        int code = 0;
        code = getVoice(text, lang);
        if (code > 299) {
            return "Error when connecting to Translate server, error code: " + code + ".";
        }
        code = playVoice();
        if (code != 0) {
            return "Error when playing voice.";
        }
        return "OK";
    }
}
