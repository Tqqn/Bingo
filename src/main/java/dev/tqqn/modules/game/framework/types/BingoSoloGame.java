package dev.tqqn.modules.game.framework.types;

import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.GameInstance;
import dev.tqqn.modules.game.framework.GameStates;
import dev.tqqn.modules.game.framework.roles.Roles;
import dev.tqqn.modules.game.framework.states.LobbyState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public final class BingoSoloGame extends GameInstance {

    private final Map<UUID, Roles> currentPlayers = new HashMap<>();

    public BingoSoloGame(int id, GameModule gameModule) {
        super(id, gameModule);
        setState(new LobbyState(this));
    }

    @Override
    public void changeState(GameStates gameStates) {

    }

    @Override
    public void addPlayer(UUID uuid, Roles role) {
        currentPlayers.put(uuid, role);
    }

    @Override
    public void removePlayer(UUID uuid) {
        currentPlayers.remove(uuid);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {}

    @Override
    public void onTick() {}

    @Override
    public boolean isThereAWinner() {
        return false;
    }

    @Override
    public boolean canStart() {
        return currentPlayers.size() == 1;
        //return currentPlayers.size() >= GameModule.GAME_MIN_PLAYERS_TO_START;
    }

    @Override
    public int getPlayerCount() {
        return currentPlayers.size();
    }

    private void spawnPlayers() {
        for (UUID uuid : currentPlayers.keySet()) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                currentPlayers.remove(uuid);
                continue;
            }

            // TODO: Teleport players to spawnpoint.
        }
    }
}
