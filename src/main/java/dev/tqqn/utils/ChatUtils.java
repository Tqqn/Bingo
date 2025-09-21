package dev.tqqn.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public final class ChatUtils {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder().build();
    private static final PlainTextComponentSerializer PLAIN_TEXT_COMPONENT_SERIALIZER = PlainTextComponentSerializer.plainText();

    public static Component format(String message) {
        return MINI_MESSAGE.deserialize(message).decoration(TextDecoration.ITALIC, false);
    }

    @Deprecated
    public static String formatLegacy(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Deprecated
    public static String[] formatLegacy(String[] strings) {
        List<String> msgArray = new ArrayList<>();
        for (String msg : strings) {
            msgArray.add(formatLegacy(msg));
        }
        return msgArray.toArray(new String[0]);
    }

    public static String getRawText(Component component) {
        return PLAIN_TEXT_COMPONENT_SERIALIZER.serialize(component);
    }

    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        return String.format("%02d:%02d", m, s);
    }

    public static Component centerMessage(String message) {
        Component component = format(message);

        int messagePxSize = 0;

        TextComponent textComponent = (TextComponent) component;
        String componentString = PlainTextComponentSerializer.plainText().serialize(textComponent);

        for (char c : componentString.toCharArray()) {
            DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
            messagePxSize += dFI.getLength();
            messagePxSize++;
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = 154 - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        return format(sb.toString()).append(component);
    }


    public static Component empty() {
        return Component.empty();
    }
}
