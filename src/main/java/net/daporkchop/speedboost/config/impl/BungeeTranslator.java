package net.daporkchop.speedboost.config.impl;

import com.google.gson.JsonObject;
import net.daporkchop.speedboost.config.IConfigTranslator;

public class BungeeTranslator implements IConfigTranslator {
    public static final BungeeTranslator INSTANCE = new BungeeTranslator();

    private BungeeTranslator() {

    }

    public boolean state = false;

    public void encode(JsonObject json) {
        json.addProperty("state", state);
    }

    public void decode(String fieldName, JsonObject json) {
        state = getBoolean(json, "state", state);
    }

    public String name() {
        return "bungeecord";
    }

    public boolean getState()   {
        return state;
    }

    public String getPackageName() {
        return "bungee";
    }
}