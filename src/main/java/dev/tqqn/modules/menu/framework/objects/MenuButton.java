package dev.tqqn.modules.menu.framework.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@Getter
public final class MenuButton {

    private final ItemStack itemStack;

    private Consumer<Player> clicker = null;

    public MenuButton(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public MenuButton setClicker(Consumer<Player> consumer) {
        if (consumer == null) return this;
        this.clicker = consumer;
        return this;
    }

}