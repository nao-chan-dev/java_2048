package core;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {

    public static int size = 4;

    private final List<Tile> tiles;
    private final boolean spawningEnabled;
    private boolean gameOver = false;

    public Game () {
        tiles = Stream.generate(Tile::new).limit(length()).collect(Collectors.toList());
        GameUtils.spawnNewTile(tiles);
        this.spawningEnabled = true;
    }

    public Game (boolean spawningEnabled) {
        tiles = Stream.generate(Tile::new).limit(length()).collect(Collectors.toList());
        this.spawningEnabled = spawningEnabled;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public static int length() {
        return size * size;
    }

    public void move(Direction direction) {
        for (Tile sortedTile: direction.getSortedTiles(tiles)) {
            int sortedIndex = tiles.indexOf(sortedTile);
            Tile tile = tiles.get(sortedIndex);
            if (!tile.isEmpty()) {
                MovementOperator.move(tile, tiles, direction);
            }
            tile.setMerged(false);
        }
        List<Tile> emptyTiles = tiles.stream().filter(Tile::isEmpty).collect(Collectors.toList());
        if(MovementOperator.stateChanged && !emptyTiles.isEmpty() && spawningEnabled){
            int spawnedTileIndex = GameUtils.spawnNewTile(emptyTiles);
            emptyTiles.remove(spawnedTileIndex);
        }
        MovementOperator.stateChanged = false;
        if(emptyTiles.isEmpty() && noMoreMoves()) {
            gameOver = true;
        }
    }

    private boolean noMoreMoves() {
        for (Direction direction : Direction.values()) {
            for (Tile sortedTile: direction.getSortedTiles(tiles)) {
                int sortedIndex = tiles.indexOf(sortedTile);
                Tile tile = tiles.get(sortedIndex);
                if (MovementOperator.canMove(tile, tiles, direction)) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getScore() {
        return tiles.stream().mapToInt(Tile::getValue).sum();
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
