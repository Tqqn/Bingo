package dev.tqqn;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class BingoMain extends JavaPlugin {

    @Getter
    private static final String PREFIX = "[Bingo]";
    private static BingoMain INSTANCE;

    private PaperCommandManager commandManager;
    private ModuleManager moduleManager;

    @Override
    public void onLoad() {
        INSTANCE = this;
        moduleManager = ModuleManager.getInstance(this);
        moduleManager.load();
    }


    @Override
    public void onEnable() {
        commandManager = new PaperCommandManager(this);

        moduleManager.init();

    }

    @Override
    public void onDisable() {
        moduleManager.disable();
    }

    public static BingoMain getInstance() {
        return INSTANCE;
    }
}
