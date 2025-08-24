package dev.tqqn;

import dev.tqqn.modules.AbstractModule;
import dev.tqqn.modules.database.DatabaseModule;
import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.menu.MenuModule;
import dev.tqqn.modules.perks.PerkModule;
import dev.tqqn.modules.retriever.RetrieverModule;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ModuleManager {

    private static ModuleManager INSTANCE;

    private final Map<Class<? extends AbstractModule>, AbstractModule> modules = new LinkedHashMap<>();

    private ModuleManager(BingoMain bingoMain) {
        INSTANCE = this;
        addModule(new DatabaseModule(bingoMain));
        addModule(new MenuModule(bingoMain));
        addModule(new RetrieverModule(bingoMain));
        addModule(new GameModule(bingoMain, getModule(DatabaseModule.class)));
        addModule(new PerkModule(bingoMain));
    }

    public static ModuleManager getInstance(BingoMain bingoMain) {
        if (INSTANCE == null) {
            INSTANCE = new ModuleManager(bingoMain);
        }
        return INSTANCE;
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

    private void addModule(AbstractModule abstractModule) {
        modules.put(abstractModule.getClass(), abstractModule);
    }

    /**
     * Retrieves a module instance by its class.
     *
     * @param moduleClass The class of the module to retrieve.
     * @return The module instance.
     */

    public <M extends AbstractModule> M getModule(Class<M> moduleClass) {
        return moduleClass.cast(this.modules.get(moduleClass));
    }
}
