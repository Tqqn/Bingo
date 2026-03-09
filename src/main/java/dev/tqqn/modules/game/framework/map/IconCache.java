package dev.tqqn.modules.game.framework.map;

import dev.tqqn.BingoMain;
import lombok.RequiredArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public final class IconCache {

    private final Map<String, BufferedImage> cache = new HashMap<>();

    private final BingoMain plugin;

    public BufferedImage getIcon(String iconName) {
        return cache.computeIfAbsent(iconName, i -> loadPng("/icons/" + iconName).join());
    }

    private CompletableFuture<BufferedImage> loadPng(String path) {
        return CompletableFuture.supplyAsync(() -> {
            BufferedImage image;

            try {
                image = ImageIO.read(new File(plugin.getDataFolder(), path));
            } catch (IOException e) {
                return null;
            }
            return image;
        });
    }
}
