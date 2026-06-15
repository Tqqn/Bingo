package dev.tqqn.game;

import co.aikar.commands.PaperCommandManager;
import dev.tqqn.common.GamePlugin;
import dev.tqqn.common.modular.PluginProvider;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class BingoMain extends JavaPlugin implements GamePlugin<BingoMain> {

    @Getter
    private static final String PREFIX = "[Bingo]";
    private static BingoMain INSTANCE;

    private BingoModuleManager moduleManager;
    private PaperCommandManager commandManager;

    @Override
    public void onLoad() {
        INSTANCE = this;
        moduleManager = new BingoModuleManager(this);
        moduleManager.load();
        PluginProvider.register(this);
    }


    @Override
    public void onEnable() {
        commandManager = new PaperCommandManager(this);
        moduleManager.init();
    }

    @Override
    public void onDisable() {
        moduleManager.disable();
        PluginProvider.unregister();
    }

    public static BingoMain getInstance() {
        return INSTANCE;
    }

    @Override
    public Plugin getPlugin() {
        return this;
    }

    @Override
    public BingoModuleManager getModuleManager() {
        return moduleManager;
    }
}
