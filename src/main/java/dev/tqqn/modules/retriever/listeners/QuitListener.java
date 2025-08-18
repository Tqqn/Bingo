package dev.tqqn.modules.retriever.listeners;

import dev.tqqn.modules.retriever.RetrieverModule;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public final class QuitListener implements Listener {

    private final RetrieverModule retrieverModule;

    @EventHandler
    public void on(PlayerQuitEvent event) {
        retrieverModule.removeRetriever(event.getPlayer());
    }

}
