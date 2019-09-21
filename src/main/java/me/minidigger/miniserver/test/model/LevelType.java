package me.minidigger.miniserver.test.model;

public enum LevelType {

    DEFAULT("default"),
    FLAT("flat"),
    LARGE_BIOMES("largeBiomes"),
    AMPLIFIED("amplified"),
    CUSTOMIZED("customized"),
    BUFFET("buffet"),
    DEFAULT_1_1("default_1_1");

    private String id;

    LevelType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
