package me.minidigger.miniserver.test.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Server {

    private static final Logger log = LoggerFactory.getLogger(Server.class);

    private String name = "MiniServer \u26cf";
    private boolean offlineMode = true;
    private List<Player> players = new ArrayList<>();

    public String getName() {
        return name;
    }

    public boolean isOfflineMode() {
        return offlineMode;
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public void tick() {
        // tick players
        for(Player player : players) {
            try {
                player.tick();
            }catch (Exception ex) {
                log.warn("Error while ticking player {}",player.getName(), ex);
            }
        }
    }
}
