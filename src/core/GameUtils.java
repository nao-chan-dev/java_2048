package core;

import java.util.List;
import java.util.Random;

public class GameUtils {

    public static int spawnNewTile(List<Tile> emptyTiles) {
        int randomIndex = new Random().nextInt(emptyTiles.size());
        emptyTiles.get(randomIndex).setValue((new Random().nextInt(2) + 1) * 2);
        return randomIndex;
    }
}
