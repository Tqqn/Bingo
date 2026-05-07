package dev.tqqn.modules.game.framework.menu.recipe;

import dev.tqqn.modules.menu.framework.objects.MenuButton;
import dev.tqqn.modules.menu.framework.objects.PagedMenu;
import dev.tqqn.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 02/05/2026
 */
public class PagedRecipeMenu extends PagedMenu {

    @Getter private final ItemStack itemStack;
    private final List<Recipe> recipes;

    private final List<RecipeShapePart> shapeParts = new ArrayList<>();

    private PagedRecipeMenu possibleOldMenu = null;

    public PagedRecipeMenu(Player viewer, ItemStack itemStack, PagedRecipeMenu possibleOldMenu) {
        super(itemStack.getItemMeta().itemName(), 6, viewer);
        this.itemStack = itemStack;
        this.possibleOldMenu = possibleOldMenu;
        System.out.println(Bukkit.getServer().getRecipesFor(itemStack));
        recipes = Bukkit.getServer().getRecipesFor(itemStack).stream().filter(recipe -> recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe || recipe instanceof FurnaceRecipe).toList();
        initialize();
    }

    public PagedRecipeMenu(Player viewer, ItemStack itemStack) {
        this(viewer, itemStack, null);
    }

    @Override
    public void open() {
        if (itemStack == null || itemStack.getType() == Material.AIR) return;
        if (recipes.isEmpty()) return;
        openPage(0);
    }

    @Override
    protected void onOpenPage(int page) {
    }

    public void initialize() {
        shapeParts.add(new RecipeShapePart(List.of(10, 11, 12)));
        shapeParts.add(new RecipeShapePart(List.of(19, 20, 21)));
        shapeParts.add(new RecipeShapePart(List.of(28, 29, 30)));
        if (possibleOldMenu != null) {
            registerButton(new MenuButton(ItemBuilder.getBuilder(Material.BARRIER).setDisplayName("<red>Go Back - Previous Recipe").build()), 45);
        } else {
            addPageButton(PageButton.CLOSE, 45);
        }
        if (recipes.size() > 1) {
            addPageButton(PageButton.BACK, 48);
            addPageButton(PageButton.HOME, 49);
            addPageButton(PageButton.FORWARD, 50);
        }
    }

    @Override
    public void reload() {
        final List<RecipeShapePart> shapePartsCopy = new ArrayList<>();
        for (RecipeShapePart recipeShapePart : shapeParts) {
            shapePartsCopy.add(recipeShapePart.clone());
        }

        final Recipe recipe = recipes.get(getPage());
        if (recipe == null) return;
        if (shapePartsCopy.isEmpty()) return;

        RecipeType recipeType = null;

        if (recipe instanceof ShapedRecipe shapedRecipe) {
            final Map<Character, RecipeChoice> ingredientMap = shapedRecipe.getChoiceMap();
            for (String value : shapedRecipe.getShape()) {
                if (shapePartsCopy.isEmpty()) continue;
                final RecipeShapePart recipeShapePart = shapePartsCopy.removeFirst();
                for (char c : value.toCharArray()) {
                    final int slot = recipeShapePart.getNextAvailableSlot();
                    if (slot == -1) continue;
                    final RecipeChoice recipeChoice = ingredientMap.get(c);
                    if (recipeChoice == null) continue;
                    if (recipeChoice instanceof RecipeChoice.MaterialChoice materialChoice) {
                        registerButton(getRecipeIngredientButton(materialChoice.getItemStack()), slot);
                    }
                }
            }
            recipeType = RecipeType.CRAFTING_TABLE;
        }

        if (recipe instanceof ShapelessRecipe shapelessRecipe) {
            for (RecipeChoice recipeChoice : shapelessRecipe.getChoiceList()) {
                if (recipeChoice instanceof RecipeChoice.MaterialChoice materialChoice) {
                    if (shapePartsCopy.isEmpty()) continue;
                    final RecipeShapePart recipeShapePart = shapePartsCopy.removeFirst();
                    final int slot = recipeShapePart.getNextAvailableSlot();
                    if (slot < 0) continue;
                    registerButton(getRecipeIngredientButton(materialChoice.getItemStack()), slot);
                }
            }
            recipeType = RecipeType.CRAFTING_TABLE;
        }

        if (recipe instanceof FurnaceRecipe furnaceRecipe) {
            final RecipeChoice recipeChoice = furnaceRecipe.getInputChoice();
            if (recipeChoice instanceof RecipeChoice.MaterialChoice materialChoice) {
                registerButton(getRecipeIngredientButton(materialChoice.getItemStack()), 11);
                registerButton(getRecipeIngredientButton(recipe.getResult()), 25);
            }
            recipeType = RecipeType.FURNACE;
        }

        registerButton(new MenuButton(recipe.getResult()), 25);

        fillInventoryBorder(recipeType);
    }

    private MenuButton getRecipeIngredientButton(ItemStack itemStack) {
        final MenuButton button = new MenuButton(itemStack);
        final List<Recipe> possibleRecipes = Bukkit.getServer().getRecipesFor(itemStack);
        if (possibleRecipes.isEmpty()) return button;

        button.setClicker(player -> {
            final PagedRecipeMenu newMenu = new PagedRecipeMenu(player, itemStack, this);
            newMenu.open();
        });
        return button;
    }

    private void fillInventoryBorder(RecipeType recipeType) {
        if (recipeType == null) return;
        switch (recipeType) {
            case CRAFTING_TABLE -> {
                final List<Integer> graySlots = List.of(5, 6, 7, 8, 14, 23, 32, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
                final List<Integer> orangeSlots = List.of(0, 4, 36, 40);
                final List<Integer> brownSlots = List.of(1, 2, 3, 9, 13, 18, 22, 27, 31, 37, 38, 39);
                final List<Integer> limeGreenSlots = List.of(16, 24, 26, 34);
                final List<Integer> greenSlots = List.of(15, 17, 33, 35);

                setSlotsBasedOfList(graySlots, Material.GRAY_STAINED_GLASS_PANE);
                setSlotsBasedOfList(orangeSlots, Material.ORANGE_STAINED_GLASS_PANE);
                setSlotsBasedOfList(brownSlots, Material.BROWN_STAINED_GLASS_PANE);
                setSlotsBasedOfList(limeGreenSlots, Material.LIME_STAINED_GLASS_PANE);
                setSlotsBasedOfList(greenSlots, Material.GREEN_STAINED_GLASS_PANE);
            }

            case FURNACE -> {
                final List<Integer> blackSlots = List.of(0, 1, 2, 3, 4, 9, 13, 18, 19, 20, 21, 22, 27, 31, 36, 37, 38, 39, 40);
                final List<Integer> brownSlots = List.of(10, 12);
                final List<Integer> whiteSlots = List.of(5, 6, 7, 8, 14, 23, 32, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
                final List<Integer> fireSlots = List.of(28, 29, 30);

                setSlotsBasedOfList(blackSlots, Material.BLACK_STAINED_GLASS_PANE);
                setSlotsBasedOfList(brownSlots, Material.BROWN_STAINED_GLASS_PANE);
                setSlotsBasedOfList(whiteSlots, Material.WHITE_STAINED_GLASS_PANE);
                setSlotsBasedOfList(fireSlots, Material.CAMPFIRE);
            }
        }

    }

    private void setSlotsBasedOfList(List<Integer> slots, Material material) {
        for (int slot : slots) {
            if (!isSlotEmpty(slot)) continue;
            registerButton(new MenuButton(ItemBuilder.getBuilder(material).removeDisplayName().build()), slot);
        }
    }

    @Override
    public void onOpen() {}

    @Override
    public void onClose(Player viewer) {}

    @Override
    public int getPageCount() {
        return recipes.size() - 1;
    }

    public class RecipeShapePart implements Cloneable {

        private final List<Integer> availableSlots = new ArrayList<>();

        public RecipeShapePart(List<Integer> slots) {
            availableSlots.addAll(slots);
        }

        public int getNextAvailableSlot() {
            if (availableSlots.isEmpty()) return -1;
            return availableSlots.removeFirst();
        }

        @Override
        protected RecipeShapePart clone() {
            return new RecipeShapePart(List.copyOf(availableSlots));
        }
    }

    private enum RecipeType {
        CRAFTING_TABLE,
        FURNACE
    }
}
