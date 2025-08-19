package dev.tqqn.modules.perks.framework.types;

import dev.tqqn.modules.perks.framework.AbstractPerk;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class SpeedPerk extends AbstractPerk {

    public SpeedPerk() {
        super("<aqua>Speed II Perk", new String[]{"<red>Gain permanent Speed II."}, new ItemStack(Material.SUGAR), 20);
    }

    @Override
    public void enablePlayer(UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999999, 2, true, true));
        }
        super.enablePlayer(uuid);
    }

    @Override
    public void disablePlayer(UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            player.removePotionEffect(PotionEffectType.SPEED);
        }

        super.disablePlayer(uuid);
    }

    @Override
    public void onUse(Event event) {}
}
