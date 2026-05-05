package dev.tqqn.modules.game.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.menu.recipe.PagedRecipeMenu;
import dev.tqqn.modules.game.framework.states.abstraction.AbstractState;
import dev.tqqn.modules.game.framework.states.lobby.LobbyState;
import dev.tqqn.utils.ChatUtils;
import dev.tqqn.utils.Notify;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.List;
import java.util.Map;

@CommandAlias("bingo")
@CommandPermission("bingo.command.bingo")
@RequiredArgsConstructor
public final class BingoCommands extends BaseCommand {

    private final GameModule gameModule;

    @Default
    @Description("Default help")
    public void help(Player player) {
        Notify.INFO.chat(player, "---- <primary>Bingo Commands <default>----");
        Notify.LIST.chat(player, "No commands yet.");
        if (player.hasPermission("bingo.command.admin")) {
            player.sendMessage(ChatUtils.format(" "));
            Notify.INFO.chat(player, "---- <primary>Admin Commands <default>----");
            Notify.LIST.chat(player, "<hover:show_text:'<#9eb6ff>| Force start a bingo game.'>/bingo admin forcestart");
            Notify.LIST.chat(player, "<hover:show_text:'<#9eb6ff>| Freeze an active game.'>/bingo admin freeze");
            Notify.LIST.chat(player, "<hover:show_text:'<#9eb6ff>| Cycle to the next stage.'>/bingo admin nextstate");
            Notify.LIST.chat(player, "<hover:show_text:'<#9eb6ff>| Cycle to the previous stage.'>/bingo admin previousstate");
            player.sendMessage(ChatUtils.format("<#ffdd94>Hover a command to see what it does."));
        }
    }

    @Subcommand("admin|ac")
    @CommandPermission("bingo.command.admin")
    @Description("Admin commands for Bingo.")
    public final class AdminCommands extends BaseCommand {

        @Subcommand("nextstate")
        @CommandPermission("bingo.command.admin.nextstate")
        @Description("Cycle to the next game state.")
        public void nextState(Player player) {
            final AbstractState currentState = gameModule.getCurrentInstance().getCurrentState().get();

            if (currentState == null) {
                Notify.ERROR.chat(player, "Something went wrong. The current State is not existing. Contact an administrator.");
                return;
            }

            final AbstractState nextState = gameModule.getCurrentInstance().getNextState();

            if (nextState == null) {
                Notify.ERROR.chat(player, "There is no next state available. Cancelling this command...");
                return;
            }

            gameModule.getCurrentInstance().nextState();
            Notify.SUCCESS.chat(player, "You cycled the game to the " + nextState.getGameState().name() + " state.");
        }

        @Subcommand("previousstate")
        @CommandPermission("bingo.command.admin.previousstate")
        @Description("Force start a game.")
        public void previousState(Player player) {
            final AbstractState currentState = gameModule.getCurrentInstance().getCurrentState().get();

            if (currentState == null) {
                Notify.ERROR.chat(player, "Something went wrong. The current State is not existing. Contact an administrator.");
                return;
            }

            final AbstractState previousState = gameModule.getCurrentInstance().getPreviousState();

            if (previousState == null) {
                Notify.ERROR.chat(player, "There is no previous state available. Cancelling this command...");
                return;
            }

            gameModule.getCurrentInstance().previousState();
            Notify.SUCCESS.chat(player, "You cycled the game backwards to the " + previousState.getGameState().name() + " state.");
        }

        @Subcommand("freeze")
        @CommandPermission("bingo.command.admin.freeze")
        @Description("Freeze the current state.")
        public void freeze(Player player) {
            gameModule.getCurrentInstance().freeze();
        }

        @Subcommand("quick-start")
        @CommandPermission("bingo.command.admin.quick-start")
        @Description("Quick start the current game")
        public void quickStart(Player player) {
            if (!(gameModule.getCurrentInstance().getCurrentState().get() instanceof LobbyState lobbyState)) {
                Notify.ERROR.chat(player, "The current state does not support quick start.");
                return;
            }
            lobbyState.quickStart();
            Notify.SUCCESS.chat(player, "You quick started the game.");
        }

        @Subcommand("debug")
        @CommandPermission("bingo.command.admin.debug")
        @Description("Debug Command - Temporary...")
        public void debug(Player player) {
            final ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType() == Material.AIR) {
                Notify.ERROR.chat(player, "This Item Type is not valid for this command.");
                return;
            }

            final List<Recipe> recipes = Bukkit.getServer().getRecipesFor(itemStack);
            for (Recipe recipe : recipes) {
                if (recipe instanceof ShapedRecipe shapedRecipe) {
                    Notify.LIST.chat(player, "<gold>--------");
                    for (String value : shapedRecipe.getShape()) {
                        player.sendMessage(ChatUtils.format("<blue>!" + value + "!"));
                    }
                    Notify.INFO.chat(player, "<yellow>---");
                    for (Map.Entry<Character, RecipeChoice> set : shapedRecipe.getChoiceMap().entrySet()) {
                        player.sendMessage(ChatUtils.format("<blue>" + set.getKey() + " " + set.getValue()));
                    }
                }

                if (recipe instanceof ShapelessRecipe shapelessRecipe) {
                    for (RecipeChoice recipeChoice : shapelessRecipe.getChoiceList()) {
                        if (recipeChoice instanceof RecipeChoice.MaterialChoice materialChoice) {
                            player.sendMessage(ChatUtils.format("<blue>" + materialChoice.getItemStack()));
                        }
                    }
                }
            }
        }

        @Subcommand("recipe")
        @CommandPermission("bingo.command.admin.recipe")
        @Description("Show Item Recipe")
        public void recipe(Player player) {
            final ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType() == Material.AIR) {
                Notify.ERROR.chat(player, "This Item Type is not valid for this command.");
                return;
            }

            final List<Recipe> recipes = Bukkit.getServer().getRecipesFor(itemStack);
            if (recipes.isEmpty()) {
                Notify.ERROR.chat(player, "Item has not valid recipe.");
                return;
            }

            new PagedRecipeMenu(player, itemStack).open();
        }
    }
}
