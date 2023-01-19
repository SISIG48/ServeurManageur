package fr.sisig48.pl.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.sisig48.pl.logs;
import fr.sisig48.pl.Utils.Uconfig;

public class CommandConfig implements CommandExecutor {
	
	//@CommandAlias("hiddencommand")
	//@CommandPermission("myplugin.admin")
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg) {
		if(!sender.isOp()) {
			sender.sendMessage("§4Unknown command");
			return true;
		}
		sender.sendMessage(String.valueOf(arg.length));
		if(arg.length < 1) return false;
		logs.add("Command exucted by : " + sender.getName() + " Command : /config");
		if(sender instanceof Player) {
			Player player = Bukkit.getPlayer(sender.getName());
			switch(arg[0]) {
				case "save":
					Uconfig.saveConfig();
					player.sendMessage("§aSave Complete");
					break;
				case "rl":
					Uconfig.reloadConfig();
					player.sendMessage("§aReload Complete");
					break;
				case "reload":
					Uconfig.reloadConfig();
					player.sendMessage("§aReload Complete");
					break;
				case "get":
					if(arg.length < 2) return false;
					player.sendMessage(Uconfig.getConfig(arg[1]));
					break;
				case "set":
					
					if(arg.length < 3) return false;
					Uconfig.setConfig(arg[1], arg[2]);
					player.sendMessage("§aConfig \"" + arg[1] + "\" has set to \"" + arg[2] + "\"");
					break;
			}
			return true;
		} else {
			switch(arg[0]) {
				case "save":
					Uconfig.saveConfig();
					System.out.println("Save Complete");
					break;
				case "rl":
					Uconfig.reloadConfig();
					System.out.println("Reload Complete");
					break;
				case "reload":
					Uconfig.reloadConfig();
					System.out.println("Reload Complete");
					break;
				case "get":
					System.out.println(Uconfig.getConfig(arg[1]));
					break;
				case "set":
					Uconfig.setConfig(arg[1], arg[2]);
					System.out.println("Config \"" + arg[1] + "\" has set to \"" + arg[2] + "\"");
					break;
			}
			return true;
		}
	}

}
