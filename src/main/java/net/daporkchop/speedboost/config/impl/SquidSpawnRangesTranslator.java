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

package net.daporkchop.speedboost.config.impl;

import com.google.gson.JsonObject;
import net.daporkchop.speedboost.config.Config;
import net.daporkchop.speedboost.config.IConfigTranslator;

public class SquidSpawnRangesTranslator implements IConfigTranslator {
    public static final SquidSpawnRangesTranslator INSTANCE = new SquidSpawnRangesTranslator();
    public boolean state = false;
    public double squidMinSpawnHeight;
    public double squidMaxSpawnHeight;

    private SquidSpawnRangesTranslator() {

    }

    public void encode(JsonObject json) {
        json.addProperty("state", state);
        json.addProperty("squidMinSpawnHeight", squidMinSpawnHeight);
        json.addProperty("squidMaxSpawnHeight", squidMaxSpawnHeight);
    }

    public void decode(String fieldName, JsonObject json) {
        state = getBoolean(json, "state", state);
        squidMinSpawnHeight = getDouble(json, "squidMinSpawnHeight", squidMinSpawnHeight);
        squidMaxSpawnHeight = getDouble(json, "squidMaxSpawnHeight", squidMaxSpawnHeight);
        if (state) {
            Config.LOGGER.info("Squids will spawn between Y: " + squidMinSpawnHeight + " and Y: " + squidMaxSpawnHeight);
        }
    }

    public String name() {
        return "squidSpawnRanges";
    }

    public boolean getState() {
        return state;
    }

    public String getPackageName() {
        return "squidspawnranges";
    }
}