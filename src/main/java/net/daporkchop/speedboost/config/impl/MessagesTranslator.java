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
import net.daporkchop.speedboost.config.IConfigTranslator;

public class MessagesTranslator implements IConfigTranslator {
    public static final MessagesTranslator INSTANCE = new MessagesTranslator();
    public boolean state = false;
    public boolean logCommands = true;
    public String whitelistMessage = "You are not whitelisted on this server!";
    public String unknownCommandMessage = "Unknown command. Type \"/help\" for help.";
    public String serverFullMessage = "The server is full!";
    public String outdatedClientMessage = "Outdated client! Please use {0}";
    public String outdatedServerMessage = "Outdated server! I\'m still on {0}";

    private MessagesTranslator() {

    }

    public void encode(JsonObject json) {
        json.addProperty("state", state);
        json.addProperty("logCommands", logCommands);
        json.addProperty("whitelistMessage", whitelistMessage);
        json.addProperty("unknownCommandMessage", unknownCommandMessage);
        json.addProperty("serverFullMessage", serverFullMessage);
        json.addProperty("outdatedClientMessage", outdatedClientMessage);
        json.addProperty("outdatedServerMessage", outdatedServerMessage);
    }

    public void decode(String fieldName, JsonObject json) {
        state = getBoolean(json, "state", state);
        logCommands = getBoolean(json, "logCommands", logCommands);
        whitelistMessage = getString(json, "whitelistMessage", whitelistMessage).replace('&', '\u00A7');
        whitelistMessage = getString(json, "unknownCommandMessage", unknownCommandMessage).replace('&', '\u00A7');
        whitelistMessage = getString(json, "serverFullMessage", serverFullMessage).replace('&', '\u00A7');
        whitelistMessage = getString(json, "outdatedClientMessage", outdatedClientMessage).replace('&', '\u00A7');
        whitelistMessage = getString(json, "outdatedServerMessage", outdatedServerMessage).replace('&', '\u00A7');
    }

    public String name() {
        return "messages";
    }

    public boolean getState() {
        return state;
    }

    public String getPackageName() {
        return "messages";
    }
}