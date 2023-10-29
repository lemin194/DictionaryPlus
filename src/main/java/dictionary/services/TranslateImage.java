package dictionary.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import org.json.JSONObject;

public class TranslateImage {

  public static Image translate(Image img) {

    Image ret = null;
    try {
      String content = encodeImageToBase64(img);
      JSONObject body = new JSONObject();
      body.put("file", content);
      JSONObject res = BackendUtils.request("http://127.0.0.1:9876/translateimage/", "POST", body);

      if ((int)res.get("status") == 200) {
        String encodedImage = new JSONObject(res.get("content").toString()).get("file").toString();
        ret = decodeImageFromBase64(encodedImage);
//        System.out.println(new JSONObject(res.get("content").toString()).get("content").toString());
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return ret;
  }



  private static String encodeImageToBase64(Image image) {
    try {
      BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
      byte[] bytes = byteArrayOutputStream.toByteArray();
      return Base64.getEncoder().encodeToString(bytes);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private static Image decodeImageFromBase64(String encodedImage) {

    byte[] imageBytes = Base64.getDecoder().decode(encodedImage);

    // Create an Image from the byte array
    Image image = new Image(new java.io.ByteArrayInputStream(imageBytes));
    return image;
  }
}
