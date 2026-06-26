package dev.tqqn.lobby.modules.database.objects;

import dev.tqqn.common.database.framework.mongo.MongoItem;
import dev.tqqn.common.database.framework.objects.BasePlayerModel;
import dev.tqqn.lobby.LobbyPlugin;
import dev.tqqn.lobby.modules.database.LobbyDatabaseModule;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tqqn (tqqn.dev)
 * Created on 24/06/2026
 */
@MongoItem("players")
public class LobbyPlayerModel extends BasePlayerModel {

    private static final Map<UUID, LobbyPlayerModel> CACHE = new ConcurrentHashMap<>();

    public LobbyPlayerModel(UUID key, String name) {
        super(key, name);
    }

    public static LobbyPlayerModel from(Player player) {
        return from(player.getUniqueId());
    }

    public static LobbyPlayerModel from(UUID uuid) {
        return CACHE.get(uuid);
    }

    public static void cache(LobbyPlayerModel model) {
        CACHE.put(model.getKey(), model);
    }

    @Override
    public void initialize() {
        //TODO
    }

    @Override
    public void save() {
        LobbyPlugin.getInstance().getModuleManager().getModule(LobbyDatabaseModule.class).getMongoDriver().upsertFieldsAsync(this);
    }

    @Override
    public void cleanUp() {
        save();
        CACHE.remove(getKey());
    }

    public void setTesting(String test123) {
        this.test123 = test123;
    }
}
