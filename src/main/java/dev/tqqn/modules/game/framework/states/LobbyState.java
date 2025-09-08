package dev.tqqn.modules.game.framework.states;

import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.game.framework.GameInstance;
import dev.tqqn.modules.game.framework.GameStates;
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
    public void run() {
        if (getGameInstance().canStart()) {
            if (!isStartCountdown) {
                isStartCountdown = true;
            }

            if (timer < 31) {
                if (timer == 30) broadcast(String.format(message, timer));
                if (timer == 10) broadcast(String.format(message, timer));
                if (timer < 6) broadcast(String.format(message, timer));
                if (timer < 1) {
                    getGameInstance().changeState(GameStates.ACTIVE);
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
        this.runTaskTimer(getGameInstance().getGameModule().getPlugin(), 0L, 20L);
    }

    @Override
    public void onDisable() {
        this.cancel();
    }

    @Override
    public void applyScoreboard(Player player) {
        final PlayerModel playerModel = PlayerModel.from(player);
        if (playerModel == null) return;
        playerModel.getTempPlayerData().setScoreboard(new LobbyScoreboard(player, getGameInstance()));
    }

    private void broadcast(String message) {
        Bukkit.broadcast(ChatUtils.format(message));
    }
}
