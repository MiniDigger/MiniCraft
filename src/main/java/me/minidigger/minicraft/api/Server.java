package me.minidigger.minicraft.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import javax.imageio.ImageIO;

public class Server {

    private static final Logger log = LoggerFactory.getLogger(Server.class);

    private String name = "MiniServer \u26cf";
    private boolean offlineMode = true;
    private List<Player> players = new ArrayList<>();
    private BufferedImage serverIcon;
    private String encodedServerIcon;

    public String getName() {
        return name;
    }

    public boolean isOfflineMode() {
        return offlineMode;
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public void start() {
        try {
            serverIcon = ImageIO.read(new File("server-icon.png"));
        } catch (IOException e) {
            log.warn("Error while loading server-icon.png", e);
        }

        Thread serverThread = new Thread(() -> {
            while (true) {
                tick();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        serverThread.setName("MiniServer Thread");
        serverThread.start();
    }

    public void tick() {
        // tick players
        for (Player player : players) {
            try {
                player.tick();
            } catch (Exception ex) {
                log.warn("Error while ticking player {}", player.getName(), ex);
            }
        }
    }

    public String getServerIcon() {
        if (encodedServerIcon == null) {
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                ImageIO.write(serverIcon, "png", os);
                encodedServerIcon = "data:image/png;base64," + Base64.getEncoder().encodeToString(os.toByteArray());
            } catch (final IOException e) {
                log.warn("Couldn't encode server icon", e);
                return null;
            }
        }

        return encodedServerIcon;
    }
}
