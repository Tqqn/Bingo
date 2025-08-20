package dev.tqqn.modules.game.framework.types;

import dev.tqqn.modules.game.framework.GameInstance;
import dev.tqqn.modules.game.framework.GameStates;
import dev.tqqn.modules.game.framework.roles.Roles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public final class BingoSoloGame extends GameInstance {

    private final Map<UUID, Roles> currentPlayers = new HashMap<>();

    public BingoSoloGame(int id) {
        super(id);
    }

    @Override
    public void enableState(GameStates state) {

    }

    @Override
    public void disableState(GameStates state) {

    }

    @Override
    public void addPlayer(UUID uuid) {
        currentPlayers.put(uuid, Roles.ALIVE);
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
        spawnPlayers();
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onTick() {

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
