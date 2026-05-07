package dev.tqqn.modules.game.framework.visualizer;

import dev.tqqn.modules.game.framework.objects.BingoPlacement;
import dev.tqqn.modules.game.framework.objects.BingoTask;
import dev.tqqn.modules.game.framework.states.active.ActiveState;
import dev.tqqn.modules.game.framework.team.GameTeam;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public final class BingoMapRenderer extends MapRenderer {

    private final IconCache iconCache;

    private BingoTask[][] grid;

    public BingoMapRenderer(ActiveState state, IconCache iconCache) {
        this.iconCache = iconCache;

        grid = new BingoTask[5][5];

        for (Map.Entry<BingoTask, BingoPlacement> entry : state.getGameInstance().getBingoPlacements().entrySet()) {
            BingoPlacement placement = entry.getValue();
            grid[placement.getRow()][placement.getColumn()] = entry.getKey();
        }
    }

    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        final int cellSize = 24;
        final int offset = 4;
        final int iconSize = 16;
        final int pad = (cellSize - iconSize) / 2;

        for (int x = 0; x < 128; x++) {
            for (int y = 0; y < 128; y++) {
                mapCanvas.setPixelColor(x, y, new Color(237, 226, 194));
            }
        }

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {

                BingoTask task = grid[row][col];
                if (task == null) {
                    continue;
                }

                BufferedImage image = iconCache.getIcon(task.getPng());

                int cellX = offset + col * cellSize;
                int cellY = offset + row * cellSize;

                if (!task.getCompleted().isEmpty()) {
                    for (int dx = 1; dx < cellSize; dx++) {
                        for (int dy = 1; dy < cellSize; dy++) {
                            mapCanvas.setPixelColor(cellX + dx, cellY + dy, new Color(114, 207, 112));
                        }
                    }
                }

                if (image == null) {
                    System.out.println("MISSING ICON: " + task.getPng() + " at row=" + row + " col=" + col);
                } else {
                    drawImageTransparent(mapCanvas, cellX + pad + 1, cellY + pad, image);
                }

                for (GameTeam gameTeam : task.getCompleted()) {
                    drawCross(mapCanvas, cellX + 1, cellY + 1, gameTeam.getMapPlace(), gameTeam.getData().teamType().getMapColor());
                }
            }
        }

        drawGrid(mapCanvas);
    }

    private void drawImageTransparent(MapCanvas canvas, int startX, int startY, BufferedImage image) {
        for (int ix = 0; ix < image.getWidth(); ix++) {
            for (int iy = 0; iy < image.getHeight(); iy++) {
                int argb = image.getRGB(ix, iy);
                int alpha = (argb >> 24) & 0xFF;
                if (alpha < 128) continue;

                canvas.setPixelColor(startX + ix, startY + iy, new Color(argb, true));
            }
        }
    }

    private void drawGrid(MapCanvas canvas) {
        for (int i = 0; i <= 5; i++) {
            int pos = 4 + (i * 24);

            for (int p = 4; p < 125; p++) {
                canvas.setPixelColor(pos, p, Color.BLACK);
                canvas.setPixelColor(p, pos, Color.BLACK);
            }
        }
    }

    private void drawCross(MapCanvas canvas, int x, int y, int place, Color color) {
        int size = 11;

        switch (place) {
            case 1 -> {
                for (int i = 0; i <= size; i++) {
                    canvas.setPixelColor(x, y + i, color);
                    canvas.setPixelColor(x + 1, y + i, color);
                    canvas.setPixelColor(x + i, y, color);
                    canvas.setPixelColor(x + i, y + 1, color);
                }
            }
            case 2 -> {
                y = y + 11;
                for (int i = 0; i <= size; i++) {
                    canvas.setPixelColor(x, y + i, color);
                    canvas.setPixelColor(x + 1, y + i, color);
                    canvas.setPixelColor(x + i, y + size, color);
                    canvas.setPixelColor(x + i, y + size + 1, color);
                }
            }
            case 3 -> {
                x = x + 22;
                y = y + 22;
                for (int i = 0; i <= size; i++) {
                    canvas.setPixelColor(x - i, y, color);
                    canvas.setPixelColor(x - i, y - 1, color);
                    canvas.setPixelColor(x, y - i, color);
                    canvas.setPixelColor(x - 1, y - i, color);
                }
            }
            case 4 -> {
                x = x + 22;
                for (int i = 0; i <= size; i++) {
                    canvas.setPixelColor(x - i, y, color);
                    canvas.setPixelColor(x - i, y + 1, color);
                    canvas.setPixelColor(x, y + i, color);
                    canvas.setPixelColor(x - 1, y + i, color);
                }
            }
        }


    }
}
