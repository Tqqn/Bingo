package dev.tqqn.modules.game.framework.states.active.listeners;

import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.game.framework.objects.BingoTask;
import dev.tqqn.modules.game.framework.states.active.ActiveState;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public final class ActiveListeners implements Listener {

    private final ActiveState state;

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        final ItemStack item = event.getItem().getItemStack();
        if (!(event.getEntity() instanceof Player player)) return;
        processPossibleBingo(player, item);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        final ItemStack item = event.getRecipe().getResult();
        if (!(event.getWhoClicked() instanceof Player player)) return;
        processPossibleBingo(player, item);
    }

    private void processPossibleBingo(Player player, ItemStack item) {
        final PlayerModel playerModel = PlayerModel.from(player.getUniqueId());
        for (BingoTask task : state.getGameInstance().getGameStateSeries().getBingoTasks()) {
            if (!(task.getGoal().getType() == item.getType())) continue;
            if (playerModel.getTempPlayerData().hasCompleted(task)) return;
            state.completeTask(playerModel, task);
        }
    }

}
