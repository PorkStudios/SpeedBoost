/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 DaPorkchop_ and contributors
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.speedboost.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface IConfigTranslator {
    /**
     * Writes the configuration to the JsonObject
     *
     * @param json the object to write to
     */
    void encode(JsonObject json);

    /**
     * Reads the configuration from the JsonObject
     * @param fieldName the name of the field the object was contained in (generally useless)
     * @param json the object to read from
     */
    void decode(String fieldName, JsonObject json);

    /**
     * The name of the configuration section
     * @return
     */
    String name();

    /**
     * Reads an int from the given JsonObject
     * @param object the object to read from
     * @param name the name of the field
     * @param def the default value
     * @return the value of the field [name] contained in [object] or [def] if not found
     */
    default int getInt(JsonObject object, String name, int def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                return element.getAsJsonPrimitive().getAsNumber().intValue();
            }
        }

        return def;
    }

    /**
     * Reads a short from the given JsonObject
     * @param object the object to read from
     * @param name the name of the field
     * @param def the default value
     * @return the value of the field [name] contained in [object] or [def] if not found
     */
    default short getShort(JsonObject object, String name, short def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                return element.getAsJsonPrimitive().getAsNumber().shortValue();
            }
        }

        return def;
    }

    /**
     * Reads a byte from the given JsonObject
     * @param object the object to read from
     * @param name the name of the field
     * @param def the default value
     * @return the value of the field [name] contained in [object] or [def] if not found
     */
    default byte getByte(JsonObject object, String name, byte def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                return element.getAsJsonPrimitive().getAsNumber().byteValue();
            }
        }

        return def;
    }

    /**
     * Reads a long from the given JsonObject
     * @param object the object to read from
     * @param name the name of the field
     * @param def the default value
     * @return the value of the field [name] contained in [object] or [def] if not found
     */
    default long getLong(JsonObject object, String name, long def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                return element.getAsJsonPrimitive().getAsNumber().longValue();
            }
        }

        return def;
    }

    /**
     * Reads a float from the given JsonObject
     * @param object the object to read from
     * @param name the name of the field
     * @param def the default value
     * @return the value of the field [name] contained in [object] or [def] if not found
     */
    default float getFloat(JsonObject object, String name, float def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                return element.getAsJsonPrimitive().getAsNumber().floatValue();
            }
        }

        return def;
    }

    /**
     * Reads a double from the given JsonObject
     * @param object the object to read from
     * @param name the name of the field
     * @param def the default value
     * @return the value of the field [name] contained in [object] or [def] if not found
     */
    default double getDouble(JsonObject object, String name, double def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                return element.getAsJsonPrimitive().getAsNumber().doubleValue();
            }
        }

        return def;
    }

    /**
     * Reads a boolean from the given JsonObject
     * @param object the object to read from
     * @param name the name of the field
     * @param def the default value
     * @return the value of the field [name] contained in [object] or [def] if not found
     */
    default boolean getBoolean(JsonObject object, String name, boolean def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
                return element.getAsJsonPrimitive().getAsBoolean();
            }
        }

        return def;
    }

    /**
     * Reads a String from the given JsonObject
     * @param object the object to read from
     * @param name the name of the field
     * @param def the default value
     * @return the value of the field [name] contained in [object] or [def] if not found
     */
    default String getString(JsonObject object, String name, String def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                return element.getAsJsonPrimitive().getAsString();
            }
        }

        return def;
    }

    /**
     * Reads an array from the given JsonObject
     * @param object the object to read from
     * @param name the name of the field
     * @param def the default value
     * @return the value of the field [name] contained in [object] or [def] if not found
     */
    default JsonArray getArray(JsonObject object, String name, JsonArray def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonArray()) {
                return element.getAsJsonArray();
            }
        }

        return def;
    }

    /**
     * The state of the config option
     * @return whether or not the related mixins will load
     */
    boolean getState();

    /**
     * The subpackage that related mixins are stored in
     * (i.e. net.daporkchop.speedboost.mixin.[getPackageName()])
     *
     * @return the name of the subpackage
     */
    String getPackageName();

    /**
     * Checks if a mixin with a given name should load
     *
     * @param name the canonical name of the mixin class to check
     * @return LOAD if it should, NO_LOAD if not, UNKNOWN if this translator doesn't know
     */
    default EnumLoadType shouldLoad(String name) {
        if (name.startsWith("net.daporkchop.speedboost.mixin." + getPackageName())) {
            return getState() ? EnumLoadType.LOAD : EnumLoadType.NO_LOAD;
        }

        return EnumLoadType.UNKNOWN;
    }
}
