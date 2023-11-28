package dictionary.services;

import javafx.scene.image.Image;

public class ImageService {
    public Image loadImage(String name) {
        Image res = new Image(getClass().getResourceAsStream("/data/images/" + name + ".jpg"));
        return res;
    }
}
