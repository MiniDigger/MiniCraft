package me.minidigger.miniserver.test.api;

public class Server {

    private String name = "MiniServer \u26cf";
    private boolean offlineMode = true;

    public String getName() {
        return name;
    }

    public boolean isOfflineMode() {
        return offlineMode;
    }
}
