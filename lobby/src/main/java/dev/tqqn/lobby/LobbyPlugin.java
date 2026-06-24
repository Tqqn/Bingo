package dev.tqqn.lobby;

import co.aikar.commands.PaperCommandManager;
import dev.tqqn.common.GamePlugin;
import dev.tqqn.common.modular.ModuleManager;
import dev.tqqn.common.modular.PluginProvider;
import dev.tqqn.lobby.modules.BingoLobbyModuleManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class LobbyPlugin extends JavaPlugin implements GamePlugin<LobbyPlugin> {

    private static LobbyPlugin INSTANCE;

    private boolean shouldShutdown = false;

    private BingoLobbyModuleManager moduleManager;
    private PaperCommandManager commandManager;

    @Override
    public void onLoad() {
        INSTANCE = this;
        this.moduleManager = new BingoLobbyModuleManager(this);
        this.moduleManager.load();
        PluginProvider.register(this);
    }

    @Override
    public void onEnable() {
        if (shouldShutdown) {
            Bukkit.getScheduler().runTask(this, () -> Bukkit.getPluginManager().disablePlugin(this));
            return;
        }
        commandManager = new PaperCommandManager(this);
        this.moduleManager.init();
    }

    @Override
    public void onDisable() {
        PluginProvider.unregister();
    }

    public static LobbyPlugin getInstance() {
        return INSTANCE;
    }

    @Override
    public PaperCommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public Plugin getPlugin() {
        return this;
    }

    @Override
    public ModuleManager<LobbyPlugin> getModuleManager() {
        return moduleManager;
    }

    @Override
    public void setShutdown() {
        shouldShutdown = true;
    }
}
