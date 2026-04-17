package dev.tqqn.modules.database.listeners;

import dev.tqqn.modules.database.DatabaseModule;
import dev.tqqn.modules.database.framework.events.PlayerModelJoinEvent;
import dev.tqqn.modules.database.framework.events.PlayerModelPreJoinEvent;
import dev.tqqn.modules.database.framework.objects.PlayerModel;
import dev.tqqn.utils.ChatUtils;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

@RequiredArgsConstructor
public final class PlayerLoadListener implements Listener {

    private static final ConcurrentHashMap<UUID, PlayerModel> joiningPlayers = new ConcurrentHashMap<>();

    private final DatabaseModule databaseModule;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (isPlayerLoaded(event.getUniqueId())) return;
        PlayerModel playerModel = databaseModule.getMongoDriver().read(PlayerModel.class, event.getUniqueId().toString());
        if (playerModel == null) {
            databaseModule.getLogger().log(Level.INFO, "Player '" + event.getName() + "' has no saved data. Creating a new one...");
            playerModel = new PlayerModel(event.getUniqueId(), event.getName());
            databaseModule.getMongoDriver().save(playerModel); // No async? NO! Because its already async ;)!
        }
        joiningPlayers.put(playerModel.getKey(), playerModel);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        PlayerModel playerModel = joiningPlayers.get(player.getUniqueId());

        final PlayerModelPreJoinEvent playerModelPreJoinEvent = new PlayerModelPreJoinEvent(playerModel);
        Bukkit.getPluginManager().callEvent(playerModelPreJoinEvent);
        if (playerModelPreJoinEvent.isCancelled()) {
            playerModel.save();
            joiningPlayers.remove(player.getUniqueId());
            if (playerModelPreJoinEvent.getKickMessage() == null) {
                player.kick(ChatUtils.format("<red>Your login has been disallowed."));
                return;
            }
            player.kick(playerModelPreJoinEvent.getKickMessage());
            return;
        }

        if (playerModel == null) {
            event.kickMessage(ChatUtils.format("<red>Your data could not be loaded. Please try again later. If this issue persist please contact a staff-member."));
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            joiningPlayers.remove(player.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(Component.empty());
        final Player player = event.getPlayer();
        PlayerModel playerModel = joiningPlayers.remove(player.getUniqueId());
        playerModel.initialize();

        PlayerModel.cache(playerModel);

        final PlayerModelJoinEvent playerModelJoinEvent = new PlayerModelJoinEvent(playerModel);
        Bukkit.getPluginManager().callEvent(playerModelJoinEvent);

        if (!playerModel.getName().equalsIgnoreCase(player.getName())) {
            playerModel.setName(player.getName());
        }
    }

    private boolean isPlayerLoaded(UUID uuid) {
        return PlayerModel.from(uuid) != null;
    }
}