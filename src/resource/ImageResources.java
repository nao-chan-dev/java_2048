package resource;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageResources {

    public static final Image GAME_ICON;

    public static final int NUMBER_OF_TILE_IMAGES = 22;
    public static final Map<Integer, Image> TILE_IMAGES;
    public static final Image INFINITY_TILE;

    static {
        URL iconPath = ImageResources.class.getClassLoader().getResource("images/app.jpg");
        GAME_ICON = iconPath == null ? null : new ImageIcon(iconPath).getImage();

        TILE_IMAGES = new HashMap<>();
        for (int i = 0; i <= NUMBER_OF_TILE_IMAGES; i++) {
            int tileValue = (int) Math.pow(2, i);
            if (tileValue == 1) { tileValue = 0; }
            String fileName = tileValue + ".jpg";
            URL imagePath = ImageResources.class.getClassLoader().getResource("images/" + fileName);
            if(imagePath == null) {
                continue;
            }
            Image image = new ImageIcon(imagePath).getImage();
            TILE_IMAGES.put(tileValue, image);
        }

        URL infinityPath = ImageResources.class.getClassLoader().getResource("images/infinity.jpg");
        INFINITY_TILE = infinityPath == null ? null : new ImageIcon(infinityPath).getImage();
    }
}
