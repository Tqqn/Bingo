package dev.tqqn.game.modules.database.framework.events;

import dev.tqqn.game.modules.database.framework.objects.PlayerModel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public final class PlayerModelJoinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    @NotNull
    private final PlayerModel playerModel;

    @Setter
    @Getter
    @Nullable
    private Component kickMessage = null;

    public PlayerModelJoinEvent(@NonNull PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() { return handlers; }
}