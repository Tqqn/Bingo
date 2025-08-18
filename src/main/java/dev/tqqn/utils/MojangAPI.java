package dev.tqqn.utils;

import com.google.gson.JsonParser;
import dev.tqqn.TemplateMain;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

@UtilityClass
public final class MojangAPI {

    private static final String ADD_UUID_HYPHENS_REGEX = "([a-f0-9]{8})([a-f0-9]{4})(4[a-f0-9]{3})([89aAbB][a-f0-9]{3})([a-f0-9]{12})";

    public static UUID getUUIDFromName(String name) {

        CompletableFuture<UUID> future = CompletableFuture.supplyAsync(() -> {
            try {
                URL mojangAPI = URI.create("https://api.mojang.com/users/profiles/minecraft/" + name).toURL();
                InputStreamReader apiReader = new InputStreamReader(mojangAPI.openStream());
                String uuid = JsonParser.parseReader(apiReader).getAsJsonObject().get("id").getAsString();

                return UUID.fromString(uuid.replaceAll(ADD_UUID_HYPHENS_REGEX, "$1-$2-$3-$4-$5"));
            } catch (IOException e) {
                TemplateMain.getInstance().getLogger().log(Level.SEVERE, "Could not find UUID.");
                return null;
            }
        });

        return future.join();

    }
}

