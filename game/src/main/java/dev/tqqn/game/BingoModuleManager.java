package dev.tqqn.game;

import dev.tqqn.common.menu.MenuModule;
import dev.tqqn.common.modular.ModuleManager;
import dev.tqqn.common.retriever.RetrieverModule;
import dev.tqqn.common.scoreboard.ScoreboardModule;
import dev.tqqn.game.modules.database.GameDatabaseModule;
import dev.tqqn.game.modules.game.GameModule;
import dev.tqqn.game.modules.perks.PerkModule;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 11/06/2026
 */
public class BingoModuleManager extends ModuleManager<BingoMain> {

    public BingoModuleManager(BingoMain plugin) {
        super(plugin);

        final GameDatabaseModule databaseModule = new GameDatabaseModule(this);
        addModule(databaseModule);

        addModule(new MenuModule(this));
        addModule(new RetrieverModule(this));
        addModule(new GameModule(this, databaseModule));
        addModule(new ScoreboardModule(this, databaseModule));
        addModule(new PerkModule(this));
    }
}
