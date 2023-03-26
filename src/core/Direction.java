package core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public enum Direction {

    UP(() -> Game.size * -1, (current, next) -> next >= 0, (index) -> index),
    DOWN(() -> Game.size, (current, next) -> next < Game.length(), (index) -> index * -1),
    LEFT(() -> -1, SameLineBoundsRule.LAMBDA, (index) -> index % Game.size),
    RIGHT(() -> 1, SameLineBoundsRule.LAMBDA, (index) -> index % Game.size * -1);

    Direction(Supplier<Integer> indexDifference, BiFunction<Integer, Integer, Boolean> boundsRule, Function<Integer, Integer> sortPriority){
        this.indexDifference = indexDifference;
        this.boundsRule = boundsRule;
        this.sortPriority = sortPriority;
    }

    private final Supplier<Integer> indexDifference;
    private final BiFunction<Integer, Integer, Boolean> boundsRule;
    private final Function<Integer, Integer> sortPriority;

    public int getIndexDifference() {
        return indexDifference.get();
    }

    public boolean validateBounds(int currentIndex, int nextIndex) {
        return boundsRule.apply(currentIndex, nextIndex);
    }

    public List<Tile> getSortedTiles(List<Tile> tiles) {
        Comparator<Tile> comparator = Comparator.comparing(tile -> sortPriority.apply(tiles.indexOf(tile)));
        List<Tile> sortedTiles = new ArrayList<>(tiles);
        sortedTiles.sort(comparator);
        return sortedTiles;
    }

    private static class SameLineBoundsRule {
        private static final BiFunction<Integer, Integer, Boolean> LAMBDA = (current, next) -> {
            int minBound = current / Game.size * Game.size;
            int maxBound = minBound + Game.size - 1;
            return next >= minBound && next <= maxBound;
        };
    }
}
