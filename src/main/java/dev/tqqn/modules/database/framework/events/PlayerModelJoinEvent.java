package dev.tqqn.modules.database.framework.events;

import dev.tqqn.modules.database.framework.objects.PlayerModel;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class PlayerModelJoinEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final PlayerModel playerModel;

    private boolean cancelled = false;

    public PlayerModelJoinEvent(PlayerModel playerModel) {
        this.playerModel = playerModel;
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() { return handlers; }
}