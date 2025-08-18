package dev.tqqn.modules.retriever.objects.types;

import dev.tqqn.modules.retriever.objects.AbstractRetriever;
import dev.tqqn.utils.Notify;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class IntRetriever extends AbstractRetriever<Integer> {

    public IntRetriever(Player player, String message, Consumer<Integer> callBack) {
        super(player, message, callBack);
    }

    @Override
    public boolean handle(String s) {
        try {
            callBack.accept(Integer.parseInt(s));
            return true;
        } catch (NumberFormatException e) {
            Notify.ERROR.chat(player, "This is not a valid number.");
            return false;
        }
    }
}
