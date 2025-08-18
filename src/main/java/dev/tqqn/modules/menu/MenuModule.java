package dev.tqqn.modules.menu;

import dev.tqqn.TemplateMain;
import dev.tqqn.modules.AbstractModule;
import dev.tqqn.modules.menu.framework.listeners.MenuListener;

public final class MenuModule extends AbstractModule {

    public MenuModule(TemplateMain plugin) {
        super(plugin, "Menu");
    }

    @Override
    protected void onEnable() {
        register(new MenuListener());
    }
}
