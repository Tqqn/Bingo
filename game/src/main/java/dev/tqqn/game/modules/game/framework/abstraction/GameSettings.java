package dev.tqqn.game.modules.game.framework.abstraction;

import dev.tqqn.game.modules.game.framework.type.GameType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 20/05/2026
 */
@Getter
@Setter
public class GameSettings {

    private int minGamePlayersToStart = 1;
    private int maxGamePlayers = 4;
    private int playersPerTeam = 1;

    private GameType gameType = GameType.SOLO;
}
