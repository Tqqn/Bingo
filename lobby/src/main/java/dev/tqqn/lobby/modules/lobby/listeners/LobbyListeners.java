package dev.tqqn.lobby.modules.lobby.listeners;

import dev.tqqn.common.database.framework.events.PlayerModelJoinEvent;
import dev.tqqn.lobby.modules.database.objects.LobbyPlayerModel;
import dev.tqqn.lobby.modules.scoreboard.LobbyScoreboard;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 24/06/2026
 */

@RequiredArgsConstructor
public final class LobbyListeners implements Listener {

    @EventHandler
    public void onJoin(PlayerModelJoinEvent event) {
        if (!(event.getPlayerModel() instanceof LobbyPlayerModel lobbyPlayerModel)) return;
        final Optional<Player> playerOptional = lobbyPlayerModel.getPlayer();
        if (playerOptional.isEmpty()) return;
        lobbyPlayerModel.updateScoreboard(new LobbyScoreboard(playerOptional.get()));
    }

}
