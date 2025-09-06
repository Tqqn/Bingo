package dev.tqqn.modules.scoreboard.task;

import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.scoreboard.framework.SingleScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class ScoreboardUpdateTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            final PlayerModel playerModel = PlayerModel.from(player);
            if (playerModel == null) continue;

            final SingleScoreboard scoreboard = playerModel.getTempPlayerData().getScoreboard();
            if (scoreboard == null) continue;

            scoreboard.update();
        }
    }
}
