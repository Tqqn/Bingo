package dev.tqqn.common.database.listeners;

import dev.tqqn.common.database.AbstractDatabaseModule;
import dev.tqqn.common.database.framework.objects.BasePlayerModel;
import dev.tqqn.common.database.framework.objects.MongoDriver;
import dev.tqqn.common.utils.ChatUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 11/06/2026
 */

@RequiredArgsConstructor
@Getter
public abstract class AbstractPlayerLoadListener <M extends BasePlayerModel> implements Listener {

    private final ConcurrentHashMap<UUID, M> joiningPlayers = new ConcurrentHashMap<>();

    private final MongoDriver mongoDriver;
    private final AbstractDatabaseModule databaseModule;

    @EventHandler
    public final void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (isPlayerCached(event.getUniqueId())) return;

        M model = mongoDriver.read(getModelClass(), event.getUniqueId().toString());

        if (model == null) {
            databaseModule.getLogger().log(Level.INFO,
                    "Player '" + event.getName() + "' has no saved data. Creating new document...");
            model = createModel(event.getUniqueId(), event.getName());
            mongoDriver.save(model);
        }

        joiningPlayers.put(model.getKey(), model);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public final void onPlayerLogin(PlayerLoginEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();
        final M model = joiningPlayers.get(uuid);

        if (model == null) {
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.kickMessage(dataLoadFailedMessage());
            joiningPlayers.remove(uuid);
            return;
        }

        if (!onPreJoin(model, event)) {
            mongoDriver.saveAsync(model);
            joiningPlayers.remove(uuid);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public final void onPlayerJoin(PlayerJoinEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();
        final M model = joiningPlayers.remove(uuid);
        if (model == null) return;

        initialize(model);
        cacheModel(model);
        onJoin(model, event);
    }

    protected abstract AbstractDatabaseModule getDatabaseModule();
    protected abstract Class<M> getModelClass();
    protected abstract M createModel(UUID uuid, String name);
    protected abstract boolean onPreJoin(M model, PlayerLoginEvent event);
    protected abstract void onJoin(M model, PlayerJoinEvent event);
    protected abstract void initialize(M model);
    protected abstract void cacheModel(M model);
    protected abstract boolean isPlayerCached(UUID uuid);

    protected Component dataLoadFailedMessage() {
        return ChatUtils.format("<red>Your data could not be loaded. Please try again. If this persists, contact a staff member.");
    }
}

