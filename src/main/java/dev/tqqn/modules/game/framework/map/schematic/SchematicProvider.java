package dev.tqqn.modules.game.framework.map.schematic;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import dev.tqqn.modules.game.GameModule;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class SchematicProvider {

    private final GameModule gameModule;

    public List<File> loadSchematicFiles(File schematicDirectory) {
        final List<File> schematicFiles = new ArrayList<>();

        if (!schematicDirectory.exists() && !schematicDirectory.isDirectory()) return schematicFiles;

        final File[] files = schematicDirectory.listFiles();

        if (files == null) return schematicFiles;
        for (File file : files) {
            if (file.getName().endsWith(".schem")) schematicFiles.add(file);
        }
        return schematicFiles;
    }

    public CompletableFuture<Clipboard> loadSchematic(File schematicFile) throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            final ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(schematicFile);

            if (clipboardFormat == null) throw new IllegalArgumentException("This format is not supported: " + schematicFile.getName());

            try (final ClipboardReader reader = clipboardFormat.getReader(new FileInputStream(schematicFile))) {
                return reader.read();
            } catch (IOException ignored) {
                return null;
            }
        });
    }

    public CompletableFuture<List<SchemBlockLocation>> pasteSchematic(Location location, Clipboard clipboard, int rotation) {
        final World world = FaweAPI.getWorld(location.getWorld().getName());
        final CompletableFuture<List<SchemBlockLocation>> future = new CompletableFuture<>();
        if (world == null) {
            gameModule.getLogger().warning("Paste failed: " +
                    "World " + location.getWorld().getName() + " not found.");
            future.complete(List.of());
            return future;
        }

        final List<SchemBlockLocation> schematicLocations = new ArrayList<>();
        final EditSession editSession = WorldEdit.getInstance().newEditSession(world);
        final BlockVector3 origin = clipboard.getOrigin();

        final BlockVector3 to =
                BlockVector3.at(
                        Math.floor(location.getX()),
                        Math.floor(location.getY()),
                        Math.floor(location.getZ())
                );

        final ClipboardHolder holder = new ClipboardHolder(clipboard);

        if (rotation > 0) {
            final Transform transform = new AffineTransform().rotateY(rotation);
            holder.setTransform(transform);
        }

        final CompletableFuture<Operation> operationFuture =
                CompletableFuture.supplyAsync(() -> holder
                        .createPaste(editSession)
                        .to(to)
                        .ignoreAirBlocks(true)
                        .build());

        try {
            Operations.complete(operationFuture.join());
            editSession.close();
        } catch (WorldEditException e) {
            gameModule.getLogger().warning("Failed to paste schematic: " + e.getMessage());
        }

        final BlockVector3 minPos = clipboard.getMinimumPoint();
        final BlockVector3 maxPos = clipboard.getMaximumPoint();
        final BlockVector3 size = clipboard.getDimensions();

        final int minChunkX = (int) Math.floor((to.x() + minPos.x() - origin.x()) / 16.0);
        final int maxChunkX = (int) Math.ceil((to.x() + maxPos.x() - origin.x()) / 16.0);
        final int minChunkZ = (int) Math.floor((to.z() + minPos.z() - origin.z()) / 16.0);
        final int maxChunkZ = (int) Math.ceil((to.z() + maxPos.z() - origin.z()) / 16.0);

        final List<Chunk> loadedChunks = new ArrayList<>();

        final org.bukkit.World bukkitWorld = location.getWorld();

        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                final org.bukkit.Chunk chunk = bukkitWorld.getChunkAt(chunkX, chunkZ);
                chunk.addPluginChunkTicket(gameModule.getPlugin());
                loadedChunks.add(chunk);
            }
        }

        final BlockVector3 minRelight = BlockVector3.at(to.x(), to.y(), to.z());
        final BlockVector3 maxRelight = BlockVector3.at(to.x() + size.x(), to.y() + size.y(), to.z() + size.z());

        Bukkit.getScheduler().runTaskLater(gameModule.getPlugin(), () -> {
            for (int x = minRelight.x(); x <= maxRelight.x(); x++) {
                for (int z = minRelight.z(); z <= maxRelight.z(); z++) {
                    for (int y = minRelight.y(); y <= maxRelight.y(); y++) {
                        final Block block = bukkitWorld.getBlockAt(x, y, z);
                        block.getState().update(true, false);
                    }
                }
            }

            for (BlockVector3 pos : clipboard.getRegion()) {
                int dx = pos.x() - origin.x();
                int dy = pos.y() - origin.y();
                int dz = pos.z() - origin.z();

                int realX = to.x() + dx;
                int realY = to.y() + dy;
                int realZ = to.z() + dz;

                final SchemBlockLocation blockLocation = new SchemBlockLocation(realX, realY, realZ, world.getName());

                schematicLocations.add(blockLocation);
            }
            future.complete(schematicLocations);
            loadedChunks.forEach(chunk -> chunk.removePluginChunkTicket(gameModule.getPlugin()));
        }, 1L);
        return future;
    }

    public CompletableFuture<List<Location>> provideLocationByBlock(org.bukkit.World world, List<SchemBlockLocation> blockLocations, Material type, boolean shouldRemoveType) {
        final CompletableFuture<List<Location>> future = new CompletableFuture<>();
        if (blockLocations.isEmpty()) {
            future.complete(List.of());
            return future;
        }

        if (world == null) {
            future.complete(List.of());
            return future;
        }

        final List<Chunk> loadedChunks = new ArrayList<>();
        final List<Location> cachedLocations = new ArrayList<>();

        for (SchemBlockLocation blockLocation : blockLocations) {
            final Location location = new Location(world, blockLocation.x(), blockLocation.y(), blockLocation.z());
            final Chunk chunk = location.getChunk();
            if (!loadedChunks.contains(chunk)) {
                chunk.addPluginChunkTicket(gameModule.getPlugin());
                loadedChunks.add(chunk);
            }

            cachedLocations.add(location);
        }

        final List<Location> wantedLocations = new ArrayList<>();

        Bukkit.getScheduler().runTaskLater(gameModule.getPlugin(), () -> {
            for (Location location : cachedLocations) {
                if (location.getBlock().getType() == type) {
                    wantedLocations.add(location);
                    if (shouldRemoveType) location.getBlock().setType(Material.AIR);
                }
            }

            future.complete(wantedLocations);

            for (Chunk chunk : loadedChunks) {
                chunk.removePluginChunkTicket(gameModule.getPlugin());
            }
        }, 2L);

        return future;
    }

}
