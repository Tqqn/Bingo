package dev.tqqn.common.modular;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractModule<T extends ModuleManager<?>> {

    @Getter
    private final T moduleManager;
    private PaperCommandManager commandManager;
    @Getter private final ModuleLogger logger;

    private final Set<Listener> activeListeners = new HashSet<>();
    private final Set<BaseCommand> commands = new HashSet<>();

    @Getter private final String name;

    @Setter private boolean shouldDisablePluginOnFail = false;

    public AbstractModule(T moduleManager, String name) {
        this.moduleManager = moduleManager;
        this.logger = new ModuleLogger(moduleManager.getPlugin(),  name);
        this.name = name;
    }

    /**
     * Loads the module.
     */
    public boolean load() {
        logger.log(Level.INFO, "Is loading...");
        onLoad();

        if (testDisableOnFail()) return false;

        logger.log(Level.INFO, "Finished loading!");
        return true;
    }

    /**
     * Enables the module by registering listeners and commands.
     */
    public boolean enable() {
        logger.log(Level.INFO, "Is enabling...");
        this.commandManager = moduleManager.getPlugin().getCommandManager();
        onEnable();

        if (testDisableOnFail()) return false;

        registerListeners();
        registerCommands();
        logger.log(Level.INFO, "Finished enabling!");
        return true;
    }

    /**
     * Disables the module by unregistering listeners and commands.
     */
    public void disable() {
        logger.log(Level.INFO, "Is disabling...");
        onDisable();
        unRegisterListeners();
        logger.log(Level.INFO, "Finished disabling!");
    }

    /**
     * Called when the module is being loaded.
     */
    protected void onLoad() {
    }

    /**
     * Called when the module is being enabled.
     */
    protected void onEnable() {
    }

    /**
     * Called when the module is being disabled.
     */
    protected void onDisable() {
    }

    public void register(Object object) {
        if (object instanceof Listener listener) {
            activeListeners.add(listener);
            return;
        }

        if (object instanceof BaseCommand baseCommand) {
            commands.add(baseCommand);
        }
    }

    /**
     * Registers listeners for the module.
     */
    private void registerListeners() {
        if (activeListeners.isEmpty()) return;
        final PluginManager pluginManager = moduleManager.getPlugin().getServer().getPluginManager();
        activeListeners.forEach(listener -> {
            pluginManager.registerEvents(listener, moduleManager.getPlugin());
            logger.log(Level.INFO, "Has registered listener: " + listener.getClass());
        });
    }

    /**
     * Registers commands for the module.
     */
    private void registerCommands() {
        if (commands.isEmpty()) return;
        commands.forEach(baseCommand -> {
            commandManager.registerCommand(baseCommand);
            logger.log(Level.INFO, "Has registered command: " + baseCommand.getName());
        });
    }

    /**
     * Unregisters listeners for the module.
     */
    private void unRegisterListeners() {
        if (activeListeners.isEmpty()) return;
        activeListeners.forEach(listener -> {
            HandlerList.unregisterAll(listener);
            moduleManager.getPlugin().getLogger().info("Has unregistered listener: " + listener.getClass());
        });
    }

    private boolean testDisableOnFail() {
        if (shouldDisablePluginOnFail) {
            logger.log(Level.SEVERE, "Failed to load module - shutting down plugin");
            moduleManager.getPlugin().setShutdown();
            return true;
        }
        return false;
    }

    public static class ModuleLogger extends Logger {

        ModuleLogger(Plugin plugin, String prefix) {
            super("Bingo - Module - " + prefix, null);
            setParent(plugin.getLogger());
            setLevel(Level.ALL);
        }

        @Override
        public void log(Level level, String message) {
            super.log(level, message);
        }
    }

}
