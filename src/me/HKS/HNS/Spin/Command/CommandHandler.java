package me.HKS.HNS.Spin.Command;

import me.HKS.HNS.Spin.GUI.Settings.SaveItems;
import me.HKS.HNS.Spin.GUI.Spin.SpinGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        if (args.length >= 1 && ((p.isOp() || p.hasPermission("spin.settings")) && (args[0].equalsIgnoreCase("settings") || args[0].equalsIgnoreCase("st")))) {
            SaveItems saveItems = new SaveItems();
            saveItems.OpenInv(p, 0);
        } else { // If the Player doesn't have the permission to open the Settings GUI, it opens the Spin GUI
            SpinGui spinGui = new SpinGui();
            spinGui.OpenInv(p);
        }
        return false;
    }

}