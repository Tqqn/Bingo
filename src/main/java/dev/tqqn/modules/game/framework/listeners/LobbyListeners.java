package dev.tqqn.modules.game.framework.listeners;

import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.states.LobbyState;
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
        if (!(gameModule.getCurrentInstance().getCurrentState() instanceof LobbyState)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!(gameModule.getCurrentInstance().getCurrentState() instanceof LobbyState)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {

    }
}
