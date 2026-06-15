package dev.tqqn.common.database.framework.events;

import dev.tqqn.common.database.framework.objects.BasePlayerModel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerModelQuitEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final BasePlayerModel playerModel;

    @Setter @Getter
    private boolean removeFromCache = true;

    public PlayerModelQuitEvent(BasePlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() { return handlers; }
}
