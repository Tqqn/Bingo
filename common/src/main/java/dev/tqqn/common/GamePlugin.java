package dev.tqqn.common;

import co.aikar.commands.PaperCommandManager;
import dev.tqqn.common.modular.ModuleManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 06/06/2026
 */
public interface GamePlugin<P extends JavaPlugin & GamePlugin<P>> {

    PaperCommandManager getCommandManager();
    Plugin getPlugin();

    ModuleManager<P> getModuleManager();

}
