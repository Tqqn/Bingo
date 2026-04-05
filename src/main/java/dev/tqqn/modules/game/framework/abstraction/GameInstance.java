package dev.tqqn.modules.game.framework.abstraction;

import dev.tqqn.modules.database.framework.events.PlayerModelJoinEvent;
import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.roles.Roles;
import dev.tqqn.modules.game.framework.states.GameStateSeries;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractState;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractStateSeries;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.world.GenericGameEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class GameInstance extends BukkitRunnable {

    private final int id;
    private final GameModule gameModule;
    private final Map<Player, Roles> inGamePlayers = new HashMap<>();

    public GameInstance(int id, GameModule gameModule) {
        this.id = id;
        this.gameModule = gameModule;
    }

    public abstract void start();

    public abstract void stop();

    public void addPlayer(Player player, Roles role) {
        inGamePlayers.put(player, role);
    }

    public int getInGameAlivePlayerCount() {
        return inGamePlayers.values().stream().filter(role -> role == Roles.ALIVE).toList().size();
    }

    public abstract WeakReference<AbstractState> getCurrentState();

    public abstract boolean canStart();

    public abstract void onPlayerJoin(PlayerModel playerModel, PlayerModelJoinEvent event);
}
