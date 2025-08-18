package dev.tqqn.modules.retriever.objects;

import dev.tqqn.utils.Notify;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public abstract class AbstractRetriever<T> {

    protected final Player player;
    protected final Consumer<T> callBack;

    @Setter
    private boolean inform = false;

    protected AbstractRetriever(Player player, String message, Consumer<T> callBack) {
        this.player = player;
        player.closeInventory();
        Notify.INFO.chat(player, message);
        Notify.INFO.chat(player, "Fill in 'cancel' to cancel the action");
        this.callBack = callBack;
    }

    public abstract boolean handle(String string);

    public void cancel() {
        Notify.INFO.chat(player, "You've cancelled the action");
    }

    public void finish() {
        if (!inform) return;
        Notify.SUCCESS.chat(player, "You've successfully finished the action");
    }
}
