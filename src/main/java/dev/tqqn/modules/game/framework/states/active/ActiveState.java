package dev.tqqn.modules.game.framework.states.active;

import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.game.framework.abstraction.GameInstance;
import dev.tqqn.modules.game.framework.GameStates;
import dev.tqqn.modules.game.framework.map.BingoMapRenderer;
import dev.tqqn.modules.game.framework.map.IconCache;
import dev.tqqn.modules.game.framework.objects.BingoTask;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractState;
import dev.tqqn.modules.game.framework.states.active.listeners.ActiveListeners;
import dev.tqqn.modules.game.framework.types.BingoGame;
import dev.tqqn.modules.scoreboard.boards.ActiveScoreboard;
import dev.tqqn.utils.ChatUtils;
import dev.tqqn.utils.Notify;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

public final class ActiveState extends AbstractState {

    private BingoMapRenderer mapRenderer;

    public ActiveState(GameInstance instance) {
        super(instance, GameStates.ACTIVE, "Active", true);
        register(new ActiveListeners(this));
    }

    @Override
    public void onEnable() {
        setTimer(1200); // 15 min

        final MapView mapView = Bukkit.createMap(Bukkit.getWorlds().get(0));

        this.mapRenderer = new BingoMapRenderer(this, new IconCache(getGameInstance().getGameModule().getPlugin()));
        mapView.getRenderers().clear();
        mapView.addRenderer(mapRenderer);
        mapView.setLocked(true);
        mapView.setTrackingPosition(false);
        final ItemStack mapItem = getBingoMapItem(mapView);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().addItem(mapItem);
        }
    }

    @Override
    public void onTick() {
    }

    @Override
    public void onTimerEnd() {
        disable();
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
    public BingoGame getGameInstance() {
        return (BingoGame) super.getGameInstance();
    }

    public void completeTask(PlayerModel playerModel, BingoTask task) {
        playerModel.getTempPlayerData().completeTask(task);
        task.complete(playerModel.getTempPlayerData().getTeam());
        Notify.INFO.chat(playerModel.getPlayer(), "You collected: " + task.getName());
    }

    private ItemStack getBingoMapItem(MapView mapView) {
        final ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
        final MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
        mapMeta.displayName(ChatUtils.format("<red>Bingo Map"));
        mapMeta.setMapView(mapView);
        mapItem.setItemMeta(mapMeta);
        return mapItem;
    }
}
