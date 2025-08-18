package dev.tqqn.modules.retriever;

import dev.tqqn.TemplateMain;
import dev.tqqn.modules.AbstractModule;
import dev.tqqn.modules.retriever.listeners.ChatListener;
import dev.tqqn.modules.retriever.listeners.QuitListener;
import dev.tqqn.modules.retriever.objects.AbstractRetriever;
import dev.tqqn.modules.retriever.objects.types.DoubleRetriever;
import dev.tqqn.modules.retriever.objects.types.IntRetriever;
import dev.tqqn.modules.retriever.objects.types.StringRetriever;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class RetrieverModule extends AbstractModule {

    private static final Map<Player, AbstractRetriever<?>> CHAT_READERS = new HashMap<>();

    public RetrieverModule(TemplateMain templateMain) {
        super(templateMain, "Retrievers");
    }

    @Override
    public void onEnable() {
        register(new ChatListener(this));
        register(new QuitListener(this));
    }

    public static void retrieveString(Player player, String message, Consumer<String> callBack) {
        addRetriever(player, new StringRetriever(player, message, callBack));
    }

    public static void retrieveInt(Player player, String message, Consumer<Integer> callBack) {
        addRetriever(player, new IntRetriever(player, message, callBack));
    }

    public static void retrieveDouble(Player player, String message, Consumer<Double> callBack) {
        addRetriever(player, new DoubleRetriever(player, message, callBack));
    }

    private static void addRetriever(Player player, AbstractRetriever<?> retriever) {
        CHAT_READERS.put(player, retriever);
    }

    public void addRetrieverInstance(Player player, AbstractRetriever<?> retriever) {
        CHAT_READERS.put(player, retriever);
    }

    public void removeRetriever(Player player) {
        CHAT_READERS.remove(player);
    }

    public @Nullable AbstractRetriever<?> getRetriever(Player player) {
        return CHAT_READERS.get(player);
    }

    public boolean doesRetrieverExist(Player player) {
        return CHAT_READERS.containsKey(player);
    }
}
