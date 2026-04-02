package dev.tqqn.modules.game.framework.team;

import dev.tqqn.modules.database.framework.objects.PlayerModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 02/04/2026
 */

@RequiredArgsConstructor
@Getter
public final class GameTeam {

    private final Color teamColor;
    private final String teamName;
    private final List<PlayerModel> players = new ArrayList<>();
    private final int mapPlace;

}
