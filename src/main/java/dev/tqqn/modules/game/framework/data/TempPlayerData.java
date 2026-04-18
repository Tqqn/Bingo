package dev.tqqn.modules.game.framework.data;

import dev.tqqn.BingoMain;
import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.GameStates;
import dev.tqqn.modules.game.framework.objects.BingoTask;
import dev.tqqn.modules.game.framework.roles.Roles;
import dev.tqqn.modules.game.framework.states.GameStateSeries;
import dev.tqqn.modules.game.framework.team.GameTeam;
import dev.tqqn.modules.perks.framework.AbstractPerk;
import dev.tqqn.modules.scoreboard.framework.SingleScoreboard;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class TempPlayerData {

    @Getter private final UUID uuid;
    @Getter @Setter private AbstractPerk selectedPerk;
    @Getter @Setter private SingleScoreboard scoreboard;
    @Getter @Setter private GameTeam team = null;

    private final List<BingoTask> completedTasks;

    @Setter private Roles role;

    public TempPlayerData(UUID uuid) {
        this.uuid = uuid;
        this.completedTasks = new ArrayList<>();
    }

    public void updatePerk(AbstractPerk abstractPerk) {
        final AbstractPerk oldPerk = selectedPerk;
        if (oldPerk != null) oldPerk.disablePlayer(uuid);
        selectedPerk = abstractPerk;
    }


    public void completeTask(BingoTask task) {
        completedTasks.add(task);
    }

    public void removeCompletedTask(BingoTask task) {
        completedTasks.remove(task);
    }

    public boolean hasCompleted(BingoTask task) {
        return completedTasks.contains(task);
    }
}
