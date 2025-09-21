package dev.tqqn.modules.game.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.GameStates;
import dev.tqqn.utils.ChatUtils;
import dev.tqqn.utils.Notify;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

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
            Notify.LIST.chat(player, "<hover:show_text:'<#9eb6ff>| Freeze an active game. <yellow><bold>NOT YET IMPLEMENTED.'>/bingo admin freeze");
            player.sendMessage(ChatUtils.format("<#ffdd94>Hover a command to see what it does."));
        }
    }

    @Subcommand("admin|ac")
    @CommandPermission("bingo.command.admin")
    @Description("Admin commands for Bingo.")
    public final class AdminCommands extends BaseCommand {

        @Subcommand("forcestart|start")
        @CommandPermission("bingo.command.admin.forcestart")
        @Description("Force start a game.")
        public void forceStart(Player player) {
            gameModule.getCurrentInstance().changeState(GameStates.ACTIVE);
            Notify.SUCCESS.chat(player, "You successfully started the game by force.");
        }
    }
}
