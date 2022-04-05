package me.HKS.HNS.Spin;

import me.HKS.HNS.Spin.GUI.Settings.SaveItems;
import me.HKS.HNS.Spin.GUI.Spin.SpinGui;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 *  Config.java - Saves and loads the config.yml
 *  @author HKS_HNS
 *  @see me.HKS.HNS.Spin.Config
 */
public class Config {
    File configFile = new File("plugins/SpinGui", "config.yml");
    private static HashMap < String, String > messages = new HashMap < String, String > ();
    private static String prefix = "§4§lHNS:";
    private static ItemStack money = new ItemStack(Material.GOLD_INGOT);
    private static boolean StackIfMore = true;
    private static float OnePer = 1000;

    /**
     *  Loads the config.yml
     *  @see me.HKS.HNS.Spin.Config
     */

    public void configRl(boolean saving) { // Refreshes the config and creates it if it does not exist
        FileConfiguration Config = YamlConfiguration.loadConfiguration(configFile);
        if (!configFile.exists()) {
            Config.set("Spin.Prefix", String.valueOf(prefix));
            Config.set("Spin.Money.IgnoreNoSpace", true);
            Config.set("Spin.Money.StackIfMore", StackIfMore);
            Config.set("Spin.Money.OneItemPer", OnePer);
            Config.set("Spin.Money.Item.DisplayName", "§6Money");
            Config.set("Spin.Money.Item.Lore", Arrays.asList("§7You Won Money", "§7Amount: %amount%"));
            Config.set("Spin.Money.Item.Type", "GOLD_INGOT");
            Config.set("Spin.SpinGui.GuiTitle", "§aDaily Spin");
            Config.set("Spin.SpinGui.Items.Showcase.DisplayName", " ");
            Config.set("Spin.SpinGui.Items.Showcase.Type", "HOPPER");
            Config.set("Spin.SpinGui.Items.Skip.DisplayName", "§4>");
            Config.set("Spin.SpinGui.Items.Skip.Type", "NETHER_STAR");
            Config.set("Spin.SpinGui.Messages.AlreadySpun", "%prefix% §cYou have already spun this day! come back in %hour% hours and %min% minutes!");
            Config.set("Spin.SpinGui.Messages.NoSpace", "%prefix% §4you don't have enough space in your inventory");
            Config.set("Spin.SpinGui.Messages.WonMoney", "%prefix% §aYou have received §6%balance% §aMoney!");

        }

        if (saving) {
            SaveItemsInConfig(Config);

        } else {
            loadItemsFromConfig(Config);
        }
        loadValues(Config);
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
        if (config.contains("Spin.Items") && config.getStringList("Spin.Items") != null) {
            SaveItems.items.clear();
            for (int i = 0; i < config.getInt("Spin.Items.Count"); i++) {
                SaveItems.items.add(ItemStack.deserialize(config.getConfigurationSection("Spin.Items." + i).getValues(false)));

            }

        }
        SpinGui.hasSpin.clear();
        if (config.contains("Spin.Users") && config.getStringList("Spin.Users") != null && config.getInt("Spin.Users.Count") > 0) {
            for (int i = 0; i < config.getInt("Spin.Users.Count"); i++) {
                SpinGui.hasSpin.put(UUID.fromString(config.getString("Spin.Users." + i + ".UUID")), new Date(config.getLong("Spin.Users." + i + ".Date")));
            }
        }

    }

    /**
     * Loads the values from the config
     * @param config
     */
    public void loadValues(FileConfiguration config) {
        ItemMeta itemMeta = money.getItemMeta();
        ItemMeta itemMetaShowcase = SpinGui.showcase.getItemMeta();
        ItemMeta itemMetaSkip = SpinGui.skip.getItemMeta();
        if (config.contains("Spin.Prefix")) {
            prefix = config.getString("Spin.Prefix");
        }
        if (config.contains("Spin.Money.IgnoreNoSpace")) {
            SpinGui.ignoreNoSpace = config.getBoolean("Spin.Money.IgnoreNoSpace");
        }
        if (config.contains("Spin.Money.StackIfMore")) {
            StackIfMore = config.getBoolean("Spin.Money.StackIfMore");
        }
        if (config.contains("Spin.Money.OneItemPer")) {
            OnePer = config.getLong("Spin.Money.OneItemPer");
        }
        if (config.contains("Spin.Money.Item.DisplayName")) {
            itemMeta.setDisplayName(config.getString("Spin.Money.Item.DisplayName"));
        }
        if (config.contains("Spin.Money.Item.Lore")) {
            itemMeta.setLore(config.getStringList("Spin.Money.Item.Lore"));
        }
        if (config.contains("Spin.Money.Item.Type")) {
            if (Material.getMaterial(config.getString("Spin.Money.Item.Type")) != null) {
                money.setType(Material.getMaterial(config.getString("Spin.Money.Item.Type")));
            }
        }
        if (config.contains("Spin.SpinGui.GuiTitle")) {
            SpinGui.guiTitle = config.getString("Spin.SpinGui.GuiTitle");

        }
        if (config.contains("Spin.SpinGui.Items.Showcase.DisplayName")) {
            itemMetaShowcase.setDisplayName(config.getString("Spin.SpinGui.Items.Showcase.DisplayName"));
        }
        if (config.contains("Spin.SpinGui.Items.Showcase.Type")) {
            if (Material.getMaterial(config.getString("Spin.SpinGui.Items.Showcase.Type")) != null) {
                SpinGui.showcase.setType(Material.getMaterial(config.getString("Spin.SpinGui.Items.Showcase.Type")));
            }
        }
        if (config.contains("Spin.SpinGui.Items.Skip.DisplayName")) {
            itemMetaSkip.setDisplayName(config.getString("Spin.SpinGui.Items.Skip.DisplayName"));

        }
        if (config.contains("Spin.SpinGui.Items.Skip.Type")) {
            if (Material.getMaterial(config.getString("Spin.SpinGui.Items.Skip.Type")) != null) {
                SpinGui.skip.setType(Material.getMaterial(config.getString("Spin.SpinGui.Items.Skip.Type")));
            }
        }
        if (config.contains("Spin.SpinGui.Messages.AlreadySpun")) {
            messages.put("AlreadySpun", config.getString("Spin.SpinGui.Messages.AlreadySpun"));
        }
        if (config.contains("Spin.SpinGui.Messages.NoSpace")) {
            messages.put("NoSpace", config.getString("Spin.SpinGui.Messages.NoSpace"));
        }
        if (config.contains("Spin.SpinGui.Messages.WonMoney")) {
            messages.put("WonMoney", config.getString("Spin.SpinGui.Messages.WonMoney"));
        }
        money.setItemMeta(itemMeta);
        SpinGui.showcase.setItemMeta(itemMetaShowcase);
        SpinGui.skip.setItemMeta(itemMetaSkip);

    }

    /**
     * @return the StackIfMore
     * @author HKS_HNS
     */
    public static boolean isStackIfMore() {
        return StackIfMore;
    }

    /**
     * @return the max amount of money for the money stack
     * @author HKS_HNS
     */
    public static float getOnePer() {
        return OnePer;
    }

    /**
     * Saves the config
     * @author HKS_HNS
     */
    public static void saveConfig() {
        Config config = new Config();
        config.configRl(true);
    }

    /**
     * Loads the config
     * @author HKS_HNS
     */
    public static void loadConfig() {
        Config config = new Config();
        config.configRl(false);
    }

    /**
     * Returns the prefix of the plugin
     * @author HKS_HNS
     * @return String
     */
    public static String getMessage(String key) {
        return messages.get(key);
    }

    /**
     * Returns the Money Item
     * @author HKS_HNS
     * @return ItemStack
     */
    public static ItemStack getMoney() {
        return money;
    }

    /**
     * Returns the Prefix
     * @author HKS_HNS
     * @return String
     */
    public static String getPrefix() {
        return prefix;
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