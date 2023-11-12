package dictionary.services;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class AnimatedGif extends Animation {

  public AnimatedGif(URL filename, double durationMs) throws IOException {
    super();

    GifDecoder d = new GifDecoder();
    d.read(filename.openStream());

    Image[] sequence = new Image[d.getFrameCount()];
    for (int i = 0; i < d.getFrameCount(); i++) {

      WritableImage wimg = null;
      BufferedImage bimg = d.getFrame(i);
      sequence[i] = SwingFXUtils.toFXImage(bimg, wimg);

    }

    super.init(sequence, durationMs);
  }

}
