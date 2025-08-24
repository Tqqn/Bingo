package dev.tqqn.modules.game.framework.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
@Getter
public final class BingoTask {

    private final String name;
    private final ItemStack goal;
    private final String png;

}
