package me.HKS.HNS.Spin.Command;

import me.HKS.HNS.Spin.Config;
import me.HKS.HNS.Spin.GUI.Settings.SaveItems;
import me.HKS.HNS.Spin.GUI.Spin.SpinGui;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;

/**
 * CommandHandler.java - Handles all commands
 * @author HKS
 */
public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("Spin")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§4You aren't a Player");
                return true;
            }

        }

        Player p = (Player) sender;

        // Tests if the Player has the permission to open the Settings GUI
        if (args.length >= 1 && ((p.isOp() || p.hasPermission("spin.settings")) && (args[0].equalsIgnoreCase("settings") || args[0].equalsIgnoreCase("set")))) {
            SaveItems saveItems = new SaveItems();
            saveItems.OpenInv(p, 0);
        } else if (args.length >= 2 && ((p.isOp() || p.hasPermission("spin.config")) && (args[0].equalsIgnoreCase("config") || args[0].equalsIgnoreCase("cf")))) {
            if (args[1].equalsIgnoreCase("load")) {
                Config.loadConfig();
            } else if (args[1].equalsIgnoreCase("save")) {
                Config.saveConfig();
            }
        }  else if (args.length >= 2 && ((p.isOp() || p.hasPermission("spin.reset")) && (args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("rs")))) {
            if (Bukkit.getPlayer(args[1]) != null) {
                Player target = Bukkit.getPlayer(args[1]);
                if (SpinGui.hasSpin.get(target.getUniqueId()) != null) {
                    SpinGui.hasSpin.remove(target.getUniqueId());
                    p.sendMessage(Config.getMessage("Reset").replace("%player%", target.getName()).replace("%prefix%", Config.getPrefix()));
                }
            } else {
                p.sendMessage(Config.getMessage("NoPlayer").replace("%prefix%", Config.getPrefix()).replace("%player%", args[1]));
            }

    }else if (args.length >= 1 && ((p.isOp() || p.hasPermission("spin.help")) && (args[0].equalsIgnoreCase("help")))) {
            p.sendMessage("§6§lHNS Spin Help:");
            p.sendMessage("§6/spin - Opens the Spin GUI");
            p.sendMessage("§6/spin settings - Opens the Settings GUI");
            p.sendMessage("§6/spin config load - Loads the config");
            p.sendMessage("§6/spin config save - Saves the config");
            p.sendMessage("§6/spin reset <player> - Resets the Spin time for the player");
        } else { // If the Player doesn't have the permission to open the Settings GUI, it opens the Spin GUI
            SpinGui spinGui = new SpinGui();
            spinGui.OpenInv(p);
        }
        return false;
    }

}