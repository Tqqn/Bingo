package dev.tqqn.modules.perks;

import com.google.common.collect.ImmutableList;
import dev.tqqn.BingoMain;
import dev.tqqn.modules.AbstractModule;
import dev.tqqn.modules.perks.framework.AbstractPerk;
import dev.tqqn.modules.perks.framework.commands.PerkCommand;
import dev.tqqn.modules.perks.framework.listeners.PerkUseListener;
import dev.tqqn.modules.perks.framework.types.NoFallDamagePerk;
import dev.tqqn.modules.perks.framework.types.SpeedPerk;

import java.util.ArrayList;
import java.util.List;

public final class PerkModule extends AbstractModule {

    private final ArrayList<AbstractPerk> loadedPerks = new ArrayList<>();

    public PerkModule(BingoMain plugin) {
        super(plugin, "Perks");
    }

    @Override
    public void onEnable() {
        loadPerk(new NoFallDamagePerk());
        loadPerk(new SpeedPerk());
        register(new PerkUseListener(this));

        register(new PerkCommand(this));
    }

    private void loadPerk(AbstractPerk abstractPerk) {
        loadedPerks.add(abstractPerk);
    }

    public List<AbstractPerk> getPerks() {
        return ImmutableList.copyOf(loadedPerks);
    }
}
