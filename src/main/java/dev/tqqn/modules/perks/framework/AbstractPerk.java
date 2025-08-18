package dev.tqqn.modules.perks.framework;

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
    private final ItemStack icon;
    private final int cost;

    @Setter private boolean isGlobalEnabled = true;

    public AbstractPerk(String prettyName, String[] description, ItemStack icon, int cost) {
        this.prettyName = prettyName;
        this.description = description;
        this.icon = icon;
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

    public abstract void onUse(Event event);
}
