package dev.tqqn.modules.game.framework.states;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.game.framework.abstraction.GameInstance;
import dev.tqqn.modules.game.framework.objects.BingoPlacement;
import dev.tqqn.modules.game.framework.objects.BingoProgress;
import dev.tqqn.modules.game.framework.objects.BingoTask;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractStateSeries;
import dev.tqqn.modules.game.framework.states.active.ActiveState;
import dev.tqqn.modules.game.framework.states.end.EndState;
import dev.tqqn.modules.game.framework.states.lobby.LobbyState;
import dev.tqqn.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class GameStateSeries extends AbstractStateSeries {

    private BingoProgress bingoProgress;

    public GameStateSeries(GameInstance instance) {
        super(instance);
        registerStates(List.of(new LobbyState(instance), new ActiveState(instance), new EndState(instance)));
    }

    @Override
    public void onEnable() {
        this.bingoProgress = new BingoProgress(getInstance().getGameModule().getAvailableTasks());
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

    public boolean hasBingo(PlayerModel playerModel) {
        return bingoProgress.hasBingo(playerModel);
    }

    public List<BingoTask> getBingoTasks() {
        return ImmutableList.copyOf(bingoProgress.getTasks().keySet());
    }

    public Map<BingoTask, BingoPlacement> getBingoPlacements() {
        return ImmutableMap.copyOf(bingoProgress.getTasks());
    }
}
