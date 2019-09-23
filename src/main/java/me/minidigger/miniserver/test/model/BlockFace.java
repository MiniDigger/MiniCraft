package me.minidigger.miniserver.test.model;

public enum BlockFace {

    BOTTOM(0, 0, -1, 0),
    TOP(1, 0, 1, 0),
    NORTH(2, 0, 0, -1),
    SOUTH(3, 0, 0, 1),
    WEST(4, -1, 0, 0),
    EAST(5, 1, 0, 0);

    private int id;
    private int offsetX, offsetY, offsetZ;

    BlockFace(int id, int offsetX, int offsetY, int offsetZ) {
        this.id = id;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    public static BlockFace fromId(int id) {
        for (BlockFace value : values()) {
            if(value.id == id){
                return value;
            }
        }

        throw new IllegalArgumentException("Unknown id " + id);
    }
}
