package dev.tqqn.modules.game.framework.types;

import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.abstraction.GameInstance;
import dev.tqqn.modules.game.framework.GameStates;
import dev.tqqn.modules.game.framework.roles.Roles;
import dev.tqqn.modules.game.framework.states.active.ActiveState;
import dev.tqqn.modules.game.framework.states.end.EndState;
import dev.tqqn.modules.game.framework.states.lobby.LobbyState;

import java.util.*;
import java.util.stream.Collectors;

public final class BingoSoloGame extends GameInstance {

    public BingoSoloGame(int id, GameModule gameModule) {
        super(id, gameModule);
        getGameStateSeries().registerStates(List.of(new LobbyState(this), new ActiveState(this), new EndState(this)));
    }



//    @Override
//    public void changeState(GameStates gameStates) {
//        final GameStates currentState = getCurrentState().getGameState();
//        if (currentState == gameStates) return;
//        switch (gameStates) {
//            case ACTIVE -> {
//                if (currentState == GameStates.END) return;
//                setState(new ActiveState(this));
//            }
//
//            case END -> {
//                if (currentState == GameStates.LOBBY) return;
//                setState(new EndState(this));
//            }
//        }
//    }
//
//    @Override
//    public void addPlayer(UUID uuid, Roles role) {
//        currentPlayers.put(uuid, role);
//    }
//
//    @Override
//    public void removePlayer(UUID uuid) {
//        currentPlayers.remove(uuid);
//    }
//
//    @Override
//    public void onStart() {
//
//    }
//
//    @Override
//    public void onStop() {}
//
//    @Override
//    public void onTick() {}
//
//    @Override
//    public boolean isThereAWinner() {
//        return false;
//    }
//
    @Override
    public boolean canStart() {
        return getInGamePlayers().values().stream().filter(role -> role == Roles.ALIVE).toList().size() == 2;
        //return currentPlayers.size() >= GameModule.GAME_MIN_PLAYERS_TO_START;
    }
//
//    @Override
//    public int getPlayerCount() {
//        return currentPlayers.size();
//    }

}
