package net.daporkchop.speedboost.config;

import com.google.gson.JsonObject;

/**
 * A config translator that does nothing with the given data
 */
public class NullConfigTranslator implements IConfigTranslator {
    public static final IConfigTranslator INSTANCE = new NullConfigTranslator();

    private NullConfigTranslator() {

    }

    public void encode(JsonObject json) {

    }

    public void decode(String fieldName, JsonObject json) {
        System.out.println("[Warning] Config element with name " + fieldName + "is being ignored, discarding " + json.entrySet().size() + " values!");
    }

    public String name() {
        return null;
    }

    public boolean getState() {
        return false;
    }

    public String getPackageName() {
        return null;
    }
}
