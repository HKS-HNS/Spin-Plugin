package me.HKS.HNS.Spin;

import me.HKS.HNS.Spin.GUI.Settings.SaveItems;
import me.HKS.HNS.Spin.GUI.Spin.SpinGui;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 *  Config.java - Saves and loads the config.yml
 *  @author HKS_HNS
 *  @see me.HKS.HNS.Spin.Config
 */
public class Config {
    public static File configFile = new File("plugins/SpinGui", "config.yml");
    public static FileConfiguration Config = YamlConfiguration.loadConfiguration(configFile);
    public static String prefix = "§4§lHNS:";
    public void configRl(boolean saving) { // Refreshes the config and creates it if it does not exist
        Config = YamlConfiguration.loadConfiguration(configFile);
        if (!configFile.exists()) {
            // TODO: if time add the money items and messages
            Config.set("Spin.Prefix", String.valueOf(prefix));
        }
        if (saving) {
            SaveItemsInConfig(Config);

        } else {
            loadItemsFromConfig(Config);
        }

        try {
            Config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the items and Dates from the config
     * @param config the config
     * @author HKS_HNS
     */
    public void loadItemsFromConfig(FileConfiguration config) {
        if (config.contains("Spin.Items") && SaveItems.items.size() == 0 && config.getStringList("Spin.Items") != null) {
            for (int i = 0; i < config.getInt("Spin.Items.Count"); i++) {
                SaveItems.items.add(ItemStack.deserialize(config.getConfigurationSection("Spin.Items." + i).getValues(false)));

            }

        }
        if (config.contains("Spin.Users") && config.getStringList("Spin.Users") != null && config.getInt("Spin.Users.Count") > 0) {
            for (int i = 0; i < config.getInt("Spin.Users.Count"); i++) {
                SpinGui.hasSpin.put(UUID.fromString(config.getString("Spin.Users." + i + ".UUID")), new Date(config.getLong("Spin.Users." + i + ".Date")));
            }
        }

    }

    /**
     * Saves the items and Dates in the config
     * @param config the config
     * @author HKS_HNS
     */
    public void SaveItemsInConfig(FileConfiguration config) {
        if (config.contains("Spin.Items") && config.getStringList("Spin.Items") != null && config.getInt("Spin.Items.Count") > 0) {
            for (int i = 0; i < config.getInt("Spin.Items.Count"); i++) {
                config.set("Spin.Items." + i, null);
            }
            config.set("Spin.Items.Count", 0);
        }
        if (SaveItems.items.size() > 0) {
            config.set("Spin.Items.Count", SaveItems.items.size());
            for (int i = 0; i < SaveItems.items.size(); i++) {
                config.set("Spin.Items." + i, SaveItems.items.get(i).serialize());
            }

        }
        if (config.contains("Spin.Users") && config.getStringList("Spin.Users") != null && config.getInt("Spin.Users.Count") > 0) {
            for (int i = 0; i < config.getInt("Spin.Users.Count"); i++) {
                config.set("Spin.Users." + i, null);

            }
            config.set("Spin.Users.Count", 0);
        }

        if (SpinGui.hasSpin.keySet().size() > 0) {

            config.set("Spin.Users.Count", SpinGui.hasSpin.keySet().size());
            for (int i = 0; i < SpinGui.hasSpin.keySet().size(); i++) {
                config.set("Spin.Users." + i + ".UUID", String.valueOf(SpinGui.hasSpin.keySet().toArray()[i].toString()));
                config.set("Spin.Users." + i + ".Date", SpinGui.hasSpin.get(SpinGui.hasSpin.keySet().toArray()[i]).getTime());
            }
        }

    }

}