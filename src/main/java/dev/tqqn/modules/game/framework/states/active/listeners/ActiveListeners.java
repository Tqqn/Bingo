package dev.tqqn.modules.game.framework.states.active.listeners;

import dev.tqqn.modules.database.framework.events.PlayerModelQuitEvent;
import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.game.framework.objects.BingoTask;
import dev.tqqn.modules.game.framework.states.active.ActiveState;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.InventoryView;
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

    @EventHandler
    public void onPlayerLeave(PlayerModelQuitEvent event) {
        if (Bukkit.getOnlinePlayers().size() <= 1) {
            state.getGameInstance().nextState();
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setKeepInventory(true);
        event.setShouldDropExperience(false);
        event.getDrops().clear();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(state.getGameInstance().getGameModule().getArena().getSpawnLocation());
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        final ItemStack item = event.getItemDrop().getItemStack();
        if (isItemPartOfKit(item)) event.setCancelled(true);
    }

    @EventHandler
    public void onItemMoveEvent(InventoryClickEvent event) {
        final ItemStack item = event.getCurrentItem();
        if (item == null) return;
        if (isItemPartOfKit(item)) {
            final InventoryView inventoryView = event.getView();
            if (!inventoryView.getTopInventory().equals(inventoryView.getBottomInventory())) {
                if (inventoryView.getTopInventory().getType() != InventoryType.CRAFTING) event.setCancelled(true);
            }
        }
    }

    private boolean isItemPartOfKit(ItemStack itemStack) {
        if (itemStack == null) return false;
        return itemStack.getPersistentDataContainer().has(ActiveState.KIT_ITEM_KEY);
    }

    private void processPossibleBingo(Player player, ItemStack item) {
        final PlayerModel playerModel = PlayerModel.from(player.getUniqueId());
        for (BingoTask task : state.getGameInstance().getBingoTasks()) {
            if (!(task.getGoal().getType() == item.getType())) continue;
            if (task.hasCompleted(playerModel.getTempPlayerData().getTeam())) return;
            state.completeTask(playerModel, task);
        }

        final boolean hasBingo = state.getGameInstance().hasBingo(playerModel);
        if (hasBingo) {
            state.broadcastWithSound("<red>" + player.getName() + " has completed a bingo! Congratulations!", Sound.ITEM_GOAT_HORN_SOUND_2);
            state.getGameInstance().nextState();
        }
    }
}
