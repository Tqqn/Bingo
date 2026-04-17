package dev.tqqn.modules.database.listeners;

import dev.tqqn.modules.database.framework.events.PlayerModelQuitEvent;
import dev.tqqn.modules.database.framework.objects.PlayerModel;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final PlayerModel playerModel = PlayerModel.from(event.getPlayer());
        final PlayerModelQuitEvent playerModelQuitEvent = new PlayerModelQuitEvent(playerModel);
        Bukkit.getPluginManager().callEvent(playerModelQuitEvent);

        playerModel.save();

        if (playerModelQuitEvent.isRemoveFromCache()) playerModel.cleanUp();
    }

}
