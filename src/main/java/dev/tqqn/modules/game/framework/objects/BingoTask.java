package dev.tqqn.modules.game.framework.objects;

import dev.tqqn.modules.game.framework.team.GameTeam;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public final class BingoTask {

    @Getter private final String name;
    @Getter private final ItemStack goal;
    @Getter private final String png;

    private final List<GameTeam> completed = new ArrayList<>();

    public void complete(GameTeam team) {
        completed.add(team);
    }

    public void clear() {
        completed.clear();
    }

    public List<GameTeam> getCompleted() {
        return Collections.unmodifiableList(completed);
    }

    public boolean hasCompleted(GameTeam team) {
        return completed.contains(team);
    }
}
