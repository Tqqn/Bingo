package dev.tqqn.game.modules.database.player;

import dev.tqqn.common.database.framework.mongo.MongoItem;
import dev.tqqn.common.database.framework.objects.BasePlayerModel;
import dev.tqqn.common.utils.MojangAPI;
import dev.tqqn.game.BingoMain;
import dev.tqqn.game.modules.database.GameDatabaseModule;
import dev.tqqn.game.modules.game.framework.data.TempPlayerData;
import dev.tqqn.game.modules.game.framework.objects.stats.PlayerStats;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 11/06/2026
 */

@MongoItem("players")
@Getter
public final class BingoPlayerModel extends BasePlayerModel {

    private static final Map<UUID, BingoPlayerModel> CACHE = new ConcurrentHashMap<>();

    private PlayerStats playerStats;
    private transient TempPlayerData tempPlayerData;

    public BingoPlayerModel(UUID key, String name) {
        super(key, name);
    }

    @Override
    public void initialize() {
        if (tempPlayerData == null) this.tempPlayerData = new TempPlayerData(getKey()); // Yes a null check, sometimes its weirdly null :/
        if (playerStats == null) this.playerStats = new PlayerStats();
    }

    public static BingoPlayerModel from(Player player) {
        return from(player.getUniqueId());
    }

    public static BingoPlayerModel from(UUID uuid) {
        return CACHE.get(uuid);
    }

    public static void cache(BingoPlayerModel bingoPlayerModel) {
        CACHE.put(bingoPlayerModel.getKey(), bingoPlayerModel);
    }

    public static BingoPlayerModel fromOffline(String playerName) {
        UUID uuid = MojangAPI.getUUIDFromName(playerName);
        if (uuid == null) return null;
        return BingoMain.getInstance().getModuleManager()
                .getModule(GameDatabaseModule.class)
                .getMongoDriver()
                .read(BingoPlayerModel.class, uuid);
    }

    @Override
    public void save() {
        BingoMain.getInstance().getModuleManager().getModule(GameDatabaseModule.class).getMongoDriver().saveAsync(this);
    }

    @Override
    public void cleanUp() {
        save();
        CACHE.remove(getKey());
    }
}
