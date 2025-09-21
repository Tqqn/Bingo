package dev.tqqn.modules.scoreboard.framework.objects;

import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public final class UpdateAbleLine {

    private final List<Component> stages = new ArrayList<>();
    private int currentStage = 0;

    public Component getNextStage() {
        currentStage++;
        if (stages.size() > currentStage) currentStage = 0;
        return stages.get(currentStage);
    }

    public void addStage(Component component) {
        stages.add(component);
    }
}
