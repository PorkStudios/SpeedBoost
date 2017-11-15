package net.daporkchop.speedboost.config.impl;

import com.google.gson.JsonObject;
import net.daporkchop.speedboost.config.IConfigTranslator;

public class ItemDespawn implements IConfigTranslator {
    public static final ItemDespawn INSTANCE = new ItemDespawn();

    private ItemDespawn() {

    }

    public boolean state = false;
    public int lifespan = 6000;

    public void encode(JsonObject json) {
        json.addProperty("state", state);
        json.addProperty("lifespan", lifespan);
    }

    public void decode(String fieldName, JsonObject json) {
        state = getBoolean(json, "state", state);
        lifespan = getInt(json, "lifespan", lifespan);
    }

    public String name() {
        return "itemDespawn";
    }

    public boolean getState()   {
        return state;
    }

    public String getPackageName() {
        return "itemdespawn";
    }
}