package dev.tqqn.modules.game.framework.menu;

import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.game.framework.abstraction.GameInstance;
import dev.tqqn.modules.game.framework.objects.BingoPlacement;
import dev.tqqn.modules.game.framework.objects.BingoTask;
import dev.tqqn.modules.game.framework.states.GameStateSeries;
import dev.tqqn.modules.menu.framework.objects.Menu;
import dev.tqqn.modules.menu.framework.objects.MenuButton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Map;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 23/04/2026
 */
public class BingoMenu extends Menu {

    private final PlayerModel playerModel;
    private final GameStateSeries gameStateSeries;
    private final BingoTask[][] grid;

    public BingoMenu(Player viewer, GameStateSeries gameStateSeries) {
        super("<red>Bingo Menu", 3, viewer);
        this.playerModel = PlayerModel.from(viewer);
        this.gameStateSeries = gameStateSeries;
        this.grid = new BingoTask[5][5];
        if (playerModel == null) return;
        initialize();
    }

    @Override
    public void reload() {

    }

    @Override
    public void onOpen() {
        initialize();
    }

    @Override
    public void onClose(Player viewer) {

    }

    private void initialize() {
        for (Map.Entry<BingoTask, BingoPlacement> entry : gameStateSeries.getBingoPlacements().entrySet()) {
            BingoPlacement placement = entry.getValue();
            grid[placement.getRow()][placement.getColumn()] = entry.getKey();
        }
    }

//    private MenuButton getBingoTaskButton(BingoTask task) {
//        final ItemStack itemStack = task.getGoal();
//        final Recipe recipe = Bukkit.getRecipesFor(itemStack);
//        final ShapedRecipe shapedRecipe;
//    }
}
