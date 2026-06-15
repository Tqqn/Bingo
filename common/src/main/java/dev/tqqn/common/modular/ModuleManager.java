package dev.tqqn.common.modular;

import dev.tqqn.common.GamePlugin;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ModuleManager<T extends JavaPlugin & GamePlugin<T>> {

    private final Map<Class<? extends AbstractModule>, AbstractModule> modules = new LinkedHashMap<>();
    @Getter private final T plugin;

    protected ModuleManager(T plugin) {
        this.plugin = plugin;
    }

    public void load() {
        this.modules.values().forEach(AbstractModule::load);
    }

    /**
     * Initializes all modules.
     */
    public void init() {
        this.registerModules();
    }

    /**
     * Disables all modules.
     */
    public void disable() {
        this.unregisterModules();
    }

    /**
     * Registers all modules by enabling them.
     */
    private void registerModules() {
        this.modules.values().forEach(AbstractModule::enable);
    }

    /**
     * Unregisters all modules by disabling them and clearing the module map.
     */
    private void unregisterModules() {
        this.modules.values().forEach(AbstractModule::disable);
        this.modules.clear();
    }

    public void addModule(AbstractModule abstractModule) {
        modules.put(abstractModule.getClass(), abstractModule);
    }

    /**
     * Retrieves a module instance by its class.
     *
     * @param moduleClass The class of the module to retrieve.
     * @return The module instance.
     */

    @SuppressWarnings("unchecked")
    public <M extends AbstractModule> M getModule(Class<M> moduleClass) {
        final AbstractModule module = modules.get(moduleClass);
        if (module == null) return null;
        return moduleClass.cast(module);
    }
}
