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

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.daporkchop.speedboost.config.impl.*;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Based on the config system in pepsimod
 */
public class Config implements IMixinConfigPlugin {

    private static Hashtable<String, IConfigTranslator> translators = new Hashtable<>();

    static {
        registerConfigTranslator(BungeeTranslator.INSTANCE);
        registerConfigTranslator(EntityActivationTranslator.INSTANCE);
        registerConfigTranslator(EntityTrackingTranslator.INSTANCE);
        registerConfigTranslator(HopperCustomizationsTranslator.INSTANCE);
        registerConfigTranslator(ItemDespawn.INSTANCE);
        registerConfigTranslator(NulltileTranslator.INSTANCE);
        registerConfigTranslator(TabCompletionTranslator.INSTANCE);
    }

    public static void registerConfigTranslator(IConfigTranslator element) {
        translators.put(element.name(), element);
    }

    public static void loadConfig(String configJson) {
        System.out.println("Loading config!");
        System.out.println(configJson);

        JsonObject object = new JsonParser().parse(configJson).getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            translators.getOrDefault(entry.getKey(), NullConfigTranslator.INSTANCE).decode(entry.getKey(), entry.getValue().getAsJsonObject());
        }
    }

    public static String saveConfig() {
        JsonObject object = new JsonObject();

        for (IConfigTranslator translator : translators.values()) {
            JsonObject elementObj = new JsonObject();
            translator.encode(elementObj);
            object.add(translator.name(), elementObj);
        }

        return new GsonBuilder().setPrettyPrinting().create().toJson(object);
    }


    @Nonnull
    public static Logger LOGGER = LogManager.getLogger("vanillaboost_config");

    @Override
    public void onLoad(String mixinPackage) {
        File folder = new File(/*".", */"config");
        folder.mkdirs();
        File configFile = new File(folder, "vanillaboost_config.json");
        LOGGER.info("Loading configuration file " + configFile.getAbsolutePath());
        try {
            String config = "{}";
            if (configFile.getAbsoluteFile().exists()) {
                config = IOUtils.toString(new FileInputStream(configFile));
            }
            loadConfig(config);
            String newConf = saveConfig();
            if (!newConf.equals(config))    {
                configFile.delete();
                IOUtils.write(newConf, new FileOutputStream(configFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        for (IConfigTranslator translator : translators.values())   {
            switch (translator.shouldLoad(mixinClassName)) {
                case LOAD:
                    LOGGER.info("Loading     " + mixinClassName);
                    return true;
                case NO_LOAD:
                    LOGGER.info("Not loading " + mixinClassName);
                    return false;
                case UNKNOWN:
                    break;
            }
        }

        LOGGER.info("DefaultLoad " + mixinClassName);
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }


    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
