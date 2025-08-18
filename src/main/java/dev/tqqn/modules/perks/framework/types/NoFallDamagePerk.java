package dev.tqqn.modules.perks.framework.types;

import dev.tqqn.modules.perks.framework.AbstractPerk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public final class NoFallDamagePerk extends AbstractPerk {

    public NoFallDamagePerk() {
        super("<red>No-Fall-Damage", new String[]{"<red>Take no fall damage for this bingo game."}, new ItemStack(Material.TIPPED_ARROW), 10);
    }

    @Override
    public void onUse(Event event) {
        if (!(event instanceof EntityDamageEvent damageEvent)) return;
        if (!(damageEvent.getEntity() instanceof Player player)) return;
        if (damageEvent.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (!isPlayerEnabled(player.getUniqueId())) return;
        damageEvent.setCancelled(true);
    }
}
