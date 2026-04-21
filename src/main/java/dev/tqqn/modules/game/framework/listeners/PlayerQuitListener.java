package dev.tqqn.modules.game.framework.listeners;

import dev.tqqn.modules.database.framework.events.PlayerModelQuitEvent;
import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.GameStates;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public final class PlayerQuitListener implements Listener {

    private final GameModule gameModule;

    @EventHandler
    public void onQuit(PlayerModelQuitEvent event) {
        if (gameModule.getCurrentInstance().getCurrentState().get().getGameState() == GameStates.LOBBY) {
            gameModule.getCurrentInstance().getInGamePlayers().remove(event.getPlayerModel().getPlayer().isPresent() ? event.getPlayerModel().getPlayer().get() : null);
            return;
        }
        event.setRemoveFromCache(false);
    }
}
