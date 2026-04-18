package dev.tqqn.modules.game.framework.map;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import dev.tqqn.modules.game.GameModule;
import dev.tqqn.modules.game.framework.map.schematic.SchemBlockLocation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public final class Arena {

    private final GameModule gameModule;
    private final String name;

    @Getter @Setter
    private boolean readyToJoin = false;

    private World arenaWorld;

    @Getter
    private Location spawnLocation;

    public void setUp() {
        setUp(1000);
    }

    public void setUp(int size) {
        CompletableFuture<World> future = createNewWorld();
        future.whenComplete((world, throwable) -> {
            arenaWorld = world;
            createWorldBorder(arenaWorld, size);
            final CompletableFuture<List<SchemBlockLocation>> pastedLocationFuture = pasteSpawnStructure(arenaWorld.getSpawnLocation());
            if (pastedLocationFuture == null) return;
            pastedLocationFuture.whenComplete((pastedLocations, throwable1) -> {
               setReadyToJoin(true);
               provideSpawnLocation(pastedLocations).whenComplete((locations, throwable2) -> {
                   if (locations.isEmpty()) {
                       spawnLocation = null;
                       return;
                   }
                   spawnLocation = locations.getFirst();
               });
            });
        });
    }

    private CompletableFuture<World> createNewWorld() {
        final World world = Bukkit.createWorld(new WorldCreator(name));
        if (world == null) {
            throw new RuntimeException("Failed to create world " + name);
        }

        return CompletableFuture.completedFuture(world);
    }

    private void createWorldBorder(World world, int size) {
        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setSize(size);
        world.getWorldBorder().setDamageAmount(2);
    }

    private CompletableFuture<List<SchemBlockLocation>> pasteSpawnStructure(Location location) {
        final Location spawnLocation = location.clone();
        final World world = location.getWorld();
        final Block highestBlock = world.getHighestBlockAt(location);
        if (highestBlock.getLocation().getBlockY() > 60) {
            spawnLocation.add(0,30, 0);
        }

        Clipboard clipboard;

        try {
            clipboard = gameModule.getSchematicProvider().loadSchematic(new File(gameModule.getPlugin().getDataFolder().getPath() + "/schematics/spawn.schem")).join();
        } catch (IOException ignored) {
            return null;
        }
        return gameModule.getSchematicProvider().pasteSchematic(spawnLocation, clipboard, 0);
    }

    private CompletableFuture<List<Location>> provideSpawnLocation(List<SchemBlockLocation> schematicLocations) {
        return gameModule.getSchematicProvider().provideLocationByBlock(arenaWorld, schematicLocations, Material.DIAMOND_BLOCK, true);
    }
}
