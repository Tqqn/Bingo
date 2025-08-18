package dev.tqqn.modules.retriever.objects.types;

import dev.tqqn.modules.retriever.objects.AbstractRetriever;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public final class StringRetriever extends AbstractRetriever<String> {

    public StringRetriever(Player player, String message, Consumer<String> callBack) {
        super(player, message, callBack);
    }

    @Override
    public boolean handle(String string) {
        callBack.accept(string);
        return true;
    }
}