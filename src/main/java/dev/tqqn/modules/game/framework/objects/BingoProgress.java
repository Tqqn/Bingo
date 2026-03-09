package dev.tqqn.modules.game.framework.objects;

import dev.tqqn.modules.database.framework.objects.PlayerModel;
import lombok.Getter;

import java.util.*;

@Getter
public final class BingoProgress {

    private final Map<BingoTask, BingoPlacement> tasks = new HashMap<>();

    public BingoProgress(List<BingoTask> availableTasks) {
        final List<BingoTask> shuffled = new ArrayList<>(availableTasks);
        Collections.shuffle(shuffled);

        List<BingoTask> shuffledCopy = new ArrayList<>();

        if (shuffled.size() > 25) {
            shuffledCopy.addAll(shuffled.subList(0, 26));
        } else {
            shuffledCopy.addAll(shuffled);
        }

        for (BingoTask task : shuffledCopy) {
            tasks.put(task, providePlacement());
        }

        System.out.println("Tasks size: " + tasks.size());
    }

    public boolean hasBingo(PlayerModel playerModel) {
        int[] rowCount = new int[6];
        int[] colCount = new int[6];
        int mainDiag = 0;
        int antiDiag = 0;

        boolean[][] seen = new boolean[6][6]; // seen Placements

        for (var entry : tasks.entrySet()) {
            BingoPlacement p = entry.getValue();
            if (p == null) continue;

            int col = p.getColumn();
            int row = p.getRow();
            if (col < 1 || col > 5 || row < 1 || row > 5) continue;

            if (!playerModel.getTempPlayerData().hasCompleted(entry.getKey())) continue;

            if (seen[row][col]) continue;
            seen[row][col] = true;

            if (++rowCount[row] == 5) return true;
            if (++colCount[col] == 5) return true;

            if (row == col && ++mainDiag == 5) return true;
            if (row + col == 6 && ++antiDiag == 5) return true;
        }

        return false;
    }

    private BingoPlacement providePlacement() {
        boolean[][] usedPlacement = new boolean[6][6];

        for (BingoPlacement placement : tasks.values()) {
            if (placement == null) continue;

            int column = placement.getColumn();
            int row = placement.getRow();

            if (column >= 0 && column <= 5 && row >= 0 && row <= 5) {
                usedPlacement[row][column] = true;
            }
        }

        for (int column = 0; column <= 5; column++) {
            for (int row = 0; row <= 5; row++) {
                System.out.println(column + " " + row);
                if (!usedPlacement[row][column]) return new BingoPlacement(column, row);
            }
        }

        return null;
    }
}
