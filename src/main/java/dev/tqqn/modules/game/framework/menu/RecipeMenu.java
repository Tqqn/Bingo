package dev.tqqn.modules.game.framework.menu;

import dev.tqqn.modules.menu.framework.objects.Menu;
import dev.tqqn.modules.menu.framework.objects.MenuButton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 23/04/2026
 */
public class RecipeMenu extends Menu {

    private final ItemStack itemStack;

    private final List<RecipeShapePart> shapeParts = new ArrayList<>();

    public RecipeMenu(Player viewer, ItemStack itemStack) {
        super(itemStack.getItemMeta().itemName(), 4, viewer);
        this.itemStack = itemStack;
        shapeParts.add(new RecipeShapePart(List.of(3, 4, 5)));
        shapeParts.add(new RecipeShapePart(List.of(12, 13, 14)));
        shapeParts.add(new RecipeShapePart(List.of(21, 22, 23)));
        initialize();
    }

    @Override
    public void reload() {

    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose(Player viewer) {

    }

    private void initialize() {
        final List<Recipe> recipes = Bukkit.getServer().getRecipesFor(itemStack);
        final List<RecipeShapePart> shapePartsCopy = new ArrayList<>(List.copyOf(shapeParts));
        for (Recipe recipe : recipes) {
            if (recipe instanceof ShapedRecipe shapedRecipe) {
                final Map<Character, RecipeChoice> ingredientMap = shapedRecipe.getChoiceMap();
                if (shapePartsCopy.isEmpty()) continue;
                for (String value : shapedRecipe.getShape()) {
                    final RecipeShapePart recipeShapePart = shapePartsCopy.removeFirst();
                    for (char c : value.toCharArray()) {
                        final int slot = recipeShapePart.getNextAvailableSlot();
                        if (slot == -1) continue;
                        final RecipeChoice recipeChoice = ingredientMap.get(c);
                        if (recipeChoice == null) continue;
                        if (recipeChoice instanceof RecipeChoice.MaterialChoice materialChoice) {
                            registerButton(getRecipePart(materialChoice.getItemStack()), slot);
                        }
                    }
                }
            }
        }
    }

    private MenuButton getRecipePart(ItemStack itemStack) {
        if (itemStack == null) return null;
        return new MenuButton(itemStack).setClicker(player -> {
            close();
        });
    }

    private class RecipeShapePart  {

        private final List<Integer> availableSlots = new ArrayList<>();

        public RecipeShapePart(List<Integer> slots) {
            availableSlots.addAll(slots);
        }

        public int getNextAvailableSlot() {
            if (availableSlots.isEmpty()) return -1;
            return availableSlots.removeFirst();
        }
    }
}

