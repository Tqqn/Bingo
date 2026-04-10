package dev.tqqn.modules.game.framework.listeners;

import dev.tqqn.modules.database.framework.events.PlayerModelJoinEvent;
import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.GameStates;
import dev.tqqn.modules.game.framework.roles.Roles;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PlayerJoinListener implements Listener {

    private final GameModule gameModule;

    public PlayerJoinListener(GameModule gameModule) {
        this.gameModule = gameModule;
    }

    @EventHandler
    public void onJoin(PlayerModelJoinEvent event) {

    }
}
