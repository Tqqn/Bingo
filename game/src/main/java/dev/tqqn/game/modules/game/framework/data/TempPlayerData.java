package dev.tqqn.game.modules.game.framework.data;

import dev.tqqn.common.scoreboard.framework.SingleScoreboard;
import dev.tqqn.game.modules.game.framework.roles.Roles;
import dev.tqqn.game.modules.game.framework.team.GameTeam;
import dev.tqqn.game.modules.perks.framework.AbstractPerk;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public final class TempPlayerData {

    @Getter private final UUID uuid;
    @Getter @Setter private AbstractPerk selectedPerk;
    @Getter @Setter private SingleScoreboard scoreboard;
    @Getter @Setter private GameTeam team = null;

    @Setter private Roles role;

    public TempPlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public void updatePerk(AbstractPerk abstractPerk) {
        final AbstractPerk oldPerk = selectedPerk;
        if (oldPerk != null) oldPerk.disablePlayer(uuid);
        selectedPerk = abstractPerk;
    }
}
