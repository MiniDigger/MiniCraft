package me.minidigger.minicraft.model;

import com.google.common.base.MoreObjects;

public class Key {

    private String namespace;
    private String key;

    private Key() {
    }

    public static Key of(String key) {
        return of("minecraft", key);
    }

    public static Key of(String namespace, String key) {
        Key k = new Key();
        k.namespace = namespace;
        k.key = key;
        return k;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getKey() {
        return key;
    }

    public String combined() {
        return namespace + ":" + key;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("namespace", namespace)
                .add("key", key)
                .toString();
    }
}
