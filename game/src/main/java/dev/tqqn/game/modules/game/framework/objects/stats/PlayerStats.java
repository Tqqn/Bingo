package dev.tqqn.game.modules.game.framework.objects.stats;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 22/05/2026
 */
public class PlayerStats {

    private final Map<StatType, Integer> stats = new EnumMap<>(StatType.class);

    public PlayerStats() {
        for (StatType statType : StatType.values()) {
            stats.put(statType, 0);
        }
    }

    public int getStat(StatType statType) {
        return stats.getOrDefault(statType, 0);
    }

    public enum StatType {
        WINS,
        LOSSES,
        DEATHS,
        WLR;
    }

}
