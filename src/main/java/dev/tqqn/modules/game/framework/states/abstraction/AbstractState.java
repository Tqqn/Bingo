package dev.tqqn.modules.game.framework.states.abstraction;

import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.game.framework.abstraction.GameInstance;
import dev.tqqn.modules.game.framework.GameStates;
import dev.tqqn.modules.game.framework.data.TempPlayerData;
import dev.tqqn.modules.scoreboard.framework.SingleScoreboard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public abstract class AbstractState implements Listener {

    @Getter private final GameInstance gameInstance;
    @Getter private final GameStates gameState;
    @Getter private final String name;

    @Getter @Setter protected int timer;

    @Getter protected boolean freeze = false;

    private final Set<Listener> listeners;

    public AbstractState(GameInstance instance, GameStates gameState, String name) {
        this.gameInstance = instance;
        this.gameState = gameState;
        this.name = name;
        this.listeners = new HashSet<>();
    }

    public void onEnable() {}
    public void onDisable() {}

    public void tick() {
        if (freeze) return;
        onTick();
    }

    public abstract void onTick();

    public void enable() {
        gameInstance.getGameModule().getLogger().log(Level.INFO, "State: " + name + " is enabling...");
        onEnable();
        registerListeners();
        addScoreboardAllPlayers();
        gameInstance.getGameModule().getLogger().log(Level.INFO, "State: " + name + " finished enabling!");
    }

    public void disable() {
        gameInstance.getGameModule().getLogger().log(Level.INFO, "State: " + name + " is disabling...");
        onDisable();
        unRegisterListeners();
        gameInstance.getGameModule().getLogger().log(Level.INFO, "State: " + name + " finished disabling!");
    }

    public void registerListeners() {
        if (listeners.isEmpty()) return;
        final PluginManager manager = getGameInstance().getGameModule().getPlugin().getServer().getPluginManager();
        listeners.forEach(listener -> {
            manager.registerEvents(listener, getGameInstance().getGameModule().getPlugin());
            gameInstance.getGameModule().getLogger().log(Level.INFO, "State: " + name + " has registered listener: " + listener);
        });
    }

    public void unRegisterListeners() {
        if (listeners.isEmpty()) return;
        listeners.forEach(listener -> {
            HandlerList.unregisterAll(listener);
            gameInstance.getGameModule().getLogger().log(Level.INFO, "State: " + name + " has unregistered listener: " + listener);
        });
        listeners.clear();
    }

    public void register(Object object) {
        if (object instanceof Listener listener) {
            listeners.add(listener);
        }
    }

    private void addScoreboardAllPlayers() {
        for (Player player : getGameInstance().getInGamePlayers().keySet()) {
            setScoreboard(player);
        }
    }

    protected  <O extends SingleScoreboard> void applyScoreboard(O scoreboard, Player player) {
        final PlayerModel playerModel = PlayerModel.from(player);
        if (playerModel == null) return;
        playerModel.getTempPlayerData().setScoreboard(scoreboard);
    }

    protected void removeScoreboard(Class<? extends SingleScoreboard> scoreboardClass, Player player) {
        final PlayerModel playerModel = PlayerModel.from(player);
        if (playerModel == null) return;

        final TempPlayerData tempPlayerData = playerModel.getTempPlayerData();

        if (tempPlayerData.getScoreboard() == null) return;
        if (!tempPlayerData.getScoreboard().getClass().equals(scoreboardClass)) return;
        tempPlayerData.getScoreboard().delete();
        tempPlayerData.setScoreboard(null);
    }

    public abstract void setScoreboard(Player player);
}
