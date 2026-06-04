package dev.tqqn.game.modules.scoreboard.boards;

import dev.tqqn.game.modules.game.framework.abstraction.GameInstance;
import dev.tqqn.game.modules.game.framework.states.lobby.LobbyState;
import dev.tqqn.game.modules.scoreboard.framework.StateScoreboard;
import dev.tqqn.game.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class LobbyScoreboard extends StateScoreboard<LobbyState> {

    private final GameInstance instance;

    public LobbyScoreboard(Player player, LobbyState lobbyState) {
        super(player, lobbyState);
        this.instance = lobbyState.getGameInstance();
    }

    @Override
    public void onUpdate() {
        Component title = ChatUtils.format("<red><bold>BINGO");
        getFastBoard().updateTitle(title);

        final List<Component> lines = new ArrayList<>();
        lines.add(ChatUtils.empty());
        lines.add(ChatUtils.format("<red>Players: <yellow>" + instance.getInGameAlivePlayerCount() + "<red>/<yellow>" + instance.getGameSettings().getMaxGamePlayers()));
        lines.add(ChatUtils.empty());

        if (instance.canStart()) {
            lines.add(ChatUtils.format("<red>Status: Starting in <white>" + ChatUtils.convertSecondsToHMmSs(getState().getTimer())));
        } else {
            lines.add(ChatUtils.format("<red>Status: <gold>Waiting..."));
        }

        lines.add(ChatUtils.format("<red>Mode: <gold>" + instance.getGameSettings().getGameType().getDisplayName()));

        lines.add(ChatUtils.empty());

        lines.add(ChatUtils.format("<gray>" + ChatUtils.getFormattedDate(LocalDateTime.now(), ChatUtils.DATE_FORMATTER_YY_DASH_MM_DASH_DD)));
        lines.add(ChatUtils.format("<yellow>play.communitycraft.nl"));

        getFastBoard().updateLines(lines);
    }
}
