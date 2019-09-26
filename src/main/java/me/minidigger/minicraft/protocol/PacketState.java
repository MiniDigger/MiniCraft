package me.minidigger.minicraft.protocol;

public enum PacketState {

    HANDSHAKE(0), STATUS(1), LOGIN(2), PLAY(3);

    private int id;

    PacketState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static PacketState fromId(int id) {
        for (PacketState value : values()) {
            if (value.id == id) {
                return value;
            }
        }

        throw new IllegalArgumentException("Unknown id " + id);
    }
}
