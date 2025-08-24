package dev.tqqn.modules.game.framework.data;

import dev.tqqn.modules.game.framework.roles.Roles;
import dev.tqqn.modules.perks.framework.AbstractPerk;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public final class TempPlayerData {

    private final UUID uuid;
    private transient AbstractPerk selectedPerk;

    @Setter private Roles role;

    public TempPlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public void updatePerk(AbstractPerk abstractPerk, UUID player) {
        if (selectedPerk != null) selectedPerk.disablePlayer(player);
        selectedPerk = abstractPerk;
        selectedPerk.enablePlayer(player);
    }

    public void hasBingo() {

    }

}
