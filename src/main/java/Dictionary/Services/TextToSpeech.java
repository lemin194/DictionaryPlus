package Dictionary.Services;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class TextToSpeech {
    private static final String tts_audio_path = "temp/speech_tmp.mp3";
    private static int playVoice() {
        ExecutorService executor = null;
        try {
            executor = Executors.newSingleThreadExecutor();
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {

                        try {
                            FileInputStream fis = new FileInputStream(tts_audio_path);
                            AdvancedPlayer player = new AdvancedPlayer(fis, FactoryRegistry.systemRegistry().createAudioDevice());
                            player.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            // Delete the file after playing the audio
                            File file_to_delete = new File(tts_audio_path);
                            if (file_to_delete.exists()) {
                                if (file_to_delete.delete()) {
                                    System.out.println("File deleted successfully.");
                                } else {
                                    System.err.println("Failed to delete file.");
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            });
        } finally {
            if (executor != null) {
                executor.shutdown();
            }
        }
        return 0;
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
     * Text to Speech API.
     * @param lang  Language ("en" or "vi").
     * @param text  Input text.
     */
    public static String TTS(String text, String lang) {
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
