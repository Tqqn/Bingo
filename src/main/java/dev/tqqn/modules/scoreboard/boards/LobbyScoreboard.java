package dev.tqqn.modules.scoreboard.boards;

import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.abstraction.GameInstance;
import dev.tqqn.modules.game.framework.states.lobby.LobbyState;
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
    }

    @Override
    public void onUpdate() {
        if (!(gameInstance.getCurrentState() instanceof LobbyState)) {
            getFastBoard().delete();
            final PlayerModel playerModel = getPlayerWeakReference().get();
            if (playerModel == null) return;
            playerModel.getTempPlayerData().setScoreboard(null);
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
