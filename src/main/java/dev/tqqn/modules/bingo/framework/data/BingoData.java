package dev.tqqn.modules.bingo.framework.data;

import dev.tqqn.modules.perks.framework.AbstractPerk;
import lombok.Getter;

import java.util.UUID;

@Getter
public final class BingoData {

    private transient AbstractPerk selectedPerk;

    public void updatePerk(AbstractPerk abstractPerk, UUID player) {
        if (selectedPerk != null) selectedPerk.disablePlayer(player);
        selectedPerk = abstractPerk;
        selectedPerk.enablePlayer(player);
    }
}
