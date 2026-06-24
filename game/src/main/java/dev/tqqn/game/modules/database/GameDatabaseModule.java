package dev.tqqn.game.modules.database;

import dev.tqqn.common.database.AbstractDatabaseModule;
import dev.tqqn.common.database.framework.objects.DefaultConfig;
import dev.tqqn.game.BingoModuleManager;
import dev.tqqn.game.modules.database.listener.BingoGamePlayerLoadListener;
import dev.tqqn.game.modules.database.listener.BingoGamePlayerQuitListener;
import dev.tqqn.game.modules.database.player.BingoPlayerModel;
import dev.tqqn.game.modules.game.GameModule;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 11/06/2026
 */

@Getter
public final class GameDatabaseModule extends AbstractDatabaseModule {

    private final DefaultConfig defaultConfig;
    private final BingoTaskConfig bingoTaskConfig;

    public GameDatabaseModule(BingoModuleManager bingoModuleManager) {
        super(bingoModuleManager);
        this.defaultConfig = DefaultConfig.getInstance(this);
        this.bingoTaskConfig = new BingoTaskConfig(this);
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        register(new BingoGamePlayerLoadListener(this, getMongoDriver()));
        register(new BingoGamePlayerQuitListener(getModuleManager().getPlugin(), getModuleManager().getModule(GameModule.class)));
    }

    @Override
    public BingoPlayerModel getPlayerModel(Player player) {
        return BingoPlayerModel.from(player);
    }
}
