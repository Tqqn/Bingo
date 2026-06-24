package dev.tqqn.lobby.modules.database.listeners;

import dev.tqqn.common.database.framework.events.PlayerModelJoinEvent;
import dev.tqqn.common.database.framework.events.PlayerModelPreJoinEvent;
import dev.tqqn.common.database.framework.objects.MongoDriver;
import dev.tqqn.common.database.listeners.AbstractPlayerLoadListener;
import dev.tqqn.common.utils.ChatUtils;
import dev.tqqn.lobby.modules.database.LobbyDatabaseModule;
import dev.tqqn.lobby.modules.database.objects.LobbyPlayerModel;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 12/06/2026
 */
public final class BingoLobbyPlayerLoadListener extends AbstractPlayerLoadListener<LobbyPlayerModel> {

    private final LobbyDatabaseModule databaseModule;

    public BingoLobbyPlayerLoadListener(LobbyDatabaseModule databaseModule, MongoDriver mongoDriver) {
        super(mongoDriver, databaseModule);
        this.databaseModule = databaseModule;
    }

    @Override
    public LobbyDatabaseModule getDatabaseModule() {
        return databaseModule;
    }

    @Override
    protected Class<LobbyPlayerModel> getModelClass() {
        return LobbyPlayerModel.class;
    }

    @Override
    protected LobbyPlayerModel createModel(UUID uuid, String name) {
        return new LobbyPlayerModel(uuid, name);
    }

    @Override
    protected boolean onPreJoin(LobbyPlayerModel model, PlayerLoginEvent event) {
        final PlayerModelPreJoinEvent preJoinEvent = new PlayerModelPreJoinEvent(model);
        Bukkit.getPluginManager().callEvent(preJoinEvent);

        if (preJoinEvent.isCancelled()) {
            final Component kickMessage = preJoinEvent.getKickMessage() != null
                    ? preJoinEvent.getKickMessage()
                    : ChatUtils.format("<red>Your login has been disallowed.");
            event.getPlayer().kick(kickMessage);
            return false;
        }

        return true;
    }

    @Override
    protected void onJoin(LobbyPlayerModel model, PlayerJoinEvent event) {
        event.joinMessage(Component.empty());

        if (!model.getName().equalsIgnoreCase(event.getPlayer().getName())) {
            model.setName(event.getPlayer().getName());
        }

        final PlayerModelJoinEvent joinEvent = new PlayerModelJoinEvent(model);
        Bukkit.getPluginManager().callEvent(joinEvent);
    }

    @Override
    protected void initialize(LobbyPlayerModel model) {
        model.initialize();
    }

    @Override
    protected void cacheModel(LobbyPlayerModel model) {
        LobbyPlayerModel.cache(model);
    }

    @Override
    protected boolean isPlayerCached(UUID uuid) {
        return LobbyPlayerModel.from(uuid) != null;
    }
}
