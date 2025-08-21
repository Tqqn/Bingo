package dev.tqqn.modules.bingo.framework.objects;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public final class BingoGameProgress {

    private final BingoTask[][] tasks = new BingoTask[5][5];

    public BingoGameProgress(List<BingoTask> availableTasks) {
        final List<BingoTask> shuffled = new ArrayList<>(availableTasks);
        Collections.shuffle(shuffled);

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                tasks[row][col] = shuffled.get(row * 5 + col);
            }
        }
    }
}
