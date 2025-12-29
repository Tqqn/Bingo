package dev.tqqn.modules.game.framework.objects;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public final class PlayerBingoProgress {

    private final List<BingoTask> tasks = new ArrayList<>();

    public PlayerBingoProgress(List<BingoTask> availableTasks) {
        final List<BingoTask> shuffled = new ArrayList<>(availableTasks);
        Collections.shuffle(shuffled);
        for (BingoTask task : shuffled) {

        }
    }

    public boolean hasBingo() {
        boolean isComplete = true;
        for (BingoTask[] task : tasks) {
        }
    }
}
