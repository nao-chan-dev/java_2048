package core;

public class GameConsole {

    public static void showTiles(Game game) {

        int maxNumber = game.getTiles().stream().map(Tile::getValue).max(Integer::compareTo).orElse(0);
        int maxNumberLength = String.valueOf(maxNumber).length();
        int currentCount = 1;
        for (Tile tile: game.getTiles()) {
            String tileString = tile.isEmpty() ? "" : String.valueOf(tile.getValue());
            System.out.printf("[%1$" + maxNumberLength + "s] ", tileString);
            if (currentCount % Game.size == 0) {
                System.out.println();
            }
            currentCount++;
        }
    }
}
