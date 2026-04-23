package dev.tqqn.modules.game.framework.states.lobby;

import dev.tqqn.modules.database.framework.events.PlayerModelJoinEvent;
import dev.tqqn.modules.database.framework.events.PlayerModelPreJoinEvent;
import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.GameStates;
import dev.tqqn.modules.game.framework.roles.Roles;
import dev.tqqn.modules.game.framework.states.GameStateSeries;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractState;
import dev.tqqn.modules.game.framework.states.lobby.listeners.LobbyListeners;
import dev.tqqn.modules.scoreboard.boards.LobbyScoreboard;
import dev.tqqn.utils.ChatUtils;
import dev.tqqn.utils.NMSUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public final class LobbyState extends AbstractState {

    private boolean isStartCountdown = false;
    private static final String message = "<red>Game is starting in <white><bold>%s<reset><red>s.";

    public LobbyState(GameStateSeries instance) {
        super(instance, GameStates.LOBBY, "Lobby", 10000, true);
    }

    @Override
    public void onTick() {
        if (getGameInstance().canStart()) {
            if (!isStartCountdown) {
                setTimer(60);
                isStartCountdown = true;
                broadcastWithSound("<green>Enough players have joined, game starting in <red><bold>" + getTimer() + "s<reset><green>.", Sound.ENTITY_PARROT_IMITATE_BLAZE);
            }

            if (timer < 31) {
                if (timer == 30 || timer == 10 || timer < 6) broadcastWithSound(String.format(message, timer), Sound.BLOCK_NOTE_BLOCK_PLING);
            }

        } else if (isStartCountdown) {
            isStartCountdown = false;
            setTimer(180);
            broadcastWithSound("<red>Not enough players in this game. Setting timer to " + (getTimer() / 60) + " minutes", Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF);
        }
    }

    @Override
    public void onTimerEnd() {
        if (!getGameInstance().canStart()) {
            resetTimer();
            return;
        }
        getGameInstance().nextState();
    }

    @Override
    public void onEnable() {
        register(new LobbyListeners(getGameInstance().getGameModule()));
    }

    @Override
    public void onDisable() {
        getGameInstance().getGameModule().putPlayersInTeams();
        for (Player player : Bukkit.getOnlinePlayers()) {
            removeScoreboard(LobbyScoreboard.class, player);
            NMSUtils.refreshTag(player);
        }
    }

    @Override
    public void onPlayerJoin(PlayerModel playerModel, PlayerModelJoinEvent event) {
        final AbstractState currentState = getGameInstance().getCurrentState().get();
        if (currentState == null) return;
        event.getPlayerModel().getPlayer().ifPresent(player -> {
            currentState.setScoreboard(player);

            getGameInstance().addPlayer(player, Roles.ALIVE);
            player.getInventory().clear();
            player.getActivePotionEffects().clear();
            player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getBaseValue());
            player.setGameMode(GameMode.SURVIVAL);
        });

        broadcast("<yellow>[<aqua>" + (getGameInstance().getInGamePlayers().size()) + "<yellow>/<aqua>" + GameModule.GAME_MAX_PLAYERS + "<yellow>] <green>+ " + playerModel.getName());

    }

    @Override
    public void onPlayerPreJoin(PlayerModel playerModel, PlayerModelPreJoinEvent event) {
        if (event.isCancelled()) return;

        if (!getGameInstance().getGameModule().getArena().isReadyToJoin()) {
            event.setCancelled(true);
            event.setKickMessage(ChatUtils.format("<red>The game is not ready to join yet. Please try again later."));
        }
    }

    @Override
    public void setScoreboard(Player player) {
        applyScoreboard(new LobbyScoreboard(player, getGameInstance()), player);
    }

    public void quickStart() {
        setTimer(11);
    }
}
