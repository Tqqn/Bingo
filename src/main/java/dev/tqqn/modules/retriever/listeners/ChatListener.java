package dev.tqqn.modules.retriever.listeners;

import dev.tqqn.BingoMain;
import dev.tqqn.modules.retriever.RetrieverModule;
import dev.tqqn.modules.retriever.objects.AbstractRetriever;
import dev.tqqn.utils.ChatUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public final class ChatListener implements Listener {

    private final RetrieverModule module;

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(AsyncChatEvent event) {
        final Player player = event.getPlayer();

        if (!module.doesRetrieverExist(player))
            return;

        AbstractRetriever<?> retriever = module.getRetriever(player);
        if (retriever == null) return;

        event.setCancelled(true);

        String rawMessage = ChatUtils.getRawText(event.message());

        if (rawMessage.equalsIgnoreCase("cancel")) {
            retriever.cancel();
            module.removeRetriever(player);
            event.setCancelled(true);
            return;
        }

        Bukkit.getScheduler().runTask(BingoMain.getInstance(), () -> {
            module.removeRetriever(player);

            if (!retriever.handle(rawMessage)) {
                module.addRetrieverInstance(player, retriever);
                return;
            }

            retriever.finish();
        });
    }
}
