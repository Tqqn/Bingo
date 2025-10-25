package dev.tqqn.modules.game.framework.states.lobby;

import dev.tqqn.modules.game.framework.abstraction.GameInstance;
import dev.tqqn.modules.game.framework.GameStates;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractState;
import dev.tqqn.modules.game.framework.states.lobby.listeners.LobbyListeners;
import dev.tqqn.modules.scoreboard.boards.LobbyScoreboard;
import dev.tqqn.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class LobbyState extends AbstractState {

    private boolean isStartCountdown = false;
    private static final String message = "<red>Game is starting in <white><bold>%s<reset><red>s.";

    public LobbyState(GameInstance instance) {
        super(instance, GameStates.LOBBY, "Lobby");
        setTimer(10000);
    }

    @Override
    public void onTick() {
        if (getGameInstance().canStart()) {
            if (!isStartCountdown) {
                isStartCountdown = true;
            }

            if (timer < 31) {
                if (timer == 30) broadcast(String.format(message, timer));
                if (timer == 10) broadcast(String.format(message, timer));
                if (timer < 6) broadcast(String.format(message, timer));
                if (timer < 1) {
                    getGameInstance().getGameStateSeries().nextState();
                    return;
                }
            }

        } else if (isStartCountdown) {
            isStartCountdown = false;
            setTimer(10000);
        }

        timer--;
    }

    @Override
    public void onEnable() {
        register(new LobbyListeners(getGameInstance().getGameModule()));
    }

    @Override
    public void onDisable() {
        for (Player player : getGameInstance().getInGamePlayers().keySet()) {
            removeScoreboard(LobbyScoreboard.class, player);
        }
    }

    @Override
    public void setScoreboard(Player player) {
        applyScoreboard(new LobbyScoreboard(player, getGameInstance()), player);
    }

    private void broadcast(String message) {
        Bukkit.broadcast(ChatUtils.format(message));
    }
}
