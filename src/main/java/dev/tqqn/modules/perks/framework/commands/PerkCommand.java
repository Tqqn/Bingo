package dev.tqqn.modules.perks.framework.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.perks.PerkModule;
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
        final PlayerModel playerModel = PlayerModel.from(player);
        if (playerModel == null) return;
        // TODO: Open Menu with perks.
    }

}
