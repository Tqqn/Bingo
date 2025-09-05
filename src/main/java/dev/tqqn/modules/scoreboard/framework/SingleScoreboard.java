package dev.tqqn.modules.scoreboard.framework;

import dev.tqqn.modules.database.framework.objects.PlayerModel;
import fr.mrmicky.fastboard.adventure.FastBoard;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;

@Getter
public abstract class SingleScoreboard {

    private FastBoard fastBoard;
    private WeakReference<PlayerModel> playerWeakReference;

    public SingleScoreboard(Player player) {
        final PlayerModel playerModel = PlayerModel.from(player);
        if (playerModel == null) return;
        this.playerWeakReference = new WeakReference<>(playerModel);
        fastBoard = new FastBoard(player);
    }

    public abstract void update();

}
