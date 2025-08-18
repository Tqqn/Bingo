package dev.tqqn.modules.retriever.objects.types;

import dev.tqqn.modules.retriever.objects.AbstractRetriever;
import dev.tqqn.utils.Notify;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public final class DoubleRetriever extends AbstractRetriever<Double> {

    public DoubleRetriever(Player player, String message, Consumer<Double> callBack) {
        super(player, message, callBack);
    }

    @Override
    public boolean handle(String s) {
        try {
            callBack.accept(Double.parseDouble(s));
            return true;
        } catch (NumberFormatException e) {
            Notify.ERROR.chat(player, "Dit is geen geldig getal");
            return false;
        }
    }
}
