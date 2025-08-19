package dev.tqqn.modules.perks.framework.menu;

import dev.tqqn.BingoMain;
import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.menu.framework.objects.Menu;
import dev.tqqn.modules.menu.framework.objects.MenuButton;
import dev.tqqn.modules.perks.PerkModule;
import dev.tqqn.modules.perks.framework.AbstractPerk;
import dev.tqqn.utils.ItemBuilder;
import org.bukkit.entity.Player;

import java.util.List;

public final class PerkSelectorMenu extends Menu {

    private static final PerkModule perkModule = BingoMain.getInstance().getModuleManager().getModule(PerkModule.class);

    private final PlayerModel playerModel;

    public PerkSelectorMenu(Player viewer) {
        super("<red>Perk Selector", 3*9, viewer);
        this.playerModel = PlayerModel.from(viewer);
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
        final boolean hasPerkSelected = playerModel.getBingoData().getSelectedPerk().equals(abstractPerk);
        if (hasPerkSelected) {
            itemBuilder.addLore("<green>You already selected this perk.");
        } else {
            itemBuilder.addLore("<yellow>[Interact] to select this perk.");
        }

        final MenuButton menuButton = new MenuButton(itemBuilder.build());
        menuButton.setClicker(player -> {
            if (hasPerkSelected) return;
            playerModel.getBingoData().updatePerk(abstractPerk, player.getUniqueId());
            reload();
        });

        return menuButton;
    }
}
