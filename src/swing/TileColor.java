package swing;

import java.awt.Color;
import java.util.function.BiFunction;

public enum TileColor {

    RED((mainColor, otherColors) -> new Color(mainColor, otherColors, otherColors)),
    GREEN((mainColor, otherColors) -> new Color(otherColors, mainColor, otherColors)),
    BLUE((mainColor, otherColors) -> new Color(otherColors, otherColors, mainColor));

    private static final int MAX_BRIGHTNESS_LEVEL = (int) (Math.log(2048)/Math.log(2));

    private final BiFunction<Integer, Integer, Color> colorLevels;

    TileColor(BiFunction<Integer, Integer, Color> colorLevels) {
        this.colorLevels = colorLevels;
    }

    public Color awtColor(int tileValue) {
        int currentTileLevel = (int) (Math.log(tileValue) / Math.log(2));
        int brightnessLevel = Math.max(MAX_BRIGHTNESS_LEVEL - currentTileLevel, 0);
        int otherColors = 255 / 12 * brightnessLevel;
        int darknessLevel = Math.max(currentTileLevel - MAX_BRIGHTNESS_LEVEL + 2, 0);
        int mainColor = Math.max(255 - (255 / 12 * darknessLevel), 80);
        return colorLevels.apply(mainColor, otherColors);
    }
}