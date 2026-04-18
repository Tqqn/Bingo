package dev.tqqn.modules.game.framework.visualizer;

import dev.tqqn.modules.game.framework.objects.BingoPlacement;
import dev.tqqn.modules.game.framework.objects.BingoTask;
import dev.tqqn.modules.game.framework.states.active.ActiveState;
import dev.tqqn.modules.game.framework.team.GameTeam;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public final class BingoMapRenderer extends MapRenderer {

    private final ActiveState state;
    private final IconCache iconCache;

    private BingoTask[][] grid;

    public BingoMapRenderer(ActiveState state, IconCache iconCache) {
        this.state = state;
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

        mapCanvas.getBasePixelColor(0, 0);

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {

                BingoTask task = grid[row][col];
                if (task == null) {
                    continue;
                }

                BufferedImage image = iconCache.getIcon(task.getPng());

                int cellX = offset + col * cellSize;
                int cellY = offset + row * cellSize;

                if (image == null) {
                    System.out.println("MISSING ICON: " + task.getPng() + " at row=" + row + " col=" + col);
                } else {
                    mapCanvas.drawImage(cellX + pad + 1, cellY + pad, image);
                }

                for (GameTeam gameTeam : task.getCompleted()) {
                    drawCross(mapCanvas, cellX + 1, cellY + 1, gameTeam.getMapPlace());
                }
            }
        }

        drawGrid(mapCanvas);
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

    private void drawCross(MapCanvas canvas, int x, int y, int place) {
        int size = 11;

        switch (place) {
            case 1 -> {
                for (int i = 0; i <= size; i++) {
                    canvas.setPixelColor(x, y + i, Color.GREEN);
                    canvas.setPixelColor(x + i, y, Color.GREEN);
                }
            }
            case 2 -> {
                y = y + 11;
                for (int i = 0; i <= size; i++) {
                    canvas.setPixelColor(x, y + i, Color.RED);
                    canvas.setPixelColor(x + i, y + size, Color.RED);
                }
            }
            case 3 -> {
                x = x + 22;
                y = y + 22;
                for (int i = 0; i <= size; i++) {
                    canvas.setPixelColor(x - i, y, Color.BLUE);
                    canvas.setPixelColor(x, y - i, Color.BLUE);
                }
            }
            case 4 -> {
                x = x + 22;
                for (int i = 0; i <= size; i++) {
                    canvas.setPixelColor(x - i, y, Color.ORANGE);
                    canvas.setPixelColor(x, y + i, Color.ORANGE);
                }
            }
        }


    }
}
