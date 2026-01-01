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
            shuffledCopy.addAll(shuffled.subList(0, 25));
        }

        for (BingoTask task : shuffledCopy) {
            tasks.put(task, providePlacement());
        }
    }

    public boolean hasBingo(PlayerModel playerModel) {

        return true;
    }

    private BingoPlacement providePlacement() {
        boolean[][] usedPlacement = new boolean[5][5];

        for (BingoPlacement placement : tasks.values()) {
            if (placement == null) continue;

            int column = placement.getColumn();
            int row = placement.getRow();

            if (column >= 1 && column <= 5 && row >= 1 && row <= 5) {
                usedPlacement[column][row] = true;
            }
        }

        for (int column = 1; column <= 5; column++) {
            for (int row = 1; row <= 5; row++) {
                if (!usedPlacement[column][row]) return new BingoPlacement(column, row);
            }
        }

        return null;
    }
}
