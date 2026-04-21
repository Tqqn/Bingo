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
import java.util.ArrayList;
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

    private final List<Location> spawnBorderLocations = new ArrayList<>();

    public void setUp() {
        setUp(1000);
    }

    public void setUp(int size) {
        CompletableFuture<World> future = createNewWorld();
        future.whenComplete((world, throwable) -> {
            arenaWorld = world;
            createWorldBorder(arenaWorld, size);
            final CompletableFuture<List<SchemBlockLocation>> pastedLocationFuture = pasteSpawnStructure(new Location(arenaWorld, 0, 100, 0));
            if (pastedLocationFuture == null) return;
            pastedLocationFuture.whenComplete((pastedLocations, throwable1) -> {
               setReadyToJoin(true);
               provideSpawnLocation(pastedLocations).whenComplete((locations, throwable2) -> {
                   if (locations.isEmpty()) {
                       spawnLocation = null;
                       return;
                   }
                   spawnLocation = locations.getFirst().toCenterLocation();
                   arenaWorld.setSpawnLocation(spawnLocation);
                   spawnLocation.getChunk().addPluginChunkTicket(gameModule.getPlugin());
               });

               provideSpawnBorderLocations(pastedLocations).whenComplete((locations, throwable2) -> {
                   spawnBorderLocations.addAll(locations);
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

    public void removeSpawnBorder() {
        spawnBorderLocations.forEach(location -> location.getBlock().setType(Material.AIR));
        spawnBorderLocations.clear();
    }

    private CompletableFuture<List<SchemBlockLocation>> pasteSpawnStructure(Location location) {
        final Location spawnLocation = location.clone();
        final World world = location.getWorld();
        final Block highestBlock = world.getHighestBlockAt(location);
        spawnLocation.setY(highestBlock.getLocation().getBlockY());
        spawnLocation.add(0,30, 0);

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

    private CompletableFuture<List<Location>> provideSpawnBorderLocations(List<SchemBlockLocation> schematicBlockLocations) {
        return gameModule.getSchematicProvider().provideLocationByBlock(arenaWorld, schematicBlockLocations, Material.BARRIER, false);
    }
}
