/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2018-2019 DaPorkchop_ and contributors
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
import net.daporkchop.speedboost.config.IConfigTranslator;

public class EntityTrackingTranslator implements IConfigTranslator {
    public static final EntityTrackingTranslator INSTANCE = new EntityTrackingTranslator();
    public boolean state;
    public int playerTrackingRange = 48;
    public int animalTrackingRange = 48;
    public int monsterTrackingRange = 48;
    public int miscTrackingRange = 32;
    public int otherTrackingRange = 64;

    private EntityTrackingTranslator() {

    }

    public void encode(JsonObject json) {
        json.addProperty("state", this.state);
        json.addProperty("playerTrackingRange", this.playerTrackingRange);
        json.addProperty("animalTrackingRange", this.animalTrackingRange);
        json.addProperty("monsterTrackingRange", this.monsterTrackingRange);
        json.addProperty("miscTrackingRange", this.miscTrackingRange);
        json.addProperty("otherTrackingRange", this.otherTrackingRange);
    }

    public void decode(String fieldName, JsonObject json) {
        this.state = this.getBoolean(json, "state", this.state);
        this.playerTrackingRange = this.getInt(json, "playerTrackingRange", this.playerTrackingRange);
        this.animalTrackingRange = this.getInt(json, "animalTrackingRange", this.animalTrackingRange);
        this.monsterTrackingRange = this.getInt(json, "monsterTrackingRange", this.monsterTrackingRange);
        this.miscTrackingRange = this.getInt(json, "miscTrackingRange", this.miscTrackingRange);
        this.otherTrackingRange = this.getInt(json, "otherTrackingRange", this.otherTrackingRange);
    }

    public String name() {
        return "entityTrackingRange";
    }

    public boolean getState() {
        return this.state;
    }

    public String getPackageName() {
        return "entitytrackingrange";
    }
}