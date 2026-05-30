package dev.tqqn.game.modules.database.framework.objects;

import dev.tqqn.game.BingoMain;
import dev.tqqn.game.modules.database.DatabaseModule;
import dev.tqqn.game.modules.database.framework.mongo.MongoItem;
import dev.tqqn.game.modules.database.framework.mongo.MongoObject;
import dev.tqqn.game.modules.game.framework.data.TempPlayerData;
import dev.tqqn.game.modules.game.framework.objects.stats.PlayerStats;
import dev.tqqn.game.utils.MojangAPI;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@MongoItem("players")
public final class PlayerModel extends MongoObject<UUID> {

    private static final Map<UUID, PlayerModel> CACHE = new ConcurrentHashMap<>();

    @Setter
    private String name;
    private PlayerStats playerStats;

    private transient WeakReference<Player> playerWeakReference;

    private transient TempPlayerData tempPlayerData;

    public PlayerModel(UUID key, String name) {
        super(key);
        this.name = name;
    }

    public void initialize() {
        if (tempPlayerData == null) this.tempPlayerData = new TempPlayerData(getKey()); // Yes a null check, sometimes its weirdly null :/
        if (playerStats == null) this.playerStats = new PlayerStats();
    }

    public Optional<Player> getPlayer() {
        if (playerWeakReference == null || playerWeakReference.get() == null) {
            final Player player = Bukkit.getPlayer(getKey());
            if (player == null) return Optional.empty();

            playerWeakReference = new WeakReference<>(player);
            return Optional.ofNullable(playerWeakReference.get());
        }
        return Optional.ofNullable(playerWeakReference.get());
    }

    public static PlayerModel from(Player player) {
        return from(player.getUniqueId());
    }

    public static PlayerModel from(UUID uuid) {
        return CACHE.get(uuid);
    }

    public static void cache(PlayerModel playerModel) {
        CACHE.put(playerModel.getKey(), playerModel);
    }

    public static PlayerModel fromOffline(String playerName) {
        UUID uuid = MojangAPI.getUUIDFromName(playerName);
        if (uuid == null) return null;

        return BingoMain.getInstance().getModuleManager().getModule(DatabaseModule.class).getMongoDriver().read(PlayerModel.class, uuid);
    }

    public void save() {
        BingoMain.getInstance().getModuleManager().getModule(DatabaseModule.class).getMongoDriver().saveAsync(this);
    }

    public void cleanUp() {
        save();
        CACHE.remove(getKey());
    }
}
