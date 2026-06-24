package dev.tqqn.common.scoreboard.framework;

import dev.tqqn.common.database.AbstractDatabaseModule;
import dev.tqqn.common.database.framework.objects.BasePlayerModel;
import dev.tqqn.common.modular.PluginProvider;
import dev.tqqn.common.scoreboard.ScoreboardModule;
import fr.mrmicky.fastboard.adventure.FastBoard;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 24/06/2026
 */
@Getter
public abstract class SingleScoreboard {

    private final ScoreboardModule scoreboardModule;

    private final FastBoard fastBoard;
    private WeakReference<BasePlayerModel> playerModelWeakReference;

    public SingleScoreboard(Player player) {
        this.scoreboardModule = PluginProvider.get().getModuleManager().getModule(ScoreboardModule.class);
        this.fastBoard = new FastBoard(player);

        final BasePlayerModel basePlayerModel = scoreboardModule.getDatabaseModule().getPlayerModel(player);
        if (basePlayerModel == null) return;
        basePlayerModel.updateScoreboard(this);
        playerModelWeakReference = new WeakReference<>(basePlayerModel);
    }

    public void update() {
        onUpdate();
    }

    public abstract void onUpdate();

    public void delete() {
        if (fastBoard.isDeleted()) return;
        fastBoard.delete();
        final BasePlayerModel playerModel = playerModelWeakReference.get();
        if (playerModel == null) return;
        playerModel.cleanUpScoreboard();
    }
}
