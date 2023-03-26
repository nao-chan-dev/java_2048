package core;

import java.util.List;

public class MovementOperator {

    public static boolean stateChanged = false;

    public static void move(Tile tileToMove, List<Tile> allTiles, Direction direction) {

        int currentIndex = allTiles.indexOf(tileToMove);
        int nextIndex = currentIndex + direction.getIndexDifference();
        if (!direction.validateBounds(currentIndex, nextIndex)) {
            return;
        }
        Tile nextPossibleTile = allTiles.get(nextIndex);
        if(!nextPossibleTile.isEmpty()) {
            if(nextPossibleTile.getValue() == tileToMove.getValue() && !nextPossibleTile.isMerged()) {
                nextPossibleTile.setValue(nextPossibleTile.getValue() + tileToMove.getValue());
                tileToMove.eraseValue();
                nextPossibleTile.setMerged(true);
                stateChanged = true;
            }
            return;
        }
        nextPossibleTile.setValue(tileToMove.getValue());
        tileToMove.eraseValue();
        stateChanged = true;
        move(nextPossibleTile, allTiles, direction);
    }

    public static boolean canMove(Tile tileToMove, List<Tile> allTiles, Direction direction) {
        int currentIndex = allTiles.indexOf(tileToMove);
        int nextIndex = currentIndex + direction.getIndexDifference();
        if (!direction.validateBounds(currentIndex, nextIndex)) {
            return false;
        }
        Tile nextPossibleTile = allTiles.get(nextIndex);
        return nextPossibleTile.getValue() == tileToMove.getValue() && !nextPossibleTile.isMerged();
    }
}
