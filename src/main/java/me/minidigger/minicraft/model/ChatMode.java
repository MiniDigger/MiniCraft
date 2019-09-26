package me.minidigger.minicraft.model;

public enum ChatMode {
    ENABLED(0), COMMANDS_ONLY(1), HIDDEN(2);

    private int id;

    ChatMode(int id) {
        this.id = id;
    }

    public static ChatMode fromId(int id) {
        for (ChatMode value : values()) {
            if(value.id == id){
                return value;
            }
        }

        throw new IllegalArgumentException("Unknown id " + id);
    }
}
