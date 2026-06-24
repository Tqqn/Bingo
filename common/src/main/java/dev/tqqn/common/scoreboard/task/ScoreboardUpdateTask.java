package dev.tqqn.common.scoreboard.task;

import dev.tqqn.common.database.AbstractDatabaseModule;
import dev.tqqn.common.database.framework.objects.BasePlayerModel;
import dev.tqqn.common.scoreboard.framework.SingleScoreboard;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 24/06/2026
 */

@RequiredArgsConstructor
public final class ScoreboardUpdateTask extends BukkitRunnable {

    private final AbstractDatabaseModule databaseModule;

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            final BasePlayerModel playerModel = databaseModule.getPlayerModel(player);
            if (playerModel == null) continue;

            final SingleScoreboard scoreboard = playerModel.getPlayerScoreboard();
            if (scoreboard == null) continue;

            scoreboard.update();
        }
    }

}
