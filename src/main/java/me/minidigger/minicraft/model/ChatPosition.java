package me.minidigger.minicraft.model;

public enum ChatPosition {

    CHAT(0), SYSTEM(1), HOTBAR(2);

    private byte id;

    ChatPosition(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return id;
    }
}
