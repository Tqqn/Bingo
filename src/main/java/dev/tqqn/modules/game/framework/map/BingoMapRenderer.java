package dev.tqqn.modules.game.framework.map;

import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.modules.game.framework.objects.BingoPlacement;
import dev.tqqn.modules.game.framework.objects.BingoTask;
import dev.tqqn.modules.game.framework.states.active.ActiveState;
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

    private final ActiveState state;
    private final IconCache iconCache;

    private BingoTask[][] grid;

    public BingoMapRenderer(ActiveState state, IconCache iconCache) {
        this.state = state;
        this.iconCache = iconCache;

        grid = new BingoTask[6][6];


        for (Map.Entry<BingoTask, BingoPlacement> entry : state.getGameInstance().getGameStateSeries().getBingoPlacements().entrySet()) {
            BingoPlacement placement = entry.getValue();
            grid[placement.getRow()][placement.getColumn()] = entry.getKey();
        }
    }

    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
        final PlayerModel playerModel = PlayerModel.from(player);

        final int cellSize = 25;   // 5*25 = 125 fits
        final int offset = 1;      // margin (125 + 2 = 127)
        final int iconSize = 16;
        final int pad = (cellSize - iconSize) / 2; // 4

        // optional: clear background
        mapCanvas.getBasePixelColor(0, 0);

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {

                BingoTask task = grid[row][col];
                if (task == null) {
                    System.out.println("Task is null");
                    continue;
                }

                BufferedImage image = iconCache.getIcon(task.getPng());

                int cellX = offset + col * cellSize;
                int cellY = offset + row * cellSize;

                // draw icon centered in the cell
                if (image == null) {
                    System.out.println("MISSING ICON: " + task.getPng() + " at row=" + row + " col=" + col);
                } else {
                    mapCanvas.drawImage(cellX + pad, cellY + pad, image);
                }

                boolean done = playerModel.getTempPlayerData().hasCompleted(task);

                // OPTIONAL: overlay if done (border or tint)
                // if (done) drawCellBorder(mapCanvas, row, col, cellSize, MapPalette.GOLD, offset);
            }
        }

        drawGrid(mapCanvas, 25);
    }

//    @Override
//    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {
//        final PlayerModel playerModel = PlayerModel.from(player);
//
//        for (int row = 1; row < 6; row++) {
//            for (int col = 1; col < 6; col++) {
//                int cellSize = 25;
//
//                if (row == 1 || col == 1) {
//                    cellSize = 0;
//                }
//
//                BingoTask task = grid[row][col];
//                if (task == null) continue;
//
//                BufferedImage image = iconCache.getIcon(task.getPng());
//
//                int cellX = col * cellSize;
//                int cellY = row * cellSize;
//
//
//                if (image != null) {
//                    mapCanvas.drawImage(cellX, cellY, image);
//                    System.out.println(cellX + " " + cellY + task.getName());
//                }
//
//                boolean done = playerModel.getTempPlayerData().hasCompleted(task);
//
//                // drawCell(mapCanvas, row, col, cellSize,
//                //     done ? Color.GREEN : Color.GRAY);
//            }
//        }
//
//        drawGrid(mapCanvas, 25);
//    }

    private void drawCell(MapCanvas canvas, int row, int col, int size, java.awt.Color color) {
        int startX = col * size;
        int startY = row * size;

        for (int x = startX; x < startX + size; x++) {
            for (int y = startY; y < startY + size; y++) {
                canvas.setPixelColor(x, y, color);
            }
        }
    }

    private void drawGrid(MapCanvas canvas, int size) {

        for (int i = 0; i <= 5; i++) {
            int pos = i * size;

            for (int p = 0; p < 128; p++) {
                if (pos < 128) {
                    canvas.setPixelColor(pos, p, Color.BLACK); // vertical
                    canvas.setPixelColor(p, pos, Color.BLACK); // horizontal
                }
            }
        }
    }
}
