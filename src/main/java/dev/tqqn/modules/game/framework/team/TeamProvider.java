package dev.tqqn.modules.game.framework.team;

import dev.tqqn.modules.database.framework.objects.PlayerModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 04/04/2026
 */
public final class TeamProvider {

    private final List<TeamData> availableTeams = new ArrayList<>();

    private final List<GameTeam> assignedTeams = new ArrayList<>();

    public TeamProvider(int howManyTeams) {
        final List<TeamType> teamTypes = new ArrayList<>(EnumSet.allOf(TeamType.class));
        Collections.shuffle(teamTypes);
        for (int i = 1; i < howManyTeams + 1; i++) {
            if (teamTypes.isEmpty()) continue;
            availableTeams.add(new TeamData(teamTypes.getFirst(), i));
        }
    }

    public GameTeam getFreeTeam() {
        final TeamData gottenTeam = availableTeams.removeFirst();
        if (gottenTeam == null) return null;
        final GameTeam team = new GameTeam(gottenTeam);
        assignedTeams.add(team);
        return team;
    }

    public boolean areTeamsAvailable() {
        return !availableTeams.isEmpty();
    }

    public List<GameTeam> getAssignedTeams() {
        return Collections.unmodifiableList(assignedTeams);
    }

    public void assignTeam(PlayerModel playerModel) {
        final GameTeam gameTeam = getFreeTeam();
        if (gameTeam == null) return;
        playerModel.getTempPlayerData().setTeam(gameTeam);
    }

    @Getter
    @RequiredArgsConstructor
    public class TeamData {
        private final TeamType teamType;
        private final int mapPlace;
    }

    @Getter
    public enum TeamType {
        RED("RED", "<red>", Color.RED, java.awt.Color.RED),
        BLUE("BLUE", "<blue>", Color.BLUE, java.awt.Color.BLUE),
        GREEN("GREEN", "<green>", Color.GREEN, java.awt.Color.GREEN),
        YELLOW("YELLOW", "<yellow>", Color.YELLOW, java.awt.Color.YELLOW),
        DARK_PURPLE("PURPLE", "<dark_purple>", Color.PURPLE, java.awt.Color.MAGENTA),
        GOLD("ORANGE", "<gold>", Color.ORANGE, java.awt.Color.ORANGE);

        private final String name;
        private final String prefixColor;
        private final Color teamColor;
        private final java.awt.Color mapColor;

        TeamType(String name, String prefixColor, Color teamColor, java.awt.Color mapColor) {
            this.name = name;
            this.prefixColor = prefixColor;
            this.teamColor = teamColor;
            this.mapColor = mapColor;
        }
    }
}
