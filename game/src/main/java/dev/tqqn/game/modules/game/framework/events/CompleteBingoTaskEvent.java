package dev.tqqn.game.modules.game.framework.events;

import dev.tqqn.game.modules.game.framework.objects.BingoTask;
import dev.tqqn.game.modules.game.framework.team.GameTeam;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 03/06/2026
 */
public final class CompleteBingoTaskEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter private final BingoTask completedTask;
    @Getter private final Player player;
    @Getter private final GameTeam team;

    public CompleteBingoTaskEvent(Player player, BingoTask completedTask, GameTeam gameTeam) {
        this.completedTask = completedTask;
        this.player = player;
        this.team = gameTeam;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() { return handlers; }
}
