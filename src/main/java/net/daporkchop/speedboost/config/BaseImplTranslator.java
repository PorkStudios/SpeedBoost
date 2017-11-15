package net.daporkchop.speedboost.config;

import com.google.gson.JsonObject;

public class BaseImplTranslator implements IConfigTranslator {
    public static final BaseImplTranslator INSTANCE = new BaseImplTranslator();

    private BaseImplTranslator() {

    }

    public boolean state = false;

    public void encode(JsonObject json) {
        json.addProperty("state", state);
    }

    public void decode(String fieldName, JsonObject json) {
        state = getBoolean(json, "state", state);
    }

    public String name() {
        return "delet_this";
    }

    public boolean getState()   {
        return state;
    }

    public String getPackageName() {
        return "delet_this";
    }
}