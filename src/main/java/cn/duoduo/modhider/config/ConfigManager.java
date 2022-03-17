package cn.duoduo.modhider.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigManager {
    public static final ConfigManager instance = new ConfigManager();
    private final Configuration config;

    public ConfigManager() {
        File configFile = new File(Loader.instance().getConfigDir(), "hiddenmods.cfg");

        this.config = new Configuration(configFile);

        config.load();
    }

    public String[] getHiddenMods() {
        Property property = config.get("Hidden Mods", "hidden_mods", new String[]{""}, "Hidden client-side mods.");
        return property.getStringList();
    }

    public void setHiddenMods(String[] mods) {
        Property property = config.get("Hidden Mods", "hidden_mods", new String[]{""}, "Hidden client-side mods.");
        property.set(mods);
        config.save();
    }

    public void addHiddenMod(String modId) {
        String[] hiddenMods = getHiddenMods();
        hiddenMods = Arrays.copyOf(hiddenMods, hiddenMods.length + 1);
        hiddenMods[hiddenMods.length - 1] = modId;
        setHiddenMods(hiddenMods);
    }

    public void removeHiddenMod(String modId) {
        String[] hiddenMods = getHiddenMods();
        List<String> list = Arrays.stream(hiddenMods).collect(Collectors.toList());
        list.removeIf(next -> next.equals(modId));
        setHiddenMods(list.toArray(new String[]{}));
    }

}
