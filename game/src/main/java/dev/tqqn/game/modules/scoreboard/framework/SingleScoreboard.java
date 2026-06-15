package dev.tqqn.game.modules.scoreboard.framework;

import dev.tqqn.game.modules.database.player.BingoPlayerModel;
import fr.mrmicky.fastboard.adventure.FastBoard;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;

@Getter
public abstract class SingleScoreboard {

    private FastBoard fastBoard;
    private WeakReference<BingoPlayerModel> playerWeakReference;

    public SingleScoreboard(Player player) {
        final BingoPlayerModel playerModel = BingoPlayerModel.from(player);
        if (playerModel == null) return;
        this.playerWeakReference = new WeakReference<>(playerModel);
        fastBoard = new FastBoard(player);
    }

    public void update() {
        onUpdate();
    }

    public abstract void onUpdate();

    public void delete() {
        if (fastBoard.isDeleted()) return;
        fastBoard.delete();
        final BingoPlayerModel playerModel = playerWeakReference.get();
        if (playerModel == null) return;
        playerModel.getTempPlayerData().setScoreboard(null);
    }
}
