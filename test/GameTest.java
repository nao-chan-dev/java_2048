import core.Direction;
import core.Game;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GameTest {

    private static final List<Integer> POSSIBLE_INITIAL_VALUES = List.of(2, 4);

    @Test
    public void initGameInitialTile() {

        Game game = new Game();

        assert game.getTiles().stream().filter(tile -> POSSIBLE_INITIAL_VALUES.contains(tile.getValue())).count() == 1;
    }

    @Test
    public void allTwosMergedInOne() {

        Game game = new Game(false);
        GameTestUtils.allTwos(game);

        game.move(Direction.DOWN);
        assert game.getTiles().stream().filter(tile -> 4 == tile.getValue()).count() == 8;

        game.move(Direction.RIGHT);
        assert game.getTiles().stream().filter(tile -> 8 == tile.getValue()).count() == 4;

        game.move(Direction.LEFT);
        assert game.getTiles().stream().filter(tile -> 16 == tile.getValue()).count() == 2;

        game.move(Direction.UP);
        assert game.getTiles().get(0).getValue() == 32;
    }

    @Test
    public void noMultipleMergesPerTile() {

        Game game = new Game(false);
        GameTestUtils.noMultipleMergesPerTile(game);

        game.move(Direction.LEFT);
        assert game.getTiles().stream().filter(tile -> !tile.isEmpty() && tile.getValue() == 4).count() == 2;
    }
}
