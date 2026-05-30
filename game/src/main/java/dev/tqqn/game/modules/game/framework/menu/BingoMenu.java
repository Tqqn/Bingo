package dev.tqqn.game.modules.game.framework.menu;

import dev.tqqn.game.modules.database.framework.objects.PlayerModel;
import dev.tqqn.game.modules.game.framework.menu.recipe.PagedRecipeMenu;
import dev.tqqn.game.modules.game.framework.objects.BingoPlacement;
import dev.tqqn.game.modules.game.framework.objects.BingoTask;
import dev.tqqn.game.modules.game.framework.states.GameStateSeries;
import dev.tqqn.game.modules.game.framework.team.GameTeam;
import dev.tqqn.game.modules.menu.framework.objects.Menu;
import dev.tqqn.game.modules.menu.framework.objects.MenuButton;
import dev.tqqn.game.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 23/04/2026
 */
public class BingoMenu extends Menu {

    private static final List<Integer> bingoSlots = Arrays.asList(2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 20, 21, 22, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42);

    private final PlayerModel playerModel;
    private final GameStateSeries gameStateSeries;
    private final BingoTask[][] grid;

    public BingoMenu(Player viewer, GameStateSeries gameStateSeries) {
        super("<red>Bingo Menu", 6, viewer);
        this.playerModel = PlayerModel.from(viewer);
        this.gameStateSeries = gameStateSeries;
        this.grid = new BingoTask[5][5];
        if (playerModel == null) return;
        initialize();
    }

    @Override
    public void reload() {}

    @Override
    public void onOpen() {}

    @Override
    public void onClose(Player viewer) {}

    private void initialize() {
        final List<Integer> copiedBingoSlots = new ArrayList<>(bingoSlots);
        for (Map.Entry<BingoTask, BingoPlacement> entry : gameStateSeries.getBingoPlacements().entrySet()) {
            BingoPlacement placement = entry.getValue();
            grid[placement.getRow()][placement.getColumn()] = entry.getKey();
        }

        for (BingoTask[] bingoTasks : grid) {
            for (BingoTask bingoTask : bingoTasks) {
                registerButton(getBingoTaskButton(bingoTask), copiedBingoSlots.removeFirst());
            }
        }

        registerButton(getCloseButton(), 45);

        fillInventoryBorder();
    }

    private MenuButton getBingoTaskButton(BingoTask task) {
        final boolean hasRecipes = !Bukkit.getServer().getRecipesFor(task.getGoal()).stream().filter(recipe -> recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe || recipe instanceof FurnaceRecipe).toList().isEmpty();

        final ItemBuilder itemBuilder = ItemBuilder.getBuilder(task.getGoal());

        final GameTeam gameTeam = playerModel.getTempPlayerData().getTeam();
        if (gameTeam != null) {
            if (task.hasCompleted(gameTeam)) {
                itemBuilder.setGlow();
                itemBuilder.addLore("<gold>You have completed this task!");
            } else {
                itemBuilder.addLore("<red>You have not completed this task yet.");
            }
        }

        if (hasRecipes) {
            itemBuilder.addLore("<yellow><bold>[CLICK]<!bold> To show recipes of this item.");
        } else {
            itemBuilder.addLore("<red>This item has no recipe. Try to find it!");
        }



        final ItemStack goal = itemBuilder.build();
        final MenuButton menuButton = new MenuButton(goal);

        if (hasRecipes) {
            menuButton.setClicker(player -> new PagedRecipeMenu(player, goal, this).open());
        }
        return menuButton;
    }

    private void fillInventoryBorder() {
        final List<Integer> whiteSlots = List.of(0, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 53);
        final List<Integer> graySlots = List.of(1, 7, 10, 16, 19, 25, 28, 34, 37, 43, 46, 47, 48, 49, 50, 51, 52);

        setSlotsBasedOfList(whiteSlots, Material.WHITE_STAINED_GLASS_PANE);
        setSlotsBasedOfList(graySlots, Material.GRAY_STAINED_GLASS_PANE);
    }
}
