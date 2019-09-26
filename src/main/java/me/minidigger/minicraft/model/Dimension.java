package me.minidigger.minicraft.model;

public enum Dimension {

    NETHER(-1), OVERWORLD(0), END(1);

    private int id;

    Dimension(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
