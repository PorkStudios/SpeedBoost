package net.daporkchop.speedboost.config.impl;

import com.google.gson.JsonObject;
import net.daporkchop.speedboost.config.IConfigTranslator;

public class NulltileTranslator implements IConfigTranslator {
    public static final NulltileTranslator INSTANCE = new NulltileTranslator();

    private NulltileTranslator() {

    }

    public boolean state = false;

    public void encode(JsonObject json) {
        json.addProperty("state", state);
    }

    public void decode(String fieldName, JsonObject json) {
        state = getBoolean(json, "state", state);
    }

    public String name() {
        return "antinulltiles";
    }

    public boolean getState()   {
        return state;
    }

    public String getPackageName() {
        return "antinulltiles";
    }
}