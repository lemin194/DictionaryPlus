package dictionary.apiservices;

import dictionary.apis.FlaskAPIs;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import org.json.JSONObject;

public class TranslateImageService {

  public static Image translate(Image img) {

    Image ret = null;
    try {
      String content = encodeImageToBase64(img);
      HashMap<String, String> body = new HashMap<>();
      body.put("file", content);
      FlaskAPIs req = new FlaskAPIs(30000, 30000);
      HashMap<String, Object> res = req.requestJsonBody("http://127.0.0.1:9876/translateimage/", "POST", null, body);

      if (res.get("status").equals("OK")) {
        String encodedImage = new JSONObject(res.get("content").toString()).get("file").toString();
        ret = decodeImageFromBase64(encodedImage);
//        System.out.println(new JSONObject(res.get("content").toString()).get("content").toString());
      } else {
        throw new Error(res.get("stderr").toString());
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    } catch (Error e) {
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
