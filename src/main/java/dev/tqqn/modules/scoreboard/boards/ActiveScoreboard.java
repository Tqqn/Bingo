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

    private final List<Component> lines = new ArrayList<>();

    public ActiveScoreboard(Player player, GameInstance gameInstance) {
        super(player);

        lines.add(ChatUtils.format("<red>Players: <gold>" + gameInstance.getInGamePlayers().size() + "<red>/<gold>" + GameModule.GAME_MAX_PLAYERS));

        lines.add(ChatUtils.format("<red>------------------------"));
        lines.add(ChatUtils.format("<red>play.communitycraft.nl"));
    }

    @Override
    public void onUpdate() {
        Component title = ChatUtils.format("<red><bold>BINGO");
        getFastBoard().updateTitle(title);

        getFastBoard().updateLines(lines);
    }
}
