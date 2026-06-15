package dev.tqqn.common.modular;

import dev.tqqn.common.GamePlugin;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 15/06/2026
 */
public final class PluginProvider {

    private static GamePlugin<?> instance;

    public static void register(GamePlugin<?> plugin) {
        instance = plugin;
    }

    public static GamePlugin<?> get() {
        if (instance == null) throw new IllegalStateException("PluginProvider has not been initialized.");
        return instance;
    }

    public static void unregister() {
        instance = null;
    }
}
