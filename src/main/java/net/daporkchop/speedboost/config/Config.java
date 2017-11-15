package net.daporkchop.speedboost.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.daporkchop.speedboost.config.impl.BungeeTranslator;
import net.daporkchop.speedboost.config.impl.EntityActivationTranslator;
import net.daporkchop.speedboost.config.impl.ItemDespawn;
import net.daporkchop.speedboost.config.impl.NulltileTranslator;
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
        registerConfigTranslator(ItemDespawn.INSTANCE);
        registerConfigTranslator(NulltileTranslator.INSTANCE);
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
        LOGGER.info("Checking config option for " + mixinClassName);
        for (IConfigTranslator translator : translators.values())   {
            switch (translator.shouldLoad(mixinClassName)) {
                case LOAD:
                    return true;
                case NO_LOAD:
                    return false;
                case UNKNOWN:
                    break;
            }
        }

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
