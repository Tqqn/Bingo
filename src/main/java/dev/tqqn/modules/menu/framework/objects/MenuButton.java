package dev.tqqn.modules.menu.framework.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@Getter
public final class MenuButton {

    private final ItemStack itemStack;

    @Setter
    private Consumer<Player> clicker;

    public MenuButton(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

}