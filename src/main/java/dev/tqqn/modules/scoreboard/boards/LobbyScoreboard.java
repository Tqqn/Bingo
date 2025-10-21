package dev.tqqn.modules.scoreboard.boards;

import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.abstraction.GameInstance;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractState;
import dev.tqqn.modules.game.framework.states.lobby.LobbyState;
import dev.tqqn.modules.scoreboard.framework.SingleScoreboard;
import dev.tqqn.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class LobbyScoreboard extends SingleScoreboard {

    private final GameInstance gameInstance;
    private final AbstractState currentState;

    public LobbyScoreboard(Player player, GameInstance gameInstance) {
        super(player);
        this.gameInstance = gameInstance;
        currentState = gameInstance.getGameStateSeries().getCurrentState().get();
    }

    @Override
    public void onUpdate() {
        if (currentState == null || !(currentState instanceof LobbyState)) {
            delete();
            return;
        }

        Component title = ChatUtils.format("<red><bold>BINGO");
        getFastBoard().updateTitle(title);

        final List<Component> lines = new ArrayList<>();
        lines.add(ChatUtils.empty());
        lines.add(ChatUtils.format("<red>Players: <gold>" + gameInstance.getInGameAlivePlayerCount() + "<red>/<gold>" + GameModule.GAME_MAX_PLAYERS));
        lines.add(ChatUtils.format("<red>Status: <gold>Waiting..."));

        if (gameInstance.canStart()) {
            lines.add(ChatUtils.format("<red>Starting in <white>" + gameInstance.getGameStateSeries().getCurrentState().get().getTimer() + "<red>s"));
        }

        lines.add(ChatUtils.format("<red>Mode: <gold>Solo"));
        lines.add(ChatUtils.format("<red>------------------------"));
        lines.add(ChatUtils.format("<red>play.dusdavidgames.nl"));

        getFastBoard().updateLines(lines);
    }
}
