package dev.tqqn.modules.database.framework.config;

import dev.tqqn.modules.game.framework.objects.BingoTask;
import dev.tqqn.modules.database.DatabaseModule;
import dev.tqqn.modules.database.framework.objects.AbstractConfig;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class BingoTaskConfig extends AbstractConfig {

    public BingoTaskConfig(DatabaseModule dataModule) {
        super("tasks", dataModule);
    }

    public List<BingoTask> getAllTasks() {
        final List<BingoTask> tasks = new ArrayList<>();
        tasks.addAll(getTasks("easy"));
        tasks.addAll(getTasks("hard"));
        return tasks;
    }

    private List<BingoTask> getTasks(String type) {
        final ConfigurationSection section = getConfiguration().getConfigurationSection(type);
        final List<BingoTask> tasks = new ArrayList<>();

        if (section == null) {
            getDatabaseModule().getLogger().log(Level.INFO, "No valid " + type + " tasks found. Returning empty list.");
            return List.of();
        }

        for (String task : section.getKeys(true)) {
            final String pathItem = type + "." + task + ".item";
            final String possibleMaterial = section.getString(pathItem);
            Material material;
            try {
                material = Material.valueOf(section.getString(pathItem));
            } catch (IllegalArgumentException | NullPointerException e) {
                getDatabaseModule().getLogger().log(Level.SEVERE, "No valid material found at: " +  pathItem + ": " + possibleMaterial);
                continue;
            }

            ItemStack itemStack = new ItemStack(material);
            String png = section.getString(type + "." + task + ".png");
            tasks.add(new BingoTask(task, itemStack, png));
        }

        return tasks;
    }
}
