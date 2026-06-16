package dev.tqqn.game.modules.game.framework.states;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import dev.tqqn.game.modules.database.player.BingoPlayerModel;
import dev.tqqn.game.modules.game.GameModule;
import dev.tqqn.game.modules.game.framework.objects.BingoPlacement;
import dev.tqqn.game.modules.game.framework.objects.BingoProgress;
import dev.tqqn.game.modules.game.framework.objects.BingoTask;
import dev.tqqn.game.modules.game.framework.roles.Roles;
import dev.tqqn.game.modules.game.framework.states.abstraction.AbstractStateSeries;
import dev.tqqn.game.modules.game.framework.states.active.ActiveState;
import dev.tqqn.game.modules.game.framework.states.end.EndState;
import dev.tqqn.game.modules.game.framework.states.lobby.LobbyState;
import dev.tqqn.game.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class GameStateSeries extends AbstractStateSeries {

    private BingoProgress bingoProgress;

    public GameStateSeries(GameModule module, int instanceId) {
        super(instanceId, module);
        registerStates(List.of(new LobbyState(this), new ActiveState(this), new EndState(this)));
    }

    @Override
    public void onEnable() {
        this.bingoProgress = new BingoProgress(getGameModule().getAvailableTasks());
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onFreeze() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("bingo.admin")) {
                player.sendMessage(ChatUtils.format("<aqua>The game has been " + (getCurrentState().get().isFreeze() ? "<red>frozen" : "<green>unfrozen") + "<aqua>."));
            }
        }
    }

    public boolean hasBingo(BingoPlayerModel playerModel) {
        return bingoProgress.hasBingo(playerModel);
    }

    public List<BingoTask> getBingoTasks() {
        return ImmutableList.copyOf(bingoProgress.getTasks().keySet());
    }

    public Map<BingoTask, BingoPlacement> getBingoPlacements() {
        return ImmutableMap.copyOf(bingoProgress.getTasks());
    }

    @Override
    public void start() {
        enable();
    }

    @Override
    public void stop() {
        disable();
    }

    @Override
    public boolean canStart() {
        if (getCurrentState().get() instanceof LobbyState lobbyState) {
            if (lobbyState.isForceStart()) {
                return true;
            }
        }
        return getInGamePlayers().values().stream().filter(role -> role == Roles.ALIVE).toList().size() >= getGameSettings().getMinGamePlayersToStart();
    }
}
