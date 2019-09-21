package me.minidigger.miniserver.test.model;

import com.google.common.base.MoreObjects;

import net.kyori.text.Component;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
import java.util.UUID;

public class ServerStatusResponse {

    private Version version;
    private Players players;
    private Description description;
    private String favicon;

    public ServerStatusResponse(Version version, Players players, Component description, String favicon) {
        this.version = version;
        this.players = players;
        this.description = new Description(LegacyComponentSerializer.INSTANCE.serialize(description));
        this.favicon = favicon;
    }

    public Version getVersion() {
        return version;
    }

    public Players getPlayers() {
        return players;
    }

    public Component getDescription() {
        return LegacyComponentSerializer.INSTANCE.deserialize(description.getText());
    }

    public String getFavicon() {
        return favicon;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("version", version)
                .add("players", players)
                .add("description", description)
                .add("favicon", favicon == null)
                .toString();
    }

    public static class Version {
        private String name;
        private int protocol;

        public Version(String name, int protocol) {
            this.name = name;
            this.protocol = protocol;
        }

        public String getName() {
            return name;
        }

        public int getProtocol() {
            return protocol;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("name", name)
                    .add("protocol", protocol)
                    .toString();
        }
    }

    public static class Players {

        private int max;
        private int online;
        private List<Player> sample;

        public Players(int max, int online, List<Player> sample) {
            this.max = max;
            this.online = online;
            this.sample = sample;
        }

        public int getMax() {
            return max;
        }

        public int getOnline() {
            return online;
        }

        public List<Player> getSample() {
            return sample;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("max", max)
                    .add("online", online)
                    .add("sample", sample)
                    .toString();
        }

        public static class Player {

            private String name;
            private UUID id;

            public Player(String name, UUID id) {
                this.name = name;
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public UUID getId() {
                return id;
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper(this)
                        .add("name", name)
                        .add("id", id)
                        .toString();
            }
        }
    }

    public static class Description {

        private String text;

        public Description(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("text", text)
                    .toString();
        }
    }
}
