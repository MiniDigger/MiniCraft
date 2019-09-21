package me.minidigger.miniserver.test.model;

public enum GameMode {
    SURVIVAL(0), CREATIVE(1), ADVENTURE(2), SPECTATOR(3);

    private byte id;

    GameMode(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return id;
    }
}
