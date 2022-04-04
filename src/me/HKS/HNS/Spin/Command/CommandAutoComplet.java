package me.HKS.HNS.Spin.Command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CommandAutoComplet implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("Spin")) {
			
			  Player p = (Player) sender;

			
			if (args.length == 0 && ((p.isOp() || p.hasPermission("spin.settings")))) {
	        	  return Arrays.asList("settings");
	          }
		}		return null;
	}

}
