package core;

public class Tile {

    private int value;
    private boolean merged;

    public boolean isEmpty() {
        return value == 0;
    }

    public void eraseValue() {
        setValue(0);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }
}
