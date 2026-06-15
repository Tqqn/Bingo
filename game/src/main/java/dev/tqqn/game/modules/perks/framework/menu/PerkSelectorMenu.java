package dev.tqqn.game.modules.perks.framework.menu;

import dev.tqqn.common.menu.framework.objects.Menu;
import dev.tqqn.common.menu.framework.objects.MenuButton;
import dev.tqqn.game.BingoMain;
import dev.tqqn.game.modules.database.player.BingoPlayerModel;
import dev.tqqn.game.modules.game.framework.data.TempPlayerData;
import dev.tqqn.game.modules.perks.PerkModule;
import dev.tqqn.game.modules.perks.framework.AbstractPerk;
import dev.tqqn.game.utils.ItemBuilder;
import org.bukkit.entity.Player;

import java.util.List;

public final class PerkSelectorMenu extends Menu {

    private static final PerkModule perkModule = BingoMain.getInstance().getModuleManager().getModule(PerkModule.class);

    private final BingoPlayerModel playerModel;

    public PerkSelectorMenu(Player viewer) {
        super("<red>Perk Selector", 1, viewer);
        this.playerModel = BingoPlayerModel.from(viewer);
    }

    @Override
    public void reload() {
        final List<AbstractPerk> perks = perkModule.getPerks();
        int slot = 0;
        for (AbstractPerk perk : perks) {
            registerButton(getPerkButton(perk), slot);
            slot++;
        }
    }

    @Override
    public void onOpen() {}

    @Override
    public void onClose(Player viewer) {}

    private MenuButton getPerkButton(AbstractPerk abstractPerk) {
        final ItemBuilder itemBuilder = ItemBuilder.getBuilder(abstractPerk.getIcon());
        final TempPlayerData tempPlayerData = playerModel.getTempPlayerData();
        boolean hasPerkSelected;

        if (tempPlayerData.getSelectedPerk() != null) {
            hasPerkSelected = playerModel.getTempPlayerData().getSelectedPerk().equals(abstractPerk);
        } else {
            hasPerkSelected = false;
        }

        if (hasPerkSelected) {
            itemBuilder.addLore("<green>You already selected this perk.");
        } else {
            itemBuilder.addLore("<yellow>[Interact] to select this perk.");
        }

        final MenuButton menuButton = new MenuButton(itemBuilder.build());
        menuButton.setClicker(player -> {
            if (hasPerkSelected) return;
            playerModel.getTempPlayerData().updatePerk(abstractPerk);
            close();
        });

        return menuButton;
    }
}
