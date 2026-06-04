package dev.tqqn.game.modules.scoreboard.boards;

import dev.tqqn.game.modules.game.framework.states.end.EndState;
import dev.tqqn.game.modules.scoreboard.framework.StateScoreboard;
import dev.tqqn.game.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EndScoreboard extends StateScoreboard<EndState> {

    public EndScoreboard(Player player, EndState endState) {
        super(player, endState);
    }

    @Override
    public void onUpdate() {
        final List<Component> lines = new ArrayList<>();
        Component title = ChatUtils.format("<red><bold>BINGO");

        lines.add(ChatUtils.format("<red>Game has ended...."));
        lines.add(ChatUtils.format("<red>Game ending in <white><bold>" + getState().getFormattedTimer() + " <reset><red>seconds!"));

        lines.add(ChatUtils.format("<red>------------------------"));
        lines.add(ChatUtils.format("<red>play.communitycraft.nl"));

        getFastBoard().updateTitle(title);

        getFastBoard().updateLines(lines);
    }

}
