package dev.tqqn.common.database.framework.objects;

import dev.tqqn.common.database.framework.mongo.MongoObject;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.UUID;

@Getter
public abstract class BasePlayerModel extends MongoObject<UUID> {

    @Setter
    private String name;
    private transient WeakReference<Player> playerWeakReference;

    public BasePlayerModel(UUID key, String name) {
        super(key);
        this.name = name;
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
}
