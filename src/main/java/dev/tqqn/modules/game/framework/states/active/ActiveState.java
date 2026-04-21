package dev.tqqn.modules.game.framework.states.active;

import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.game.framework.GameStates;
import dev.tqqn.modules.game.framework.visualizer.BingoMapRenderer;
import dev.tqqn.modules.game.framework.visualizer.IconCache;
import dev.tqqn.modules.game.framework.objects.BingoTask;
import dev.tqqn.modules.game.framework.states.GameStateSeries;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractState;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractStateSeries;
import dev.tqqn.modules.game.framework.states.active.listeners.ActiveListeners;
import dev.tqqn.modules.scoreboard.boards.ActiveScoreboard;
import dev.tqqn.utils.ChatUtils;
import dev.tqqn.utils.Notify;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public final class ActiveState extends AbstractState {

    public static final NamespacedKey KIT_ITEM_KEY = new NamespacedKey("bingo", "kit_item");

    private BingoMapRenderer mapRenderer;

    @Getter private ItemStack mapItem;
    @Getter private ItemStack pickaxe;

    public ActiveState(AbstractStateSeries instance) {
        super(instance, GameStates.ACTIVE, "Active", true);
        register(new ActiveListeners(this));
    }

    @Override
    public void onEnable() {
        setTimer(1200); // 15 min

        final MapView mapView = Bukkit.createMap(Bukkit.getWorlds().getFirst());

        this.mapRenderer = new BingoMapRenderer(this, new IconCache(getGameInstance().getGameModule().getPlugin()));
        mapView.getRenderers().clear();
        mapView.addRenderer(mapRenderer);
        mapView.setLocked(true);
        mapView.setTrackingPosition(false);
        mapItem = getBingoMapItem(mapView);

        pickaxe = getDefaultPickaxe();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
            player.getInventory().setItemInOffHand(mapItem.clone());
            player.getInventory().addItem(pickaxe.clone());
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 200, 100, true, false, false));
        }

        broadcastWithSound("<red>The game has started! <bold>Good luck!", Sound.ENTITY_ENDER_DRAGON_GROWL);
        getGameInstance().getGameModule().getArena().removeSpawnBorder();
    }

    @Override
    public void onTick() {
    }

    @Override
    public void onTimerEnd() {
        getGameInstance().nextState();
    }

    @Override
    public void setScoreboard(Player player) {
        applyScoreboard(new ActiveScoreboard(player, getGameInstance()), player);
    }

    @Override
    public void onDisable() {
        for (Player player : getGameInstance().getInGamePlayers().keySet()) {
            removeScoreboard(ActiveScoreboard.class, player);
        }
    }

    @Override
    public GameStateSeries getGameInstance() {
        return (GameStateSeries) super.getGameInstance();
    }

    public void completeTask(PlayerModel playerModel, BingoTask task) {
        task.complete(playerModel.getTempPlayerData().getTeam());
        playerModel.getPlayer().ifPresent(player -> Notify.INFO.chat(player, "You collected: " + task.getName()));
    }

    private ItemStack getBingoMapItem(MapView mapView) {
        final ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
        final MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
        mapMeta.displayName(ChatUtils.format("<red>Bingo Map"));
        mapMeta.setMapView(mapView);
        mapMeta.lore(List.of(ChatUtils.format("<gray>Collect all tasks to win!")));
        mapMeta.getPersistentDataContainer().set(KIT_ITEM_KEY, org.bukkit.persistence.PersistentDataType.STRING, "true");
        mapItem.setItemMeta(mapMeta);
        return mapItem;
    }

    private ItemStack getDefaultPickaxe() {
        final ItemStack pickaxe = new ItemStack(Material.NETHERITE_PICKAXE);
        final ItemMeta itemMeta = pickaxe.getItemMeta();
        itemMeta.getPersistentDataContainer().set(KIT_ITEM_KEY, org.bukkit.persistence.PersistentDataType.STRING, "true");
        itemMeta.displayName(ChatUtils.format("<red>Pickaxe"));
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.addEnchant(Enchantment.EFFICIENCY, 5, true);
        pickaxe.setItemMeta(itemMeta);
        return pickaxe;
    }
}
