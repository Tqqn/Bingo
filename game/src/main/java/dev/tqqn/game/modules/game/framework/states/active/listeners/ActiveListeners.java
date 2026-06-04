package dev.tqqn.game.modules.game.framework.states.active.listeners;

import dev.tqqn.game.modules.database.framework.events.PlayerModelQuitEvent;
import dev.tqqn.game.modules.database.framework.objects.PlayerModel;
import dev.tqqn.game.modules.game.framework.events.CompleteBingoTaskEvent;
import dev.tqqn.game.modules.game.framework.menu.BingoMenu;
import dev.tqqn.game.modules.game.framework.objects.BingoTask;
import dev.tqqn.game.modules.game.framework.states.active.ActiveState;
import dev.tqqn.game.utils.Notify;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@RequiredArgsConstructor
public final class ActiveListeners implements Listener {

    private final ActiveState state;

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (state.isGracePeriod()) event.setCancelled(true);

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            final Location loc = player.getLocation();
            final Location center = state.getGameInstance().getGameModule().getArena().getSpawnLocation();
            double dx = loc.getX() - center.getX();
            double dz = loc.getZ() - center.getZ();

            if (dx * dx + dz * dz <= 20 * 20) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        final ItemStack item = event.getItem();
        if (item == null) return;

        final ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;
        if (!itemMeta.getPersistentDataContainer().has(ActiveState.BINGO_MAP_KEY)) return;
        final Player player = event.getPlayer();
        new BingoMenu(player, state.getGameInstance()).open();
    }

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

    @EventHandler
    public void onBingoTaskComplete(CompleteBingoTaskEvent event) {
        Notify.INFO.chat(event.getPlayer(), "You collected: " + event.getCompletedTask().getName());
        for (Player player : state.getGameInstance().getInGamePlayers().keySet()) {
            final String teamColor = event.getTeam().getData().teamType().getPrefixColor();
            Notify.INFO.chat(player, teamColor + event.getPlayer().getName() + " has collected: <bold>" + event.getCompletedTask().getName() + "</bold> for their team.");
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
