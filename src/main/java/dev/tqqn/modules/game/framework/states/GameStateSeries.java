package dev.tqqn.modules.game.framework.states;

import dev.tqqn.modules.game.framework.abstraction.GameInstance;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractStateSeries;
import dev.tqqn.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GameStateSeries extends AbstractStateSeries {

    public GameStateSeries(GameInstance instance) {
        super(instance);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onFreeze() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("bingo.admin")) {
                player.sendMessage(ChatUtils.format("<aqua>The game has been " + (getCurrentState().get().isFreeze() ? "<red>frozen" : "<green>unfrozen") + "<aqua>."));
            }
        }
    }
}
