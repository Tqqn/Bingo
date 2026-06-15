package dev.tqqn.game.modules.database.listener;

import dev.tqqn.common.database.framework.events.PlayerModelQuitEvent;
import dev.tqqn.common.database.listeners.AbstractPlayerQuitListener;
import dev.tqqn.game.modules.database.player.BingoPlayerModel;
import dev.tqqn.game.modules.game.GameModule;
import dev.tqqn.game.modules.game.framework.GameStates;
import dev.tqqn.game.modules.game.framework.states.abstraction.AbstractState;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 12/06/2026
 */
public final class BingoPlayerQuitListener extends AbstractPlayerQuitListener<BingoPlayerModel> {

    private final GameModule gameModule;

    public BingoPlayerQuitListener(JavaPlugin plugin, GameModule gameModule) {
        super(plugin);
        this.gameModule = gameModule;
    }

    @Override
    protected BingoPlayerModel getFromCache(UUID uuid) {
        return BingoPlayerModel.from(uuid);
    }

    @Override
    protected void removeFromCache(UUID uuid) {
        BingoPlayerModel.from(uuid).cleanUp();
    }

    @Override
    protected void saveModel(BingoPlayerModel model) {
        model.save();
    }

    @Override
    protected void onQuit(BingoPlayerModel model, PlayerQuitEvent event) {
        // fire the custom quit event so other modules can react
        final PlayerModelQuitEvent quitEvent = new PlayerModelQuitEvent(model);
        Bukkit.getPluginManager().callEvent(quitEvent);

        // remove from the in-game player map
        model.getPlayer().ifPresent(player ->
                gameModule.getCurrentInstance().getInGamePlayers().remove(player));
    }

    @Override
    protected boolean shouldRemoveFromCache(BingoPlayerModel model, PlayerQuitEvent event) {
        final AbstractState currentState = gameModule.getCurrentInstance().getCurrentState().get();
        if (currentState == null) return false;
        final GameStates gameState = currentState.getGameState();

        return gameState == GameStates.LOBBY || gameState == GameStates.END;
    }
}
