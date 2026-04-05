package dev.tqqn.modules.scoreboard.boards;

import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.abstraction.GameInstance;
import dev.tqqn.modules.scoreboard.framework.SingleScoreboard;
import dev.tqqn.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class ActiveScoreboard extends SingleScoreboard {

    private final GameInstance gameInstance;

    public ActiveScoreboard(Player player, GameInstance gameInstance) {
        super(player);
        this.gameInstance = gameInstance;
    }

    @Override
    public void onUpdate() {
        final List<Component> lines = new ArrayList<>();

        Component title = ChatUtils.format("<red><bold>BINGO");

        lines.add(ChatUtils.format("<red>Round ending in <white><bold>" + gameInstance.getCurrentState().get().getFormattedTimer()));


        lines.add(ChatUtils.format("<red>------------------------"));
        lines.add(ChatUtils.format("<red>play.communitycraft.nl"));

        getFastBoard().updateTitle(title);

        getFastBoard().updateLines(lines);
    }
}
