package dev.tqqn.modules.game.framework.listeners;

import dev.tqqn.modules.database.framework.events.PlayerModelJoinEvent;
import dev.tqqn.modules.database.framework.events.PlayerModelPreJoinEvent;
import dev.tqqn.modules.game.GameModule;
import dev.tqqn.utils.NMSUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PlayerJoinListener implements Listener {

    private final GameModule gameModule;

    public PlayerJoinListener(GameModule gameModule) {
        this.gameModule = gameModule;
    }

    @EventHandler
    public void onJoin(PlayerModelJoinEvent event) {
        gameModule.getCurrentInstance().onPlayerJoin(event.getPlayerModel(), event);
        event.getPlayerModel().getPlayer().ifPresent(NMSUtils::refreshTag);
    }

    @EventHandler
    public void onPreJoin(PlayerModelPreJoinEvent event) {
        gameModule.getCurrentInstance().onPlayerPreJoin(event.getPlayerModel(), event);
    }
}
