package dev.tqqn.common.database.listeners;

import dev.tqqn.common.database.framework.objects.BasePlayerModel;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 12/06/2026
 */
@RequiredArgsConstructor
public abstract class AbstractPlayerQuitListener<M extends BasePlayerModel> implements Listener {

    private final JavaPlugin plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    public final void onPlayerQuit(PlayerQuitEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();
        final M model = getFromCache(uuid);

        if (model == null) {
            plugin.getLogger().warning(
                    "PlayerQuitEvent fired for " + event.getPlayer().getName()
                            + " but no model found in cache. Skipping save.");
            return;
        }

        onQuit(model, event);

        if (shouldSave(model, event)) {
            saveModel(model);
        }

        if (shouldRemoveFromCache(model, event)) {
            removeFromCache(uuid);
        }
    }

    protected abstract M getFromCache(UUID uuid);
    protected abstract void removeFromCache(UUID uuid);
    protected abstract void saveModel(M model);

    protected abstract void onQuit(M model, PlayerQuitEvent event);

    protected boolean shouldSave(M model, PlayerQuitEvent event) {
        return true;
    }

    protected boolean shouldRemoveFromCache(M model, PlayerQuitEvent event) {
        return true;
    }
}
