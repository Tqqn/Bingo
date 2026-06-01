package dev.tqqn.game.modules.scoreboard.boards;

import dev.tqqn.game.modules.game.framework.states.active.ActiveState;
import dev.tqqn.game.modules.scoreboard.framework.StateScoreboard;
import dev.tqqn.game.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class ActiveScoreboard extends StateScoreboard<ActiveState> {

    public ActiveScoreboard(Player player, ActiveState state) {
        super(player, state);
    }

    @Override
    public void onUpdate() {
        final List<Component> lines = new ArrayList<>();

        Component title = ChatUtils.format("<red><bold>BINGO");
        lines.add(ChatUtils.empty());

        if (getState().isGracePeriod()) {
            lines.add(ChatUtils.format("<red><bold>Grace period: <yellow>" + getState().getFormattedGracePeriodTimer()));
        } else {
            lines.add(ChatUtils.format("<red>Game End: <yellow>" + getState().getFormattedTimer()));
        }

        lines.add(ChatUtils.empty());

        lines.add(ChatUtils.format("<gray>" + ChatUtils.getFormattedDate(LocalDateTime.now(), ChatUtils.DATE_FORMATTER_YY_DASH_MM_DASH_DD)));
        lines.add(ChatUtils.format("<yellow>play.communitycraft.nl"));

        getFastBoard().updateTitle(title);

        getFastBoard().updateLines(lines);
    }
}
