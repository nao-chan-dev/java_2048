import core.Direction;
import core.Game;
import core.GameConsole;

import java.util.Map;
import java.util.Scanner;

public class Nao2048Console {

    private static final Map<String, Direction> inputDirections = Map.of(
            "u", Direction.UP,
            "d", Direction.DOWN,
            "l", Direction.LEFT,
            "r", Direction.RIGHT
    );

    public static void main(String[] args) {

        Game game = new Game();
        GameConsole.showTiles(game);

        String input = "";
        while (!input.equals("q")) {
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine().toLowerCase();
            String inputDirection = String.valueOf(input.charAt(0));
            if(inputDirections.containsKey(inputDirection)) {
                game.move(inputDirections.get(inputDirection));
                GameConsole.showTiles(game);
            }
        }
    }
}
