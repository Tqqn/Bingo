package dev.tqqn.modules.game.framework.types;

import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.abstraction.GameInstance;
import dev.tqqn.modules.game.framework.roles.Roles;

public final class BingoGame extends GameInstance {

    public BingoGame(int id, GameModule gameModule) {
        super(id, gameModule);
    }

    @Override
    public boolean canStart() {
        return getInGamePlayers().values().stream().filter(role -> role == Roles.ALIVE).toList().size() == 2;
        //return currentPlayers.size() >= GameModule.GAME_MIN_PLAYERS_TO_START;
    }
}
