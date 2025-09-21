package dev.tqqn.modules.scoreboard.boards;

import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.GameInstance;
import dev.tqqn.modules.game.framework.states.LobbyState;
import dev.tqqn.modules.scoreboard.framework.SingleScoreboard;
import dev.tqqn.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class LobbyScoreboard extends SingleScoreboard {

    private final GameInstance gameInstance;

    public LobbyScoreboard(Player player, GameInstance gameInstance) {
        super(player);
        this.gameInstance = gameInstance;

        if (!(gameInstance.getCurrentState() instanceof LobbyState lobbyState)) return;
    }

    @Override
    public void update() {
        if (!(gameInstance.getCurrentState() instanceof LobbyState)) {
            getFastBoard().delete();
            return;
        }

        Component title = ChatUtils.format("<red><bold>BINGO");
        getFastBoard().updateTitle(title);

        final List<Component> lines = new ArrayList<>();
        lines.add(ChatUtils.empty());
        lines.add(ChatUtils.format("<red>Players: <gold>" + gameInstance.getPlayerCount() + "<red>/<gold>" + GameModule.GAME_MAX_PLAYERS));
        lines.add(ChatUtils.format("<red>Status: <gold>Waiting..."));

        if (gameInstance.canStart()) {
            lines.add(ChatUtils.format("<red>Starting in <white>" + gameInstance.getCurrentState().getTimer() + "<red>s"));
        }

        lines.add(ChatUtils.format("<red>Mode: <gold>Solo"));
        lines.add(ChatUtils.format("<red>------------------------"));
        lines.add(ChatUtils.format("<red>play.dusdavidgames.nl"));



        getFastBoard().updateLines(lines);
    }
}
