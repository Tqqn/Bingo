package dev.tqqn.modules.game.framework.states.lobby.listeners;

import dev.tqqn.modules.database.framework.events.PlayerModelJoinEvent;
import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.states.lobby.LobbyState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class LobbyListeners implements Listener {

    private final GameModule gameModule;

    public LobbyListeners(GameModule gameModule) {
        this.gameModule = gameModule;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerModelJoinEvent event) {
        gameModule.getCurrentInstance().onPlayerJoin(event.getPlayerModel(), event);
    }
}
