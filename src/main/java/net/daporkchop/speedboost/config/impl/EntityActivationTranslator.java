package net.daporkchop.speedboost.config.impl;

import com.google.gson.JsonObject;
import net.daporkchop.speedboost.config.IConfigTranslator;

public class EntityActivationTranslator implements IConfigTranslator {
    public static final EntityActivationTranslator INSTANCE = new EntityActivationTranslator();

    public int animalActivationRange = 32;
    public int monsterActivationRange = 32;
    public int miscActivationRange = 16;
    public boolean state = false;

    private EntityActivationTranslator() {

    }

    public String getPackageName() {
        return "entityactivationrange";
    }

    public void encode(JsonObject json) {
        json.addProperty("state", state);
        json.addProperty("animalActivationRange", animalActivationRange);
        json.addProperty("monsterActivationRange", monsterActivationRange);
        json.addProperty("miscActivationRange", miscActivationRange);
    }

    public void decode(String fieldName, JsonObject json) {
        state = getBoolean(json, "state", state);
        animalActivationRange = getInt(json, "animalActivationRange", animalActivationRange);
        monsterActivationRange = getInt(json, "monsterActivationRange", monsterActivationRange);
        miscActivationRange = getInt(json, "miscActivationRange", miscActivationRange);
    }

    public String name() {
        return "entityActivationRange";
    }

    public boolean getState() {
        return state;
    }
}