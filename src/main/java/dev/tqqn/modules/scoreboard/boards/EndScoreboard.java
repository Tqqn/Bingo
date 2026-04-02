package dev.tqqn.modules.scoreboard.boards;

import dev.tqqn.modules.game.framework.abstraction.GameInstance;
import dev.tqqn.modules.scoreboard.framework.SingleScoreboard;
import dev.tqqn.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EndScoreboard extends SingleScoreboard {

    private final GameInstance gameInstance;

    public EndScoreboard(Player player, GameInstance gameInstance) {
        super(player);
        this.gameInstance = gameInstance;
    }

    @Override
    public void onUpdate() {
        final List<Component> lines = new ArrayList<>();
        Component title = ChatUtils.format("<red><bold>BINGO");

        lines.add(ChatUtils.format("<red>Game has ended...."));
        lines.add(ChatUtils.format("<red>Game ending in <white><bold>" + gameInstance.getGameStateSeries().getCurrentState().get().getFormattedTimer() + " <reset><red>seconds!"));

        lines.add(ChatUtils.format("<red>------------------------"));
        lines.add(ChatUtils.format("<red>play.communitycraft.nl"));

        getFastBoard().updateTitle(title);

        getFastBoard().updateLines(lines);
    }

}
