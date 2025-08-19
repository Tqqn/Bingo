package dev.tqqn.modules.perks.framework;

import dev.tqqn.utils.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class AbstractPerk implements Listener {

    private final List<UUID> enabledPlayers = new ArrayList<>();

    private final String prettyName;
    private final String[] description;
    private final ItemStack iconItem;
    private final int cost;

    @Setter private boolean isGlobalEnabled = true;

    public AbstractPerk(String prettyName, String[] description, ItemStack iconItem, int cost) {
        this.prettyName = prettyName;
        this.description = description;
        this.iconItem = iconItem;
        this.cost = cost;
    }

    public void enablePlayer(UUID uuid) {
        this.enabledPlayers.add(uuid);
    }

    public void disablePlayer(UUID uuid) {
        this.enabledPlayers.remove(uuid);
    }

    public boolean isPlayerEnabled(UUID uuid) {
        return enabledPlayers.contains(uuid);
    }

    public ItemStack getIcon() {
        final ItemBuilder itemBuilder = ItemBuilder.getBuilder(iconItem)
                .setDisplayName(prettyName)
                .setLore(description);

        return itemBuilder.build();
    }

    public abstract void onUse(Event event);
}
