package dev.tqqn.modules.database.framework.events;

import dev.tqqn.modules.database.framework.objects.PlayerModel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerModelPreJoinEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final PlayerModel playerModel;

    private boolean cancelled = false;

    @Setter
    @Getter
    @Nullable
    private Component kickMessage = null;

    public PlayerModelPreJoinEvent(PlayerModel playerModel) {
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
