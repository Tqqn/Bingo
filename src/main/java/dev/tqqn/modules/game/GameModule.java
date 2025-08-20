package dev.tqqn.modules.game;

import dev.tqqn.BingoMain;
import dev.tqqn.modules.AbstractModule;
import dev.tqqn.modules.game.framework.GameInstance;

import java.util.HashMap;
import java.util.Map;

public final class GameModule extends AbstractModule {

    private Map<String, GameInstance> gameInstances = new HashMap<>();

    public GameModule(BingoMain plugin) {
        super(plugin, "Game");
    }
}
