package dev.tqqn.lobby.modules.scoreboard;

import dev.tqqn.common.scoreboard.framework.SingleScoreboard;
import dev.tqqn.common.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 23/06/2026
 */
public class LobbyScoreboard extends SingleScoreboard {

    public LobbyScoreboard(Player player) {
        super(player);
    }

    @Override
    public void onUpdate() {
        Component title = ChatUtils.format("<red><bold>BINGO");
        getFastBoard().updateTitle(title);

        final List<Component> lines = new ArrayList<>();
        lines.add(ChatUtils.empty());
        lines.add(ChatUtils.format("<yellow>Wins: <white>10"));
        lines.add(ChatUtils.format("<yellow>Coins: <white>100"));

        lines.add(ChatUtils.empty());

        lines.add(ChatUtils.format("<gray>" + ChatUtils.getFormattedDate(LocalDateTime.now(), ChatUtils.DATE_FORMATTER_YY_DASH_MM_DASH_DD)));
        lines.add(ChatUtils.format("<yellow>play.communitycraft.nl"));

        getFastBoard().updateLines(lines);
    }
}
