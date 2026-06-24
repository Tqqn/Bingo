package dev.tqqn.common.menu;

import dev.tqqn.common.menu.framework.listeners.MenuListener;
import dev.tqqn.common.modular.AbstractModule;
import dev.tqqn.common.modular.ModuleManager;

public final class MenuModule extends AbstractModule<ModuleManager<?>> {

    public MenuModule(ModuleManager<?> moduleManager) {
        super(moduleManager, "Menu");
    }

    @Override
    protected void onEnable() {
        register(new MenuListener());
    }
}
