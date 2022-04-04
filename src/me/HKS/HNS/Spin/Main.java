package me.HKS.HNS.Spin;

import me.HKS.HNS.Spin.Command.CommandAutoComplet;
import me.HKS.HNS.Spin.Command.CommandHandler;
import me.HKS.HNS.Spin.GUI.Settings.SaveItems;
import me.HKS.HNS.Spin.GUI.Spin.SpinGui;
import me.HKS.HNS.Spin.Packet.SignPacket;
import net.minecraft.server.v1_12_R1.BlockGrass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

/**
 * Main.java - Main class of the spin plugin
 * @author HKS
 */
public class Main extends JavaPlugin {
    public static Main getInstance;
    private static Economy econ = null;

    @Override
    public void onEnable() {

        if (!setupEconomy()) {
            Bukkit.getLogger().warning(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Config config = new Config();
        config.configRl(false);
        getInstance = this;
        getCommand("Spin").setExecutor(new CommandHandler());
        getCommand("Spin").setTabCompleter(new CommandAutoComplet());
        getServer().getPluginManager().registerEvents(new SaveItems(), this);
        getServer().getPluginManager().registerEvents(new SpinGui(), this);
        getServer().getPluginManager().registerEvents(new SignPacket(), this);
    }

    @Override
    public void onDisable() {
        Config config = new Config();
        config.configRl(true);
    }

    /**
     * @return True if the Vault dependency is found, otherwise false
     * @author HKS
     */
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider < Economy > rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    /**
     * @return The economy instance
     * @author HKS
     */
    public static Economy getEconomy() {
        return econ;
    }

    /**
     * @return This instance
     * @author HKS
     */
    public static Main getInstance() {
        return getInstance;
    }

}