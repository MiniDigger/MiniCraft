package me.minidigger.minicraft.model;

public enum Hand {

    LEFT(0), RIGHT(1);

    private int id;

    Hand(int id) {
        this.id = id;
    }

    public static Hand fromId(int id) {
        for (Hand value : values()) {
            if(value.id == id){
                return value;
            }
        }

        throw new IllegalArgumentException("Unknown id " + id);
    }
}
