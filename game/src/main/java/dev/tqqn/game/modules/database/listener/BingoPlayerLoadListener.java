package dev.tqqn.game.modules.database.listener;

import dev.tqqn.common.database.framework.events.PlayerModelJoinEvent;
import dev.tqqn.common.database.framework.events.PlayerModelPreJoinEvent;
import dev.tqqn.common.database.framework.objects.MongoDriver;
import dev.tqqn.common.database.listeners.AbstractPlayerLoadListener;
import dev.tqqn.common.utils.ChatUtils;
import dev.tqqn.game.modules.database.GameDatabaseModule;
import dev.tqqn.game.modules.database.player.BingoPlayerModel;
import dev.tqqn.game.modules.game.GameModule;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 12/06/2026
 */
public final class BingoPlayerLoadListener extends AbstractPlayerLoadListener<BingoPlayerModel> {

    private final GameDatabaseModule databaseModule;

    public BingoPlayerLoadListener(GameDatabaseModule databaseModule, MongoDriver mongoDriver) {
        super(mongoDriver, databaseModule);
        this.databaseModule = databaseModule;
    }

    @Override
    public GameDatabaseModule getDatabaseModule() {
        return databaseModule;
    }

    @Override
    protected Class<BingoPlayerModel> getModelClass() {
        return BingoPlayerModel.class;
    }

    @Override
    protected BingoPlayerModel createModel(UUID uuid, String name) {
        return new BingoPlayerModel(uuid, name);
    }

    @Override
    protected boolean onPreJoin(BingoPlayerModel model, PlayerLoginEvent event) {
        final PlayerModelPreJoinEvent preJoinEvent = new PlayerModelPreJoinEvent(model);
        Bukkit.getPluginManager().callEvent(preJoinEvent);

        if (preJoinEvent.isCancelled()) {
            final Component kickMessage = preJoinEvent.getKickMessage() != null
                    ? preJoinEvent.getKickMessage()
                    : ChatUtils.format("<red>Your login has been disallowed.");
            event.getPlayer().kick(kickMessage);
            return false;
        }

        if (!getDatabaseModule().getModuleManager().getModule(GameModule.class).getArena().isReadyToJoin()) {
            event.getPlayer().kick(ChatUtils.format(
                    "<red>The game is not ready yet. Please try again in a moment."));
            return false;
        }

        return true;
    }

    @Override
    protected void onJoin(BingoPlayerModel model, PlayerJoinEvent event) {
        event.joinMessage(Component.empty());

        if (!model.getName().equalsIgnoreCase(event.getPlayer().getName())) {
            model.setName(event.getPlayer().getName());
        }

        final PlayerModelJoinEvent joinEvent = new PlayerModelJoinEvent(model);
        Bukkit.getPluginManager().callEvent(joinEvent);
    }

    @Override
    protected void initialize(BingoPlayerModel model) {
        model.initialize();
    }

    @Override
    protected void cacheModel(BingoPlayerModel model) {
        BingoPlayerModel.cache(model);
    }

    @Override
    protected boolean isPlayerCached(UUID uuid) {
        return BingoPlayerModel.from(uuid) != null;
    }
}
