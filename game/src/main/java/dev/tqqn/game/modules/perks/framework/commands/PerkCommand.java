package dev.tqqn.game.modules.perks.framework.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import dev.tqqn.game.modules.database.player.BingoPlayerModel;
import dev.tqqn.game.modules.perks.PerkModule;
import dev.tqqn.game.modules.perks.framework.menu.PerkSelectorMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("perks|perk")
@CommandPermission("bingo.command.perk")
@Description("Perk Commands")
public final class PerkCommand extends BaseCommand {

    private final PerkModule perkModule;

    public PerkCommand(PerkModule perkModule) {
        this.perkModule = perkModule;
    }

    @HelpCommand
    public void showHelp(CommandSender sender, CommandHelp commandHelp) {
        commandHelp.showHelp();
    }

    @Default
    @Description("Open perk menu.")
    public void open(Player player) {
        final BingoPlayerModel playerModel = BingoPlayerModel.from(player);
        if (playerModel == null) return;
        new PerkSelectorMenu(player).open();
    }

}
