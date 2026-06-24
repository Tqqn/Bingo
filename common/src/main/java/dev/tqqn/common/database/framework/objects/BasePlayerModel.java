package dev.tqqn.common.database.framework.objects;

import dev.tqqn.common.database.framework.mongo.MongoObject;
import dev.tqqn.common.scoreboard.framework.SingleScoreboard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.UUID;

@Getter
public abstract class BasePlayerModel extends MongoObject<UUID> implements SaveAble, CleanupAble {

    @Setter
    private String name;
    private transient WeakReference<Player> playerWeakReference;
    private transient SingleScoreboard playerScoreboard;

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

    public void cleanUpScoreboard() {
        playerScoreboard = null;
    }

    public void updateScoreboard(SingleScoreboard scoreboard) {
        this.playerScoreboard = scoreboard;
    }

    public abstract void initialize();


}
