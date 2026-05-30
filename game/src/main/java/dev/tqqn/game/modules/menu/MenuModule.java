package dev.tqqn.game.modules.menu;

import dev.tqqn.game.BingoMain;
import dev.tqqn.game.modules.AbstractModule;
import dev.tqqn.game.modules.menu.framework.listeners.MenuListener;

public final class MenuModule extends AbstractModule {

    public MenuModule(BingoMain plugin) {
        super(plugin, "Menu");
    }

    @Override
    protected void onEnable() {
        register(new MenuListener());
    }
}
