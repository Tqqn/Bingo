package dev.tqqn.modules.game.framework.data;

import dev.tqqn.modules.game.framework.objects.BingoTask;
import dev.tqqn.modules.game.framework.roles.Roles;
import dev.tqqn.modules.perks.framework.AbstractPerk;
import dev.tqqn.modules.scoreboard.framework.SingleScoreboard;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class TempPlayerData {

    @Getter private final UUID uuid;
    @Getter private transient AbstractPerk selectedPerk;
    @Getter @Setter private transient SingleScoreboard scoreboard;

    private final List<BingoTask> completedTasks;

    @Setter private Roles role;

    public TempPlayerData(UUID uuid) {
        this.uuid = uuid;
        this.completedTasks = new ArrayList<>();
    }

    public void updatePerk(AbstractPerk abstractPerk, UUID player) {
        if (selectedPerk != null) selectedPerk.disablePlayer(player);
        selectedPerk = abstractPerk;
        selectedPerk.enablePlayer(player);
    }

    public void completeTask(BingoTask task) {
        completedTasks.add(task);
    }

    public void removeCompletedTask(BingoTask task) {
        completedTasks.remove(task);
    }
}
