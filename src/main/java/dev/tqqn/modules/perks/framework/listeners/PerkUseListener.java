package dev.tqqn.modules.perks.framework.listeners;

import dev.tqqn.modules.perks.PerkModule;
import dev.tqqn.modules.perks.framework.AbstractPerk;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public final class PerkUseListener implements Listener {

    private final PerkModule perkModule;
    @Setter private boolean arePerksAllowed = true;

    public PerkUseListener(PerkModule perkModule) {
        this.perkModule = perkModule;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        usePerk(event);
    }

    private void usePerk(Event event) {
        if (!arePerksAllowed) return;

        for (AbstractPerk perk : perkModule.getPerks()) {
            if (!perk.isGlobalEnabled()) continue;
            perk.onUse(event);
        }
    }

}
