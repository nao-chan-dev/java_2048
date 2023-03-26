import core.Game;
import core.Tile;

public class GameTestUtils {

    public static void allTwos(Game game) {
        for (Tile tile: game.getTiles()) {
            tile.setValue(2);
        }
    }

    public static void noMultipleMergesPerTile(Game game) {
        game.getTiles().get(0).setValue(2);
        game.getTiles().get(1).setValue(2);
        game.getTiles().get(2).setValue(4);
    }
}
