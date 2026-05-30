package dev.tqqn.game.modules.game.framework.team;

import dev.tqqn.game.modules.database.framework.objects.PlayerModel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 02/04/2026
 */

@Getter
public final class GameTeam {

    private final TeamProvider.TeamData data;
    private final List<PlayerModel> players = new ArrayList<>();
    private final int mapPlace;

    public GameTeam(TeamProvider.TeamData data) {
        this.data = data;
        this.mapPlace = data.mapPlace();
    }

    public boolean isFull(int maxPlayers) {
        return players.size() >= maxPlayers;
    }
}
