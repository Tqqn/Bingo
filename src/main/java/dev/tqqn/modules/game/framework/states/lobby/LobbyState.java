package dev.tqqn.modules.game.framework.states.lobby;

import dev.tqqn.modules.database.framework.events.PlayerModelJoinEvent;
import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.GameStates;
import dev.tqqn.modules.game.framework.states.GameStateSeries;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractState;
import dev.tqqn.modules.game.framework.states.lobby.listeners.LobbyListeners;
import dev.tqqn.modules.scoreboard.boards.LobbyScoreboard;
import dev.tqqn.utils.ChatUtils;
import dev.tqqn.utils.NMSUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public final class LobbyState extends AbstractState {

    private boolean isStartCountdown = false;
    private static final String message = "<red>Game is starting in <white><bold>%s<reset><red>s.";

    public LobbyState(GameStateSeries instance) {
        super(instance, GameStates.LOBBY, "Lobby", true);
        setTimer(10000);
    }

    @Override
    public void onTick() {
        if (getGameInstance().canStart()) {
            if (!isStartCountdown) {
                setTimer(240);
                isStartCountdown = true;
            }

            if (timer < 31) {
                if (timer == 30 || timer == 10 || timer < 6) broadcastWithSound(String.format(message, timer), Sound.BLOCK_NOTE_BLOCK_PLING);
            }

        } else if (isStartCountdown) {
            isStartCountdown = false;
            setTimer(10000);
        }
    }

    @Override
    public void onTimerEnd() {
        getGameInstance().nextState();
    }

    @Override
    public void onEnable() {
        register(new LobbyListeners(getGameInstance().getGameModule()));
    }

    @Override
    public void onDisable() {
        getGameInstance().getGameModule().putPlayersInTeams();
        for (Player player : getGameInstance().getInGamePlayers().keySet()) {
            removeScoreboard(LobbyScoreboard.class, player);
            NMSUtils.refreshTag(player);
        }
    }

    @Override
    public void onPlayerJoin(PlayerModel playerModel, PlayerModelJoinEvent event) {
        broadcast("<yellow>[<aqua>" + (getGameInstance().getInGamePlayers().size()+1) + "<yellow>/<aqua>" + GameModule.GAME_MAX_PLAYERS + "<yellow>] <green>+ " + playerModel.getName());
    }

    @Override
    public void setScoreboard(Player player) {
        applyScoreboard(new LobbyScoreboard(player, getGameInstance()), player);
    }

    public void quickStart() {
        setTimer(11);
    }
}
