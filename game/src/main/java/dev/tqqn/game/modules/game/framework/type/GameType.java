package dev.tqqn.game.modules.game.framework.type;

import lombok.Getter;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 20/05/2026
 */
@Getter
public enum GameType {

    SOLO("Solo"),
    TEAM("Team");

    private final String displayName;

    GameType(String displayName) {
        this.displayName = displayName;
    }

}
