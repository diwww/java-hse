package warehouse;

public class Item {
    private String name;
    private boolean spoiled;

    public Item() {
    }

    public Item(boolean s) {
        spoiled = s;
    }

    public boolean isSpoiled() {
        return spoiled;
    }
}
